package co.edu.unipacifico.demo.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {

    @Email(message = "El correo debe tener un formato válido.")
    @NotEmpty(message = "El correo no puede estar vacío.") 
    private String email;
    
    @NotEmpty(message = "La clave no puede estar vacía.")
    private String password;

}
