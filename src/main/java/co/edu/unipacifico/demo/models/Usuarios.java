package co.edu.unipacifico.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@ToString(exclude = {"clave", "roles"})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuarios {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "clave", nullable = false, length = 100)
    private String clave;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "usuarios_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Roles> roles = new HashSet<>();

    public Usuarios(String nombre, String clave) {
        this.nombre = nombre;
        this.clave = clave;
        this.roles = new HashSet<>();
    }

    public Usuarios(String nombre, String clave, Set<Roles> roles) {
        this.nombre = nombre;
        this.clave = clave;
        this.roles = roles != null ? roles : new HashSet<>();
    }
    
    // Métodos de utilidad para manejar la relación bidireccional
    public void addRol(Roles rol) {
        this.roles.add(rol);
        rol.getUsuarios().add(this);
    }
    
    public void removeRol(Roles rol) {
        this.roles.remove(rol);
        rol.getUsuarios().remove(this);
    }
}
