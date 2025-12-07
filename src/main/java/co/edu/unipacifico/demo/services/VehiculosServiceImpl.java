package co.edu.unipacifico.demo.services;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import co.edu.unipacifico.demo.mappers.VehiculosMapper;
import co.edu.unipacifico.demo.models.Vehiculos;
import co.edu.unipacifico.demo.repositories.VehiculosRepository;
import co.edu.unipacifico.demo.repositories.VehiculosSpecifications;
import lombok.RequiredArgsConstructor;
import co.edu.unipacifico.demo.dtos.VehiculosDTO;
import co.edu.unipacifico.demo.exceptions.DatabaseException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VehiculosServiceImpl implements VehiculosService {

    // Ver inmutabilidad
    private final VehiculosMapper VehiculosMapper;
    private final VehiculosRepository VehiculosRepository;
    
    
    public VehiculosDTO crearVehiculos(VehiculosDTO vehiculoDTO){
        Vehiculos vehiculo = VehiculosMapper.toEntity(vehiculoDTO);
        Vehiculos vehiculoGuardado = VehiculosRepository.save(vehiculo);
        return VehiculosMapper.toDTO(vehiculoGuardado);
    }
    
    public void eliminarVehiculos(Long id){
        if (VehiculosRepository.existsById(id)) {
           VehiculosRepository.deleteById(id);
        } else {
            throw new RuntimeException("Vehiculo no encontrado con id: " + id); 
        }
        
    }

    public VehiculosDTO actualizarVehiculos(Long id, VehiculosDTO vehiculoDTO){
        Optional<Vehiculos> vehiculoExistente = VehiculosRepository.findById(id);
        if (!vehiculoExistente.isPresent()) {
            Vehiculos vehiculo = vehiculoExistente.get();
            vehiculo.setPlaca(vehiculoDTO.getPlaca());            
            vehiculo.setTipo(vehiculoDTO.getTipo());
            Vehiculos vehiculoActualizado = VehiculosRepository.save(vehiculo);
            return VehiculosMapper.toDTO(vehiculoActualizado);
        } else
            throw new RuntimeException("Vehiculo no encontrado con id: " + id);
            }

    @Override
    public Optional<List<VehiculosDTO>> consultarVehiculos(String tipo) {
        List<VehiculosDTO> vehiculos;
    
    try {
          // 1. Obtener datos del repositorio
          vehiculos = VehiculosRepository.findAll(
            VehiculosSpecifications.buscarVehiculos(tipo)).stream()
              .map(VehiculosMapper::toDTO)
              .collect(Collectors.toList());
            
      } catch (Exception e) {
          // 2. Captura cualquier excepción de persistencia (conexión, timeout, etc.)
          // y relanza como tu excepción personalizada.
          throw new DatabaseException(
            "Error al consultar vehículos en la base de datos.", e);
      }
    
      if (vehiculos.isEmpty()) {
          return Optional.empty();
      }
    
        return Optional.of(vehiculos);
    }

}
