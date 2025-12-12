package co.edu.unipacifico.demo.dtos;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LugaresEstadisticasDTO {
    
    private Long total;
    private Long ocupados;
    private Long libres;
    private String tipo; // null si es para todos los tipos
    // NUEVO CAMPO: Mapa de estadísticas por tipo
    private Map<String, EstadisticasPorTipo> porTipo;

    // NUEVA CLASE ANIDADA (o DTO separado si prefieres)
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class EstadisticasPorTipo {
        private Long libres;
        private Long ocupados;
    }
    public Double getPorcentajeOcupacion() {
        if (total == 0) return 0.0;
        return (ocupados * 100.0) / total;
    }
}
