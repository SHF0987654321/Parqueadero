package co.edu.unipacifico.demo.services;

import java.util.List;
import java.util.Optional;

import co.edu.unipacifico.demo.dtos.VehiculosRequest;
import co.edu.unipacifico.demo.dtos.VehiculosResponse;

public interface VehiculosService {
    List<VehiculosResponse> consultarVehiculos(String tipo); 
    VehiculosResponse crearVehiculos(VehiculosRequest vehiculo);
    void eliminarVehiculos(Long id);
    VehiculosResponse actualizarVehiculos(Long id, VehiculosRequest vehiculo);

}
