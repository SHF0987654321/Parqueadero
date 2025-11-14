package co.edu.unipacifico.demo.services;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unipacifico.demo.mappers.VehiculosMapper;
import co.edu.unipacifico.demo.models.Vehiculos;
import co.edu.unipacifico.demo.repositories.VehiculosRepository;
import co.edu.unipacifico.demo.dtos.VehiculosDTO;
import java.util.List;
import java.util.Optional;

@Service
public class VehiculosServiceImpl implements VehiculosService {

    @Autowired
    private VehiculosRepository VehiculosRepository;
    public List<VehiculosDTO> consultarVehiculos(){
        return VehiculosRepository.findAll().stream()
                .map(VehiculosMapper::toDTO)
                .collect(Collectors.toList());
    } 
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
            vehiculo.setModelo(vehiculoDTO.getModelo());
            vehiculo.setMarca(vehiculoDTO.getMarca());
            vehiculo.setColor(vehiculoDTO.getColor());
            vehiculo.setTipo(vehiculoDTO.getTipo());
            Vehiculos vehiculoActualizado = VehiculosRepository.save(vehiculo);
            return VehiculosMapper.toDTO(vehiculoActualizado);
        } else
            throw new RuntimeException("Vehiculo no encontrado con id: " + id);
            }

}
