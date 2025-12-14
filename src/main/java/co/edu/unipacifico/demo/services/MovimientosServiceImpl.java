package co.edu.unipacifico.demo.services;

import co.edu.unipacifico.demo.dtos.MovimientosRequest;
import co.edu.unipacifico.demo.dtos.MovimientosResponse;
import co.edu.unipacifico.demo.exceptions.DatabaseException;
import co.edu.unipacifico.demo.exceptions.InvalidOperationExeception;
import co.edu.unipacifico.demo.exceptions.ResourceNotFoundException;
import co.edu.unipacifico.demo.mappers.MovimientosMapper;
import co.edu.unipacifico.demo.models.*;
import co.edu.unipacifico.demo.repositories.*;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
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

            Optional<Vehiculos> vehiculoOpt =
                    vehiculosRepository.findByPlaca(movimiento.getPlaca());

            if (vehiculoOpt.isPresent()) {
                vehiculo = vehiculoOpt.get();
            } else {
                if (movimiento.getTipo() == null || movimiento.getTipo().isBlank()) {
                    throw new InvalidOperationExeception(
                            "El tipo de vehículo es obligatorio para registrar un vehículo nuevo.");
                }
                Vehiculos nuevoVehiculo = new Vehiculos();
                nuevoVehiculo.setPlaca(movimiento.getPlaca());
                nuevoVehiculo.setTipo(movimiento.getTipo());
                vehiculo = vehiculosRepository.save(nuevoVehiculo);
            }

            // Validar que el vehículo no esté ya dentro
            if (movimientosRepository
                    .findByVehiculoIdAndFechaSalidaIsNull(vehiculo.getId())
                    .isPresent()) {
                throw new InvalidOperationExeception(
                        "El vehículo ya tiene un movimiento activo en el sistema.");
            }

            // --- 2. ASIGNAR LUGAR ---
            String tipoVehiculo = vehiculo.getTipo();
            Lugares lugar;

            if (movimiento.getNombreLugar() != null && !movimiento.getNombreLugar().isBlank()) {

                // Lugar específico
                lugar = lugaresRepository.findByNombre(movimiento.getNombreLugar())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Lugar no encontrado con nombre: " + movimiento.getNombreLugar()));

                if (!lugar.getTipo().equalsIgnoreCase(tipoVehiculo)) {
                    throw new InvalidOperationExeception(
                            "El lugar '" + lugar.getNombre() + "' es para tipo '" +
                                    lugar.getTipo() + "' y el vehículo es tipo '" + tipoVehiculo + "'.");
                }

                if (movimientosRepository
                        .existsByLugarIdAndFechaSalidaIsNull(lugar.getId())) {
                    throw new InvalidOperationExeception(
                            "El lugar '" + lugar.getNombre() + "' ya está ocupado.");
                }

            } else {

                // Lugar automático
                lugar = lugaresRepository
                        .findLugarLibrePorTipo(tipoVehiculo, PageRequest.of(0, 1))
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> 
                            new InvalidOperationExeception(
                                "No hay lugares disponibles de tipo '" + tipoVehiculo 
                                + "' en el parqueadero."));
            }

            // --- 3. VALIDAR USUARIO ---
            Usuarios usuario = usuariosRepository.findById(movimiento.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

            // --- 4. CREAR MOVIMIENTO ---
            Movimientos movimientoNuevo = new Movimientos();
            movimientoNuevo.setVehiculo(vehiculo);
            movimientoNuevo.setUsuario(usuario);
            movimientoNuevo.setLugar(lugar);
            movimientoNuevo.setFechaEntrada(LocalDateTime.now());

            return movimientosMapper.toDTO(
                    movimientosRepository.save(movimientoNuevo));

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException(
                    "Error fatal al registrar la entrada del vehículo.", e);
        }
    }

    @Override
    @Transactional
    public MovimientosResponse registrarSalida(String placa) {
        try {
            // --- 1. BUSCAR VEHÍCULO POR PLACA ---
            Vehiculos vehiculo = vehiculosRepository.findByPlaca(placa)
                    .orElseThrow(() -> 
                    new ResourceNotFoundException(
                        "Vehículo no encontrado con placa: " + placa));

            // --- 2. BUSCAR MOVIMIENTO ACTIVO POR ID DEL VEHÍCULO ---
            // Usamos el ID del vehículo para encontrar el único movimiento sin fecha de salida.
            Movimientos movimiento = movimientosRepository
                    .findByVehiculoIdAndFechaSalidaIsNull(vehiculo.getId())
                    .orElseThrow(() -> 
                        new InvalidOperationExeception(
                            "No se encontró un movimiento de entrada activo para el vehículo con placa: " + placa));

            // Registrar la hora de salida
            movimiento.setFechaSalida(LocalDateTime.now());
        
            Movimientos movimientoActualizado = movimientosRepository.save(movimiento);
        
            return movimientosMapper.toDTO(movimientoActualizado);

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            // Capturar y envolver excepciones de bajo nivel
            throw new DatabaseException(
                "Error al registrar la salida del vehículo.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MovimientosResponse consultarMovimientoActivoPorPlaca(String placa) {
        try {
            Vehiculos vehiculo = vehiculosRepository.findByPlaca(placa)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Vehículo no encontrado con placa: " + placa));

            return movimientosRepository.findByVehiculoIdAndFechaSalidaIsNull(vehiculo.getId())
                .map(movimientosMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "No hay movimiento activo para el vehículo con placa: " + placa));

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            // Errores técnicos → 500
            throw new DatabaseException(
                "Error al consultar movimiento activo del vehículo.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientosResponse> consultarMovimientosActivos() {
        try {
            return movimientosRepository.findByFechaSalidaIsNull().stream()
                    .map(movimientosMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException(
                "Error al consultar movimientos activos.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientosResponse> consultarHistorialPorPlaca(String placa) {
        try {
            Vehiculos vehiculo = vehiculosRepository.findByPlaca(placa)
                    .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehículo no encontrado con placa: " + placa));
            return movimientosRepository.findByVehiculoIdOrderByFechaEntradaDesc(
                vehiculo.getId()).stream()
                    .map(movimientosMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException(
                "Error al consultar historial del vehículo.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientosResponse> consultarHistorialPorUsuario(Long usuarioId) {
        try {
            return movimientosRepository.findByUsuarioIdOrderByFechaEntradaDesc(
                usuarioId).stream()
                    .map(movimientosMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException(
                "Error al consultar historial del usuario.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MovimientosResponse consultarMovimientoPorId(Long id) {
        try {
        Movimientos movimiento = movimientosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Movimiento no encontrado con ID: " + id));
        return movimientosMapper.toDTO(movimiento);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException(
                "Error al consultar el movimiento por ID.", e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MovimientosResponse> consultarTodosLosMovimientos() {
        try {
            return movimientosRepository.findAllByOrderByFechaEntradaDesc().stream()
                    .map(movimientosMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException(
                "Error al consultar todos los movimientos.", e);
        }
    }
}