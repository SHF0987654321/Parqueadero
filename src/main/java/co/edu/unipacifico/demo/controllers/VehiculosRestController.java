package co.edu.unipacifico.demo.controllers;


import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipacifico.demo.dtos.VehiculosDTO;
import co.edu.unipacifico.demo.services.VehiculosService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vehiculos")
public class VehiculosRestController {

    private final VehiculosService vehiculosService;
    
    @GetMapping
    public ResponseEntity<List<VehiculosDTO>> ListarVehiculos(
        @RequestParam(required = false) String tipo ) {
            // El servicio devuelve Optional<List<VehiculosDTO>>
        Optional<List<VehiculosDTO>> optionalVehiculos = vehiculosService.consultarVehiculos(tipo);

        // 1. Usar orElse() para desempaquetar el Optional.
        //    Si el Optional está vacío, devuelve una List.of() (lista inmutable vacía).
        List<VehiculosDTO> vehiculos = optionalVehiculos.orElse(List.of());

        if (vehiculos.isEmpty()) {
            // 2. Si la lista está vacía (tanto si el Optional lo estaba o si la lista estaba vacía),
            //    devuelve 204 No Content.
            return ResponseEntity.noContent().build();
    }
    
        // 3. Devuelve 200 OK con el contenido.
        return ResponseEntity.ok(vehiculos);
    }

    @PostMapping
    public VehiculosDTO crearVehiculosDTO(@RequestBody VehiculosDTO Vehiculos) {
        return vehiculosService.crearVehiculos(Vehiculos);
    }
    
    @PutMapping("/{id}")
    public VehiculosDTO actualizarVehiculos(@PathVariable Long id, 
    @RequestBody VehiculosDTO vehiculosDTO) {      
        return vehiculosService.actualizarVehiculos(id, vehiculosDTO);
    }

    @DeleteMapping("/{id}")
    public void eliminarVehiculos(@PathVariable Long id) {
        vehiculosService.eliminarVehiculos(id);
    }


}
