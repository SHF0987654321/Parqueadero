package co.edu.unipacifico.demo.controllers;


import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
public abstract class VehiculosRestController {

    private final VehiculosService vehiculosService;
    
    @GetMapping
    public List<VehiculosDTO> ListarVehiculos() {
        return vehiculosService.consultarVehiculos();
    }

    @PostMapping
    public VehiculosDTO crearVehiculosDTO(@RequestBody VehiculosDTO Vehiculos) {
        return vehiculosService.crearVehiculos(Vehiculos);
    }
    
    @PutMapping("/{id}")
    public VehiculosDTO actualizarVehiculos(@PathVariable Long id, @RequestBody VehiculosDTO vehiculosDTO) {      
        return vehiculosService.actualizarVehiculos(id, vehiculosDTO);
    }

    @DeleteMapping("/{id}")
    public void eliminarVehiculos(@PathVariable Long id) {
        vehiculosService.eliminarVehiculos(id);
    }


}
