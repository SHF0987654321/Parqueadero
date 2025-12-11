package co.edu.unipacifico.demo.mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import co.edu.unipacifico.demo.models.Permisos;
import co.edu.unipacifico.demo.models.Roles;
@Component
public class MappingUtils {

    /**
    * Calcula el nombre del rol principal (String) para el DTO.
     * Esta función es llamada desde el Mapper con @Mapping(qualifiedByName).
     */
    @Named("mapPrincipalRolName")
    public String mapPrincipalRolName(Set<Roles> roles) {
        
        if (roles == null || roles.isEmpty()) {
            return "USUARIO"; // Rol de fallback si no tiene roles
        }
        
        // Lógica: Tomar el nombre del primer rol encontrado
        // (Podrías añadir lógica de prioridad aquí si un usuario tiene ADMIN y USER)
        return roles.stream()
            .map(Roles::getNombre)
            .findFirst() // Elegir el primer rol
            .orElse("USUARIO"); 
    }

    @Named("rolesToIds")
    public static List<Long> rolesToIds(Set<Roles> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(Roles::getId)
                .collect(Collectors.toList());
    }

    @Named("idsToRoles")
    public static Set<Roles> idsToRoles(List<Long> ids) {
        if (ids == null) return null;
        return ids.stream()
                .map(id -> {
                    Roles r = new Roles();
                    r.setId(id);
                    return r;
                })
                .collect(Collectors.toSet());
    }
    
    @Named("permisosToIds")
    public static List<Long> permisosToIds(Set<Permisos> permisos) {
        if (permisos == null) return null;
        return permisos.stream()
                .map(Permisos::getId)
                .toList();
    }

    @Named("idsToPermisos")
    public static Set<Permisos> idsToPermisos(List<Long> ids) {
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
