package co.edu.unipacifico.demo.services;

import java.util.List;

import co.edu.unipacifico.demo.dtos.VehiculosDTO;


public interface VehiculosService {

 
    List<VehiculosDTO> consultarVehiculos(); 
    VehiculosDTO crearVehiculos(VehiculosDTO vehiculoDTO);
    void eliminarVehiculos(Long id);
    VehiculosDTO actualizarVehiculos(Long id, VehiculosDTO vehiculoDTO);

}
