package co.edu.unipacifico.demo.services;

import co.edu.unipacifico.demo.dtos.MovimientosDTO;

import java.util.List;
import java.util.Optional;

public interface MovimientosService {
    
    // Registrar entrada de vehículo
    MovimientosDTO registrarEntrada(MovimientosDTO movimientoDTO);
    
    // Registrar salida de vehículo
    MovimientosDTO registrarSalida(Long movimientoId);
    
    // Consultar movimiento activo de un vehículo
    Optional<MovimientosDTO> consultarMovimientoActivoPorVehiculo(Long vehiculoId);
    
    // Consultar todos los movimientos activos
    List<MovimientosDTO> consultarMovimientosActivos();
    
    // Consultar historial de movimientos de un vehículo
    List<MovimientosDTO> consultarHistorialPorVehiculo(Long vehiculoId);
    
    // Consultar historial de movimientos de un usuario
    List<MovimientosDTO> consultarHistorialPorUsuario(Long usuarioId);
    
    // Consultar movimiento por ID
    Optional<MovimientosDTO> consultarMovimientoPorId(Long id);
}