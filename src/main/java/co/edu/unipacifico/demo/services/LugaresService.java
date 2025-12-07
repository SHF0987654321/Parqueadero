package co.edu.unipacifico.demo.services;

import co.edu.unipacifico.demo.dtos.LugaresDTO;
import co.edu.unipacifico.demo.dtos.LugaresEstadisticasDTO;

import java.util.List;
import java.util.Optional;

public interface LugaresService {
    
    // CRUD básico
    LugaresDTO crearLugar(LugaresDTO lugarDTO);
    Optional<LugaresDTO> consultarLugarPorId(Long id);
    List<LugaresDTO> consultarTodosLosLugares();
    LugaresDTO actualizarLugar(Long id, LugaresDTO lugarDTO);
    void eliminarLugar(Long id);
    
    // Consultas por tipo
    List<LugaresDTO> consultarLugaresPorTipo(String tipo);
    
    // Consultas de disponibilidad
    List<LugaresDTO> consultarLugaresOcupados(String tipo);
    List<LugaresDTO> consultarLugaresLibres(String tipo);
    
    // Estadísticas
    LugaresEstadisticasDTO obtenerEstadisticas(String tipo);
}
