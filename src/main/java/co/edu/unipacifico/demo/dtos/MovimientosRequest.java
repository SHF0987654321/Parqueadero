package co.edu.unipacifico.demo.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MovimientosRequest {
    
    @NotNull(message = "La placa del vehículo no puede estar vacía.")
    private String placa;  
    @NotNull(message = "El ID del usuario no puede estar vacío.")
    private Long usuarioId;
    private String nombreLugar;
    private String tipo;
    
}
