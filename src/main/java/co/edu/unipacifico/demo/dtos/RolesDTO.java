package co.edu.unipacifico.demo.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RolesDTO {

    private Long id;
    private String nombre;
    private List<Long> permisos;

}
