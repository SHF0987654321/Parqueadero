package co.edu.unipacifico.demo.mappers;

import org.mapstruct.Mapper;

import co.edu.unipacifico.demo.dtos.VehiculosDTO;
import co.edu.unipacifico.demo.models.Vehiculos;

@Mapper(componentModel = "spring")
public interface VehiculosMapper {
    VehiculosDTO toDTO(Vehiculos vehiculo);
    Vehiculos toEntity(VehiculosDTO vehiculoDTO);
}
