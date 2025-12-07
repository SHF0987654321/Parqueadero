package co.edu.unipacifico.demo.mappers;

import org.mapstruct.Mapper;

import co.edu.unipacifico.demo.dtos.PermisosDTO;
import co.edu.unipacifico.demo.models.Permisos;

@Mapper(componentModel = "spring")
public interface PermisosMapper {

    PermisosDTO toDTO(Permisos permisos);

    Permisos toEntity(PermisosDTO permisosDTO);
}
