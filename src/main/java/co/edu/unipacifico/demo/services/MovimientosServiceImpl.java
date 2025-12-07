package co.edu.unipacifico.demo.services;

import co.edu.unipacifico.demo.dtos.MovimientosDTO;
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
    public MovimientosDTO registrarEntrada(MovimientosDTO movimientoDTO) {
        try {
            // Validar que el vehículo no tenga un movimiento activo
            Optional<Movimientos> movimientoActivo = movimientosRepository
                    .findMovimientoActivoByVehiculoId(movimientoDTO.getVehiculoId());
            
            if (movimientoActivo.isPresent()) {
                throw new RuntimeException("El vehículo ya tiene un movimiento activo en el sistema.");
            }
            
            // Validar que el lugar esté libre
            List<Movimientos> movimientosLugar = movimientosRepository
                    .findMovimientosActivosByLugarId(movimientoDTO.getLugarId());
            
            if (!movimientosLugar.isEmpty()) {
                throw new RuntimeException("El lugar ya está ocupado.");
            }
            
            // Buscar entidades relacionadas
            Vehiculos vehiculo = vehiculosRepository.findById(movimientoDTO.getVehiculoId())
                    .orElseThrow(() -> new RuntimeException("Vehículo no encontrado."));
            
            Usuarios usuario = usuariosRepository.findById(movimientoDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
            
            Lugares lugar = lugaresRepository.findById(movimientoDTO.getLugarId())
                    .orElseThrow(() -> new RuntimeException("Lugar no encontrado."));
            
            // Crear movimiento
            Movimientos movimiento = new Movimientos();
            movimiento.setVehiculo(vehiculo);
            movimiento.setUsuario(usuario);
            movimiento.setLugar(lugar);
            movimiento.setFechaEntrada(movimientoDTO.getFechaEntrada() != null 
                    ? movimientoDTO.getFechaEntrada() 
                    : LocalDateTime.now());
            
            Movimientos movimientoGuardado = movimientosRepository.save(movimiento);
            return movimientosMapper.toDTO(movimientoGuardado);
            
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error al registrar la entrada.", e);
        }
    }

    @Override
    public MovimientosDTO registrarSalida(Long movimientoId) {
        try {
            Movimientos movimiento = movimientosRepository.findById(movimientoId)
                    .orElseThrow(() -> new RuntimeException("Movimiento no encontrado."));
            
            if (movimiento.getFechaSalida() != null) {
                throw new RuntimeException("Este movimiento ya tiene fecha de salida registrada.");
            }
            
            movimiento.setFechaSalida(LocalDateTime.now());
            Movimientos movimientoActualizado = movimientosRepository.save(movimiento);
            
            return movimientosMapper.toDTO(movimientoActualizado);
            
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error al registrar la salida.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MovimientosDTO> consultarMovimientoActivoPorVehiculo(Long vehiculoId) {
        try {
            return movimientosRepository.findMovimientoActivoByVehiculoId(vehiculoId)
                    .map(movimientosMapper::toDTO);
        } catch (Exception e) {
            throw new DatabaseException("Error al consultar movimiento activo del vehículo.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientosDTO> consultarMovimientosActivos() {
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
    public List<MovimientosDTO> consultarHistorialPorVehiculo(Long vehiculoId) {
        try {
            return movimientosRepository.findByVehiculoIdOrderByFechaEntradaDesc(vehiculoId).stream()
                    .map(movimientosMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseException("Error al consultar historial del vehículo.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientosDTO> consultarHistorialPorUsuario(Long usuarioId) {
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
    public Optional<MovimientosDTO> consultarMovimientoPorId(Long id) {
        try {
            return movimientosRepository.findById(id)
                    .map(movimientosMapper::toDTO);
        } catch (Exception e) {
            throw new DatabaseException("Error al consultar el movimiento por ID.", e);
        }
    }
}