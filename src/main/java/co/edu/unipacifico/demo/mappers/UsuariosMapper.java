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

    // ---- Entity (Usuarios) -> DTO (UsuariosResponse) ----
    
    // 1. Mapea Set<Roles> a List<Long> (si tienes un método rolesToIds en MappingUtils)
    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToIds")
    
    // 2. ¡NUEVA LÍNEA CLAVE! Mapea Set<Roles> (source) al campo 'rol' (target)
    //    usando el método @Named("mapPrincipalRolName") definido en MappingUtils.
    @Mapping(target = "rol", source = "roles", qualifiedByName = "mapPrincipalRolName")
    UsuariosResponse toDTO(Usuarios usuarios);

    // ---- DTO (UsuariosResponse) -> Entity (Usuarios) ----
    // Roles se llenan en el service, no aquí, e 'rol' no es parte de la Entidad
    @Mapping(target = "roles", ignore = true)
    Usuarios toEntity(UsuariosResponse usuariosDTO);

    // ---- Registro (RegisterRequest) -> Entity (Usuarios) ----
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true) // Los roles se asignan en el servicio
    Usuarios toEntity(RegisterRequest registerRequest);  
}