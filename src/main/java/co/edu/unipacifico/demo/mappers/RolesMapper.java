package co.edu.unipacifico.demo.mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import co.edu.unipacifico.demo.dtos.RolesDTO;
import co.edu.unipacifico.demo.models.Permisos;
import co.edu.unipacifico.demo.models.Roles;

@Mapper(componentModel = "spring")
public interface RolesMapper {

    @Mapping(target = "permisos", source = "permisos", qualifiedByName = "mapPermisosToIds")
    RolesDTO toDTO(Roles entity);

    @Mapping(target = "permisos", source = "permisos", qualifiedByName = "mapIdsToPermisos")
    Roles toEntity(RolesDTO dto);

    // ---------- Helpers ----------- //

    @Named("mapPermisosToIds")
    public static List<Long> mapPermisosToIds(Set<Permisos> permisos) {
        if (permisos == null) return null;
        return permisos.stream()
                       .map(Permisos::getId)
                       .toList();
    }

    @Named("mapIdsToPermisos")
    public static Set<Permisos> mapIdsToPermisos(List<Long> ids) {
        if (ids == null) return null;
        return ids.stream()
                  .map(id -> {
                      Permisos p = new Permisos();
                      p.setId(id);
                      return p;
                  })
                  .collect(Collectors.toSet());
    }
}
