package co.edu.unipacifico.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LugaresEstadisticasDTO {
    
    private Long totalLugares;
    private Long lugaresOcupados;
    private Long lugaresLibres;
    private String tipo; // null si es para todos los tipos
    
    public Double getPorcentajeOcupacion() {
        if (totalLugares == 0) return 0.0;
        return (lugaresOcupados * 100.0) / totalLugares;
    }
}
