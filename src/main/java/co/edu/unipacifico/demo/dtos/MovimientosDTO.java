package co.edu.unipacifico.demo.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MovimientosDTO {
    
    private Long id;
    
    @NotNull(message = "El ID del vehículo no puede estar vacío.")
    private Long vehiculoId;
    
    @NotNull(message = "El ID del usuario no puede estar vacío.")
    private Long usuarioId;
    
    @NotNull(message = "El ID del lugar no puede estar vacío.")
    private Long lugarId;
    
    @NotNull(message = "La fecha de entrada no puede estar vacía.")
    private LocalDateTime fechaEntrada;
    
    private LocalDateTime fechaSalida;
}
