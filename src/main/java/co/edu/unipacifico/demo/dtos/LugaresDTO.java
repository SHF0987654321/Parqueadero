package co.edu.unipacifico.demo.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LugaresDTO {
    
    private Long id;
    
    @NotEmpty(message = "El nombre no puede estar vacío.")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
    private String nombre;
    
    @NotEmpty(message = "El tipo no puede estar vacío.")
    @Size(min = 3, max = 50, message = "El tipo debe tener entre 3 y 50 caracteres.")
    private String tipo;
}
