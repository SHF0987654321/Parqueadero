package co.edu.unipacifico.demo.services;

import co.edu.unipacifico.demo.dtos.MovimientosRequest;
import co.edu.unipacifico.demo.dtos.MovimientosResponse;
import co.edu.unipacifico.demo.exceptions.DatabaseException;
import co.edu.unipacifico.demo.mappers.MovimientosMapper;
import co.edu.unipacifico.demo.models.*;
import co.edu.unipacifico.demo.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MovimientosServiceImpl implements MovimientosService {

    private final MovimientosRepository movimientosRepository;
    private final VehiculosRepository vehiculosRepository;
    private final UsuariosRepository usuariosRepository;
    private final LugaresRepository lugaresRepository;
    private final MovimientosMapper movimientosMapper;

    @Override
    @Transactional
    public MovimientosResponse registrarEntrada(MovimientosRequest movimiento) {
        try {
        // --- 1. VALIDAR Y/O REGISTRAR VEHÍCULO ---
            Vehiculos vehiculo;
            Optional<Vehiculos> vehiculoExistente = vehiculosRepository.findByPlaca(movimiento.getPlaca());
    
            if (vehiculoExistente.isPresent()) {
                vehiculo = vehiculoExistente.get();
            } else {
                // Regla 2: Si el vehículo no existe, se registra automáticamente.
                // Para registrar un vehículo nuevo, el tipo (vehiculoTipo en el request) es obligatorio.
                if (movimiento.getTipo() == null || movimiento.getTipo().trim().isEmpty()) {
                    throw new RuntimeException("El tipo de vehículo es obligatorio para registrar un vehículo nuevo.");
                }
                vehiculo = registrarNuevoVehiculo(movimiento); 
            }
    
            // Validar que el vehículo no tenga un movimiento activo (ya esté dentro)
            Optional<Movimientos> movimientoActivo = movimientosRepository
                    .findMovimientoActivoByVehiculoId(vehiculo.getId());
    
            if (movimientoActivo.isPresent()) {
                throw new RuntimeException("El vehículo ya tiene un movimiento activo en el sistema.");
            }
    
            // --- 2. ASIGNAR Y VALIDAR LUGAR (Lógica Condicional) ---
            Lugares lugar;
            // Obtenemos el tipo de vehículo de la entidad, no del Request (es más confiable)
            String tipoVehiculo = vehiculo.getTipo(); 
            String nombreLugarSolicitado = movimiento.getNombreLugar();

            // Si el nombre del lugar fue proporcionado
            if (nombreLugarSolicitado != null && !nombreLugarSolicitado.trim().isEmpty()) {
        
                // A. Búsqueda y Validación de Lugar ESPECÍFICO
                lugar = lugaresRepository.findByNombre(nombreLugarSolicitado)
                        .orElseThrow(() -> new RuntimeException("Lugar no encontrado con nombre: " + nombreLugarSolicitado));
        
                // Regla 1: Validar que el tipo de vehículo coincida con el tipo de lugar
                if (!lugar.getTipo().equalsIgnoreCase(tipoVehiculo)) {
                     throw new RuntimeException("El lugar '" + nombreLugarSolicitado + "' es para tipo '" + lugar.getTipo() + "' y el vehículo es tipo '" + tipoVehiculo + "'.");
                }
        
                // Validar que el lugar ESPECÍFICO esté libre
                List<Movimientos> movimientosLugar = movimientosRepository
                        .findMovimientosActivosByLugarId(lugar.getId());
        
                if (!movimientosLugar.isEmpty()) {
                    throw new RuntimeException("El lugar '" + nombreLugarSolicitado + "' ya está ocupado.");
                }
    
            } else {
        
                // B. Búsqueda y Asignación de Lugar AUTOMÁTICO
            
                // La validación del tipo de vehículo ya se hizo arriba si el vehículo era nuevo.
                // Aquí, buscamos el lugar disponible usando el tipo.
                lugar = lugaresRepository.findPrimerLugarDisponiblePorTipo(tipoVehiculo)
                        .orElseThrow(() -> new RuntimeException("No hay lugares disponibles de tipo '" + tipoVehiculo + "' en el parqueadero."));
            }
    
            // --- 3. VALIDAR USUARIO ---
            Usuarios usuario = usuariosRepository.findById(movimiento.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
    
            // --- 4. CREAR Y GUARDAR MOVIMIENTO (FECHA ASIGNADA EN SERVIDOR) ---
            Movimientos movimientoNuevo = new Movimientos();
            movimientoNuevo.setVehiculo(vehiculo);
            movimientoNuevo.setUsuario(usuario);
            movimientoNuevo.setLugar(lugar);
            // ASIGNACIÓN DE FECHA EN EL SERVIDOR (CORRECCIÓN CLAVE)
            movimientoNuevo.setFechaEntrada(LocalDateTime.now());
    
            Movimientos movimientoGuardado = movimientosRepository.save(movimientoNuevo);
    
            return movimientosMapper.toDTO(movimientoGuardado);
    
        } catch (RuntimeException e) {
            // Las excepciones de negocio se relanzan para que el ControllerAdvice las maneje como 400
            throw e;
        } catch (Exception e) {
            // Excepciones no esperadas se envuelven en una excepción de la base de datos
            throw new DatabaseException("Error fatal al registrar la entrada del vehículo.", e);
        }
    }

    private Vehiculos registrarNuevoVehiculo(MovimientosRequest movimiento) {
        // 1. Crear una nueva entidad Vehiculos
        Vehiculos nuevoVehiculo = new Vehiculos();
    
        // 2. Asignar los campos necesarios
        nuevoVehiculo.setPlaca(movimiento.getPlaca());
        nuevoVehiculo.setTipo(movimiento.getTipo()); 
    
        // 3. Guardar en el repositorio de Vehiculos
        return vehiculosRepository.save(nuevoVehiculo);
    }

    @Override
    @Transactional
    public MovimientosResponse registrarSalida(String placa) {
        try {
            // --- 1. BUSCAR VEHÍCULO POR PLACA ---
            Vehiculos vehiculo = vehiculosRepository.findByPlaca(placa)
                    .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con placa: " + placa));

            // --- 2. BUSCAR MOVIMIENTO ACTIVO POR ID DEL VEHÍCULO ---
            // Usamos el ID del vehículo para encontrar el único movimiento sin fecha de salida.
            Movimientos movimiento = movimientosRepository
                    .findMovimientoActivoByVehiculoId(vehiculo.getId())
                    .orElseThrow(() -> new RuntimeException("No se encontró un movimiento de entrada activo para el vehículo con placa: " + placa));

            // Registrar la hora de salida
            movimiento.setFechaSalida(LocalDateTime.now());
        
            Movimientos movimientoActualizado = movimientosRepository.save(movimiento);
        
            return movimientosMapper.toDTO(movimientoActualizado);

        } catch (RuntimeException e) {
            // Re-lanzar excepciones de negocio (ej. No encontrado)
            throw e;
        } catch (Exception e) {
            // Capturar y envolver excepciones de bajo nivel
            throw new DatabaseException("Error al registrar la salida del vehículo.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MovimientosResponse> consultarMovimientoActivoPorPlaca(String placa) {
        try {
            Vehiculos vehiculo = vehiculosRepository.findByPlaca(placa)
                    .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con placa: " + placa));
            return movimientosRepository.findMovimientoActivoByVehiculoId(vehiculo.getId())
                    .map(movimientosMapper::toDTO);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error al consultar movimiento activo del vehículo.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientosResponse> consultarMovimientosActivos() {
        try {
            return movimientosRepository.findMovimientosActivos().stream()
                    .map(movimientosMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseException("Error al consultar movimientos activos.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientosResponse> consultarHistorialPorPlaca(String placa) {
        try {
            Vehiculos vehiculo = vehiculosRepository.findByPlaca(placa)
                    .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con placa: " + placa));
            return movimientosRepository.findByVehiculoIdOrderByFechaEntradaDesc(vehiculo.getId()).stream()
                    .map(movimientosMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error al consultar historial del vehículo.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientosResponse> consultarHistorialPorUsuario(Long usuarioId) {
        try {
            return movimientosRepository.findByUsuarioIdOrderByFechaEntradaDesc(usuarioId).stream()
                    .map(movimientosMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseException("Error al consultar historial del usuario.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MovimientosResponse> consultarMovimientoPorId(Long id) {
        try {
            return movimientosRepository.findById(id)
                    .map(movimientosMapper::toDTO);
        } catch (Exception e) {
            throw new DatabaseException("Error al consultar el movimiento por ID.", e);
        }
    }
}