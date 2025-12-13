package co.edu.unipacifico.demo.controllers;

import co.edu.unipacifico.demo.dtos.MovimientosRequest;
import co.edu.unipacifico.demo.dtos.MovimientosResponse;
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
    public ResponseEntity<MovimientosResponse> registrarEntrada(@Valid @RequestBody MovimientosRequest movimientoDTO) {
        MovimientosResponse nuevoMovimiento = movimientosService.registrarEntrada(movimientoDTO);
        return new ResponseEntity<>(nuevoMovimiento, HttpStatus.CREATED);
    }

    // Registrar salida de un vehículo
    @PutMapping("/salida/{placa}")
    public ResponseEntity<MovimientosResponse> registrarSalida(@PathVariable String placa) {
        MovimientosResponse movimientoActualizado = movimientosService.registrarSalida(placa);
        return ResponseEntity.ok(movimientoActualizado);
    }

    // Consultar movimiento activo de un vehículo
    @GetMapping("/activo/vehiculo/{vehiculoId}")
    public ResponseEntity<MovimientosResponse> consultarMovimientoActivoPorPlaca(@PathVariable String placa) {
        return movimientosService.consultarMovimientoActivoPorPlaca(placa)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Consultar todos los movimientos activos
    @GetMapping("/activos")
    public ResponseEntity<List<MovimientosResponse>> consultarMovimientosActivos() {
        List<MovimientosResponse> movimientos = movimientosService.consultarMovimientosActivos();
        return ResponseEntity.ok(movimientos);
    }

    // Consultar historial de movimientos de un vehículo
    @GetMapping("/historial/vehiculo/{placa}")
    public ResponseEntity<List<MovimientosResponse>> consultarHistorialPorPlaca(@PathVariable String placa) {
        List<MovimientosResponse> historial = movimientosService.consultarHistorialPorPlaca(placa);
        return ResponseEntity.ok(historial);
    }

    // Consultar historial de movimientos de un usuario
    @GetMapping("/historial/usuario/{usuarioId}")
    public ResponseEntity<List<MovimientosResponse>> consultarHistorialPorUsuario(@PathVariable Long usuarioId) {
        List<MovimientosResponse> historial = movimientosService.consultarHistorialPorUsuario(usuarioId);
        return ResponseEntity.ok(historial);
    }

    // Consultar un movimiento por ID
    @GetMapping("/{id}")
    public ResponseEntity<MovimientosResponse> consultarMovimientoPorId(@PathVariable Long id) {
        return movimientosService.consultarMovimientoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    // Consultar todos los movimientos (activos e inactivos)
    @GetMapping("/historial")
    public ResponseEntity<List<MovimientosResponse>> consultarTodosLosMovimientos() {
        List<MovimientosResponse> movimientos = movimientosService.consultarTodosLosMovimientos();
        return ResponseEntity.ok(movimientos);
    }
}