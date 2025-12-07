package co.edu.unipacifico.demo.controllers;

import co.edu.unipacifico.demo.dtos.LugaresDTO;
import co.edu.unipacifico.demo.dtos.LugaresEstadisticasDTO;
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
    public ResponseEntity<LugaresDTO> crearLugar(@Valid @RequestBody LugaresDTO lugarDTO) {
        LugaresDTO nuevoLugar = lugaresService.crearLugar(lugarDTO);
        return new ResponseEntity<>(nuevoLugar, HttpStatus.CREATED);
    }

    // Obtener todos los lugares
    @GetMapping
    public ResponseEntity<List<LugaresDTO>> consultarTodosLosLugares() {
        List<LugaresDTO> lugares = lugaresService.consultarTodosLosLugares();
        return ResponseEntity.ok(lugares);
    }

    // Obtener un lugar por ID
    @GetMapping("/{id}")
    public ResponseEntity<LugaresDTO> consultarLugarPorId(@PathVariable Long id) {
        return lugaresService.consultarLugarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar un lugar
    @PutMapping("/{id}")
    public ResponseEntity<LugaresDTO> actualizarLugar(
            @PathVariable Long id,
            @Valid @RequestBody LugaresDTO lugarDTO) {
        LugaresDTO lugarActualizado = lugaresService.actualizarLugar(id, lugarDTO);
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
    public ResponseEntity<List<LugaresDTO>> consultarLugaresPorTipo(@PathVariable String tipo) {
        List<LugaresDTO> lugares = lugaresService.consultarLugaresPorTipo(tipo);
        return ResponseEntity.ok(lugares);
    }

    // Consultar lugares ocupados
    @GetMapping("/ocupados")
    public ResponseEntity<List<LugaresDTO>> consultarLugaresOcupados(
            @RequestParam(required = false) String tipo) {
        List<LugaresDTO> lugares = lugaresService.consultarLugaresOcupados(tipo);
        return ResponseEntity.ok(lugares);
    }

    // Consultar lugares libres
    @GetMapping("/libres")
    public ResponseEntity<List<LugaresDTO>> consultarLugaresLibres(
            @RequestParam(required = false) String tipo) {
        List<LugaresDTO> lugares = lugaresService.consultarLugaresLibres(tipo);
        return ResponseEntity.ok(lugares);
    }

    // Obtener estadísticas de lugares
    @GetMapping("/estadisticas")
    public ResponseEntity<LugaresEstadisticasDTO> obtenerEstadisticas(
            @RequestParam(required = false) String tipo) {
        LugaresEstadisticasDTO estadisticas = lugaresService.obtenerEstadisticas(tipo);
        return ResponseEntity.ok(estadisticas);
    }
}
