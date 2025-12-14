package co.edu.unipacifico.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LugaresRequest {
    private String nombre;
    private String tipo;
    private String estado;
}
