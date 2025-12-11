package co.edu.unipacifico.demo.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuariosResponse {
    private String nombre; 
    private String email;
    private String password;
    private String rol;
    private List<Long> roles;
}
