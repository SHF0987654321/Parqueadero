package co.edu.unipacifico.demo.services;

import co.edu.unipacifico.demo.dtos.LugaresRequest;
import co.edu.unipacifico.demo.dtos.LugaresResponse;
import co.edu.unipacifico.demo.dtos.LugaresEstadisticas;

import java.util.List;

public interface LugaresService {
    
    // CRUD básico
    LugaresResponse crearLugar(LugaresRequest lugar);
    LugaresResponse consultarLugarPorId(Long id);
    List<LugaresResponse> consultarTodosLosLugares();
    LugaresResponse actualizarLugar(Long id, LugaresRequest lugar);
    void eliminarLugar(Long id);
    
    // Consultas por tipo
    List<LugaresResponse> consultarLugaresPorTipo(String tipo);
    
    // Consultas de disponibilidad
    List<LugaresResponse> consultarLugaresOcupados(String tipo);
    List<LugaresResponse> consultarLugaresLibres(String tipo);
    
    // Estadísticas
    LugaresEstadisticas obtenerEstadisticas(String tipo);
}
