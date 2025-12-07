package co.edu.unipacifico.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lugares")
@Getter
@Setter
@ToString(exclude = "movimientos")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Lugares {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @OneToMany(mappedBy = "lugar", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Movimientos> movimientos = new HashSet<>();

    public Lugares(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.movimientos = new HashSet<>();
    }
    
    // Métodos de utilidad para manejar la relación bidireccional
    public void addMovimiento(Movimientos movimiento) {
        this.movimientos.add(movimiento);
        movimiento.setLugar(this);
    }
    
    public void removeMovimiento(Movimientos movimiento) {
        this.movimientos.remove(movimiento);
        movimiento.setLugar(null);
    }
}
