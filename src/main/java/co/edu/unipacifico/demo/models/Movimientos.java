package co.edu.unipacifico.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Movimientos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculos vehiculo;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuarios usuario;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lugar_id", nullable = false)
    private Lugares lugar;
    
    @Column(name = "fecha_entrada", nullable = false)
    private LocalDateTime fechaEntrada;
    
    @Column(name = "fecha_salida")
    private LocalDateTime fechaSalida;

    public Movimientos(Vehiculos vehiculo, Usuarios usuario, Lugares lugar, LocalDateTime fechaEntrada) {
        this.vehiculo = vehiculo;
        this.usuario = usuario;
        this.lugar = lugar;
        this.fechaEntrada = fechaEntrada;
    }
    
    public Movimientos(Vehiculos vehiculo, Usuarios usuario, Lugares lugar, LocalDateTime fechaEntrada, LocalDateTime fechaSalida) {
        this.vehiculo = vehiculo;
        this.usuario = usuario;
        this.lugar = lugar;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
    }
}
