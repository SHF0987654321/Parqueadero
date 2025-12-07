package co.edu.unipacifico.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@ToString(exclude = {"usuarios", "permisos"})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(name = "nombre", nullable = false, length = 50, unique = true)
    private String nombre;

    @ManyToMany(mappedBy = "roles")
    private Set<Usuarios> usuarios = new HashSet<>();
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "roles_permisos",
        joinColumns = @JoinColumn(name = "rol_id"),
        inverseJoinColumns = @JoinColumn(name = "permiso_id")
    )
    private Set<Permisos> permisos = new HashSet<>();
    
    public Roles(String nombre) {
        this.nombre = nombre;
        this.usuarios = new HashSet<>();
        this.permisos = new HashSet<>();
    }
    
    public Roles(String nombre, Set<Permisos> permisos) {
        this.nombre = nombre;
        this.usuarios = new HashSet<>();
        this.permisos = permisos != null ? permisos : new HashSet<>();
    }
    
    // Métodos de utilidad para manejar la relación bidireccional
    public void addPermiso(Permisos permiso) {
        this.permisos.add(permiso);
        permiso.getRoles().add(this);
    }
    
    public void removePermiso(Permisos permiso) {
        this.permisos.remove(permiso);
        permiso.getRoles().remove(this);
    }
}
