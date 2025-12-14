package co.edu.unipacifico.demo.controllers;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipacifico.demo.dtos.VehiculosRequest;
import co.edu.unipacifico.demo.dtos.VehiculosResponse;
import co.edu.unipacifico.demo.services.VehiculosService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vehiculos")
public class VehiculosController {

    private final VehiculosService vehiculosService;
    
    @GetMapping
    public ResponseEntity<List<VehiculosResponse>> listarVehiculos(
            @RequestParam(required = false) String tipo) {
        
        return ResponseEntity.ok(
            vehiculosService.consultarVehiculos(tipo));
    }
    
    @PostMapping
    public ResponseEntity<VehiculosResponse> crearVehiculos(
            @RequestBody VehiculosRequest vehiculos) {

        return ResponseEntity
                .status(201)
                .body(vehiculosService.crearVehiculos(vehiculos));
    }
    @PutMapping("/{id}")
    public VehiculosResponse actualizarVehiculos(@PathVariable Long id, 
    @RequestBody VehiculosRequest vehiculos) {      
        return vehiculosService.actualizarVehiculos(id, vehiculos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVehiculos(@PathVariable Long id) {
        vehiculosService.eliminarVehiculos(id);
        return ResponseEntity.noContent().build();
    }


}
