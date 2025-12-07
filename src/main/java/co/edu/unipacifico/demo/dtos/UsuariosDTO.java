package co.edu.unipacifico.demo.dtos;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuariosDTO {
    private Long id;
    @NotEmpty(message = "El nombre no puede estar vacío.") 
    @Size(min = 4, max = 100, message = "El nombre debe tener entre 4 y 100 caracteres.")
    private String nombre;
    @NotEmpty(message = "La clave no puede estar vacía.")
    private String clave;
    private List<Long> roles;
}
