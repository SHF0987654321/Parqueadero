package co.edu.unipacifico.demo.controllers;

import co.edu.unipacifico.demo.dtos.MovimientosDTO;
import co.edu.unipacifico.demo.services.MovimientosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientosController {

    private final MovimientosService movimientosService;

    // Registrar entrada de un vehículo
    @PostMapping("/entrada")
    public ResponseEntity<MovimientosDTO> registrarEntrada(@Valid @RequestBody MovimientosDTO movimientoDTO) {
        MovimientosDTO nuevoMovimiento = movimientosService.registrarEntrada(movimientoDTO);
        return new ResponseEntity<>(nuevoMovimiento, HttpStatus.CREATED);
    }

    // Registrar salida de un vehículo
    @PutMapping("/salida/{movimientoId}")
    public ResponseEntity<MovimientosDTO> registrarSalida(@PathVariable Long movimientoId) {
        MovimientosDTO movimientoActualizado = movimientosService.registrarSalida(movimientoId);
        return ResponseEntity.ok(movimientoActualizado);
    }

    // Consultar movimiento activo de un vehículo
    @GetMapping("/activo/vehiculo/{vehiculoId}")
    public ResponseEntity<MovimientosDTO> consultarMovimientoActivoPorVehiculo(@PathVariable Long vehiculoId) {
        return movimientosService.consultarMovimientoActivoPorVehiculo(vehiculoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Consultar todos los movimientos activos
    @GetMapping("/activos")
    public ResponseEntity<List<MovimientosDTO>> consultarMovimientosActivos() {
        List<MovimientosDTO> movimientos = movimientosService.consultarMovimientosActivos();
        return ResponseEntity.ok(movimientos);
    }

    // Consultar historial de movimientos de un vehículo
    @GetMapping("/historial/vehiculo/{vehiculoId}")
    public ResponseEntity<List<MovimientosDTO>> consultarHistorialPorVehiculo(@PathVariable Long vehiculoId) {
        List<MovimientosDTO> historial = movimientosService.consultarHistorialPorVehiculo(vehiculoId);
        return ResponseEntity.ok(historial);
    }

    // Consultar historial de movimientos de un usuario
    @GetMapping("/historial/usuario/{usuarioId}")
    public ResponseEntity<List<MovimientosDTO>> consultarHistorialPorUsuario(@PathVariable Long usuarioId) {
        List<MovimientosDTO> historial = movimientosService.consultarHistorialPorUsuario(usuarioId);
        return ResponseEntity.ok(historial);
    }

    // Consultar un movimiento por ID
    @GetMapping("/{id}")
    public ResponseEntity<MovimientosDTO> consultarMovimientoPorId(@PathVariable Long id) {
        return movimientosService.consultarMovimientoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}