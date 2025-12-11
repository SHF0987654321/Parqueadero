package co.edu.unipacifico.demo.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterRequest {
    private Long id; 
    @NotEmpty(message = "El nombre no puede estar vacío.") 
    @Size(min = 4, max = 100, message = "El nombre debe tener entre 4 y 100 caracteres.")
    private String nombre;    
    @NotEmpty(message = "El correo no puede estar vacío.") 
    @Size(min = 5, max = 100, message = "El correo debe tener entre 5 y 100 caracteres.")
    @Email(message = "El correo debe tener un formato válido.")
    private String email;
    @NotEmpty(message = "La clave no puede estar vacía.")
    private String password;
}
