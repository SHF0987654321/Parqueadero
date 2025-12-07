package co.edu.unipacifico.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vehiculos")
@Getter
@Setter
@ToString(exclude = "movimientos")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Vehiculos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(name = "placa", nullable = false, length = 10, unique = true)
    private String placa;
    
    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Movimientos> movimientos = new HashSet<>();

    public Vehiculos(String placa, String tipo) {
        this.placa = placa;
        this.tipo = tipo;
        this.movimientos = new HashSet<>();
    }
    
    // Métodos de utilidad para manejar la relación bidireccional
    public void addMovimiento(Movimientos movimiento) {
        this.movimientos.add(movimiento);
        movimiento.setVehiculo(this);
    }
    
    public void removeMovimiento(Movimientos movimiento) {
        this.movimientos.remove(movimiento);
        movimiento.setVehiculo(null);
    }
}