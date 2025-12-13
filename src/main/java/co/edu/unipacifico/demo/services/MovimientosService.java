package co.edu.unipacifico.demo.services;

import co.edu.unipacifico.demo.dtos.MovimientosRequest;
import co.edu.unipacifico.demo.dtos.MovimientosResponse;

import java.util.List;
import java.util.Optional;

public interface MovimientosService {
    
    // Registrar entrada de vehículo
    MovimientosResponse registrarEntrada(MovimientosRequest movimientoDTO);
    
    // Registrar salida de vehículo
    MovimientosResponse registrarSalida(String placa);
    
    // Consultar movimiento activo de un vehículo
    Optional<MovimientosResponse> consultarMovimientoActivoPorPlaca(String placa);
    
    // Consultar todos los movimientos activos
    List<MovimientosResponse> consultarMovimientosActivos();
    
    // Consultar historial de movimientos de un vehículo
    List<MovimientosResponse> consultarHistorialPorPlaca(String placa);
    
    // Consultar historial de movimientos de un usuario
    List<MovimientosResponse> consultarHistorialPorUsuario(Long usuarioId);
    
    // Consultar movimiento por ID
    Optional<MovimientosResponse> consultarMovimientoPorId(Long id);

    List<MovimientosResponse> consultarTodosLosMovimientos();
}