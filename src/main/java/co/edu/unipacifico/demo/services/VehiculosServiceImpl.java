package co.edu.unipacifico.demo.services;

import org.springframework.stereotype.Service;

import co.edu.unipacifico.demo.mappers.VehiculosMapper;
import co.edu.unipacifico.demo.models.Vehiculos;
import co.edu.unipacifico.demo.repositories.VehiculosRepository;
import co.edu.unipacifico.demo.repositories.VehiculosSpecifications;
import lombok.RequiredArgsConstructor;
import co.edu.unipacifico.demo.dtos.VehiculosRequest;
import co.edu.unipacifico.demo.dtos.VehiculosResponse;
import co.edu.unipacifico.demo.exceptions.DatabaseException;
import co.edu.unipacifico.demo.exceptions.InvalidOperationExeception;
import co.edu.unipacifico.demo.exceptions.ResourceNotFoundException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VehiculosServiceImpl implements VehiculosService {

    // Ver inmutabilidad
    private final VehiculosMapper vehiculosMapper;
    private final VehiculosRepository vehiculosRepository;
    
    
    public VehiculosResponse crearVehiculos(VehiculosRequest vehiculo){

        if (vehiculosRepository.findByPlaca(vehiculo.getPlaca()).isPresent()) {
            throw new InvalidOperationExeception(
                "Ya existe un vehículo con la placa: " + vehiculo.getPlaca()
            );
        }
        Vehiculos registrarVehiculo = vehiculosMapper.toEntity(vehiculo);
        Vehiculos vehiculoGuardado = vehiculosRepository.save(registrarVehiculo);
        return vehiculosMapper.toDTO(vehiculoGuardado);
    }
    
    public void eliminarVehiculos(Long id){
        if (vehiculosRepository.existsById(id)) {
           vehiculosRepository.deleteById(id);
        } else {
            throw new InvalidOperationExeception("Vehiculo no encontrado con id: " + id); 
        }
        
    }

    public VehiculosResponse actualizarVehiculos(Long id, VehiculosRequest vehiculoDTO) {
        Vehiculos vehiculo = vehiculosRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Vehiculo no encontrado con id: " + id));

        vehiculo.setPlaca(vehiculoDTO.getPlaca());
        vehiculo.setTipo(vehiculoDTO.getTipo());

        Vehiculos vehiculoActualizado = vehiculosRepository.save(vehiculo);
        return vehiculosMapper.toDTO(vehiculoActualizado);
    }


    @Override
    public List<VehiculosResponse> consultarVehiculos(String tipo) {
        List<VehiculosResponse> vehiculos;
    
    try {
        return vehiculosRepository
            .findAll(VehiculosSpecifications.buscarVehiculos(tipo))
            .stream()
            .map(vehiculosMapper::toDTO)
            .toList();
            
      } catch (Exception e) {
          throw new DatabaseException(
            "Error al consultar vehículos en la base de datos.", e);
      }

    }

}
