package co.edu.unipacifico.demo.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "vehiculos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "placa", nullable = false, length = 10)
    private String placa;
    @Column(name = "modelo", nullable = false, length = 50)
    private String modelo;
    @Column(name = "marca", nullable = false, length = 50)
    private String marca;
    @Column(name = "color", nullable = false, length = 30)
    private String color;
    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo;
}
