package co.edu.unipacifico.demo.mappers;

import org.mapstruct.Mapper;

import co.edu.unipacifico.demo.dtos.VehiculosRequest;
import co.edu.unipacifico.demo.dtos.VehiculosResponse;
import co.edu.unipacifico.demo.models.Vehiculos;

@Mapper(componentModel = "spring")
public interface VehiculosMapper {
    VehiculosResponse toDTO(Vehiculos vehiculo);
    Vehiculos toEntity(VehiculosRequest vehiculoDTO);
}
