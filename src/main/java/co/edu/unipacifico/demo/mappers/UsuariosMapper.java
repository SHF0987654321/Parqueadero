package co.edu.unipacifico.demo.mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.unipacifico.demo.dtos.UsuariosDTO;
import co.edu.unipacifico.demo.models.Roles;
import co.edu.unipacifico.demo.models.Usuarios;

@Mapper(componentModel = "spring")
public interface UsuariosMapper {

    UsuariosDTO toDTO(Usuarios usuarios);

    @Mapping(target = "roles", ignore = true) // roles se asignan en el service
    Usuarios toEntity(UsuariosDTO usuariosDTO);

    // ======================
    // MÉTODOS QUE PIDE MAPSTRUCT
    // ======================

    // Set<Roles> -> List<Long>
    default List<Long> map(Set<Roles> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(Roles::getId)
                .collect(Collectors.toList());
    }

    // List<Long> -> Set<Roles>
    // Esto NO se hace aquí, se hace en el service, así que lo dejamos en null.
    default Set<Roles> map(List<Long> ids) {
        return null;
    }
}