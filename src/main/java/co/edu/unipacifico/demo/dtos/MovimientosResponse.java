package co.edu.unipacifico.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MovimientosResponse {
    private Long id;
    private String placa;
    private Long usuarioId;
    private String nombreLugar;
    private String tipo;
    private String fechaEntrada;
    private String fechaSalida;
}
