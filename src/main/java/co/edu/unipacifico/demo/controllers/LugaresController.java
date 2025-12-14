package co.edu.unipacifico.demo.controllers;

import co.edu.unipacifico.demo.dtos.LugaresRequest;
import co.edu.unipacifico.demo.dtos.LugaresResponse;
import co.edu.unipacifico.demo.dtos.LugaresEstadisticas;
import co.edu.unipacifico.demo.services.LugaresService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lugares")
@RequiredArgsConstructor
public class LugaresController {

    private final LugaresService lugaresService;

    // Crear un nuevo lugar
    @PostMapping
    public ResponseEntity<LugaresResponse> crearLugar(@Valid @RequestBody LugaresRequest lugarDTO) {
        LugaresResponse nuevoLugar = lugaresService.crearLugar(lugarDTO);
        return new ResponseEntity<>(nuevoLugar, HttpStatus.CREATED);
    }

    // Obtener todos los lugares
    @GetMapping
    public ResponseEntity<List<LugaresResponse>> consultarTodosLosLugares() {
        List<LugaresResponse> lugares = lugaresService.consultarTodosLosLugares();
        return ResponseEntity.ok(lugares);
    }

    // Obtener un lugar por ID
    @GetMapping("/{id}")
    public ResponseEntity<LugaresResponse> consultarLugarPorId(@PathVariable Long id) {
        LugaresResponse lugar = lugaresService.consultarLugarPorId(id);
        return ResponseEntity.ok(lugar);
    }

    // Actualizar un lugar
    @PutMapping("/{id}")
    public ResponseEntity<LugaresResponse> actualizarLugar(
            @PathVariable Long id,
            @Valid @RequestBody LugaresRequest lugarDTO) {
        LugaresResponse lugarActualizado = lugaresService.actualizarLugar(id, lugarDTO);
        return ResponseEntity.ok(lugarActualizado);
    }

    // Eliminar un lugar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLugar(@PathVariable Long id) {
        lugaresService.eliminarLugar(id);
        return ResponseEntity.noContent().build();
    }

    // Consultar lugares por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<LugaresResponse>> consultarLugaresPorTipo(@PathVariable String tipo) {
        List<LugaresResponse> lugares = lugaresService.consultarLugaresPorTipo(tipo);
        return ResponseEntity.ok(lugares);
    }

    // Consultar lugares ocupados
    @GetMapping("/ocupados")
    public ResponseEntity<List<LugaresResponse>> consultarLugaresOcupados(
            @RequestParam(required = false) String tipo) {
        List<LugaresResponse> lugares = lugaresService.consultarLugaresOcupados(tipo);
        return ResponseEntity.ok(lugares);
    }

    // Consultar lugares libres
    @GetMapping("/libres")
    public ResponseEntity<List<LugaresResponse>> consultarLugaresLibres(
            @RequestParam(required = false) String tipo) {
        List<LugaresResponse> lugares = lugaresService.consultarLugaresLibres(tipo);
        return ResponseEntity.ok(lugares);
    }

    // Obtener estadísticas de lugares
    @GetMapping("/estadisticas")
    public ResponseEntity<LugaresEstadisticas> obtenerEstadisticas(
            @RequestParam(required = false) String tipo) {
        LugaresEstadisticas estadisticas = lugaresService.obtenerEstadisticas(tipo);
        return ResponseEntity.ok(estadisticas);
    }
}
