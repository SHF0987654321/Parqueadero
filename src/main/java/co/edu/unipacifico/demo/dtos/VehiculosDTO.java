package co.edu.unipacifico.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VehiculosDTO {
    private Long id;
    private String placa;
    private String modelo;
    private String marca;
    private String color;
    private String tipo;

}
