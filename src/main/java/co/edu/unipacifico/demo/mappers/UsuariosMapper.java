package co.edu.unipacifico.demo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.unipacifico.demo.dtos.RegisterRequest;
import co.edu.unipacifico.demo.dtos.UsuariosResponse;
import co.edu.unipacifico.demo.models.Usuarios;

@Mapper(componentModel = "spring", 
        // ¡IMPORTANTE! Se debe referenciar la clase MappingUtils
        uses = {MappingUtils.class} 
)
public interface UsuariosMapper {

    @Mapping(target = "rol", source = "roles", qualifiedByName = "mapPrincipalRolName")
    UsuariosResponse toDTO(Usuarios usuarios);

    @Mapping(target = "roles", ignore = true) // Se manejan en el service
    Usuarios toEntity(UsuariosResponse usuariosResponse);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Usuarios toEntity(RegisterRequest registerRequest); 
}