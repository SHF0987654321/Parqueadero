package co.edu.unipacifico.demo.services;

import java.util.List;
import java.util.Optional;

import co.edu.unipacifico.demo.dtos.VehiculosDTO;

public interface VehiculosService {
    Optional<List<VehiculosDTO>> consultarVehiculos(String tipo); 
    VehiculosDTO crearVehiculos(VehiculosDTO vehiculoDTO);
    void eliminarVehiculos(Long id);
    VehiculosDTO actualizarVehiculos(Long id, VehiculosDTO vehiculoDTO);

}
