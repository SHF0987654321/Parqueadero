package co.edu.unipacifico.demo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.unipacifico.demo.dtos.RolesDTO;
import co.edu.unipacifico.demo.models.Roles;

@Mapper(componentModel = "spring", 
        uses = {MappingUtils.class}
)
public interface RolesMapper {

    @Mapping(target = "permisos", source = "permisos", qualifiedByName = "permisosToIds")
    RolesDTO toDTO(Roles entity);

    @Mapping(target = "permisos", source = "permisos", qualifiedByName = "idsToPermisos")
    @Mapping(target = "usuarios", ignore = true)  // opcional, evita recursión
    Roles toEntity(RolesDTO dto);
}
