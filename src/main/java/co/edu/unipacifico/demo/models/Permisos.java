package co.edu.unipacifico.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permisos")
@Getter
@Setter
@ToString(exclude = "roles")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Permisos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(name = "nombre", nullable = false, length = 50, unique = true)
    private String nombre;

    @ManyToMany(mappedBy = "permisos")
    private Set<Roles> roles = new HashSet<>();

    public Permisos(String nombre) {
        this.nombre = nombre;
        this.roles = new HashSet<>();
    }
}