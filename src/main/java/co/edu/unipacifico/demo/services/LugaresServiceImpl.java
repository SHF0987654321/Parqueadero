package co.edu.unipacifico.demo.services;

import co.edu.unipacifico.demo.dtos.LugaresDTO;
import co.edu.unipacifico.demo.dtos.LugaresEstadisticasDTO;
import co.edu.unipacifico.demo.exceptions.DatabaseException;
import co.edu.unipacifico.demo.mappers.LugaresMapper;
import co.edu.unipacifico.demo.models.Lugares;
import co.edu.unipacifico.demo.repositories.LugaresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LugaresServiceImpl implements LugaresService {

    private final LugaresRepository lugaresRepository;
    private final LugaresMapper lugaresMapper;

    // --- Lógica de Estado para DTOs (Método Auxiliar) ---
    // Este método toma una lista de Lugares y les asigna el estado antes de mapearlos a DTO
    private List<LugaresDTO> mapAndSetState(List<Lugares> lugares) {
        // 1. Obtener los IDs de todos los lugares actualmente ocupados
        // Se llama a findLugaresOcupados una sola vez para eficiencia
        List<Long> lugaresOcupadosIds = lugaresRepository.findLugaresOcupados().stream()
                .map(Lugares::getId)
                .collect(Collectors.toList());

        // 2. Mapear y asignar el estado basado en la lista de IDs ocupados
        return lugares.stream()
                .map(lugar -> {
                    LugaresDTO dto = lugaresMapper.toDTO(lugar);
                    if (lugaresOcupadosIds.contains(lugar.getId())) {
                        dto.setEstado("OCUPADO");
                    } else {
                        dto.setEstado("LIBRE");
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
    // ----------------------------------------------------

    @Override
    public LugaresDTO crearLugar(LugaresDTO lugarDTO) {
        // ... (Tu código existente)
        try {
            Lugares lugar = lugaresMapper.toEntity(lugarDTO);
            Lugares lugarGuardado = lugaresRepository.save(lugar);
            return lugaresMapper.toDTO(lugarGuardado);
        } catch (Exception e) {
            throw new DatabaseException("Error al crear el lugar en la base de datos.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LugaresDTO> consultarLugarPorId(Long id) {
        // ... (Tu código existente)
        try {
            return lugaresRepository.findById(id)
                    .map(lugaresMapper::toDTO);
        } catch (Exception e) {
            throw new DatabaseException("Error al consultar el lugar por ID.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LugaresDTO> consultarTodosLosLugares() {
        try {
            List<Lugares> todosLosLugares = lugaresRepository.findAll();
            // Llama al método auxiliar para asignar estado
            return mapAndSetState(todosLosLugares);
        } catch (Exception e) {
            throw new DatabaseException("Error al consultar todos los lugares.", e);
        }
    }

    // ... (Métodos actualizarLugar y eliminarLugar sin cambios ya que no devuelven listas) ...

    @Override
    @Transactional(readOnly = true)
    public LugaresDTO actualizarLugar(Long id, LugaresDTO lugarDTO) {
        try {
            Lugares lugarExistente = lugaresRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Lugar no encontrado con id: " + id));
            
            lugarExistente.setNombre(lugarDTO.getNombre());
            lugarExistente.setTipo(lugarDTO.getTipo());
            
            Lugares lugarActualizado = lugaresRepository.save(lugarExistente);
            return lugaresMapper.toDTO(lugarActualizado);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error al actualizar el lugar.", e);
        }
    }

    @Override
    public void eliminarLugar(Long id) {
        try {
            if (!lugaresRepository.existsById(id)) {
                throw new RuntimeException("Lugar no encontrado con id: " + id);
            }
            lugaresRepository.deleteById(id);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error al eliminar el lugar.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LugaresDTO> consultarLugaresPorTipo(String tipo) {
        try {
            List<Lugares> lugares = lugaresRepository.findByTipo(tipo);
            // Llama al método auxiliar para asignar estado
            return mapAndSetState(lugares);
        } catch (Exception e) {
            throw new DatabaseException("Error al consultar lugares por tipo.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LugaresDTO> consultarLugaresOcupados(String tipo) {
        try {
            List<Lugares> lugares;
            if (tipo != null && !tipo.isEmpty()) {
                lugares = lugaresRepository.findLugaresOcupadosPorTipo(tipo);
            } else {
                lugares = lugaresRepository.findLugaresOcupados();
            }
            // Los lugares ocupados ya vienen filtrados por el repositorio, pero mapAndSetState 
            // asegura que el campo 'estado' se establezca en 'OCUPADO' (aunque debería ser redundante, 
            // es buena práctica garantizar la consistencia del DTO).
            return mapAndSetState(lugares); 
        } catch (Exception e) {
            throw new DatabaseException("Error al consultar lugares ocupados.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LugaresDTO> consultarLugaresLibres(String tipo) {
        try {
            List<Lugares> lugares;
            if (tipo != null && !tipo.isEmpty()) {
                lugares = lugaresRepository.findLugaresLibresPorTipo(tipo);
            } else {
                lugares = lugaresRepository.findLugaresLibres();
            }
            // Los lugares libres ya vienen filtrados por el repositorio, mapAndSetState 
            // asegura que el campo 'estado' se establezca en 'LIBRE'.
            return mapAndSetState(lugares);
        } catch (Exception e) {
            throw new DatabaseException("Error al consultar lugares libres.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public LugaresEstadisticasDTO obtenerEstadisticas(String tipo) { 
        try {
            Long totalLugares = lugaresRepository.count();
            Long lugaresOcupados = lugaresRepository.contarLugaresOcupados();
            Long lugaresLibres = totalLugares - lugaresOcupados;
        
            final List<String> TIPOS_VALIDOS = List.of("CARRO", "MOTO", "BUS");
            
            // 1. Declarar e inicializar el mapa
            Map<String, LugaresEstadisticasDTO.EstadisticasPorTipo> porTipoMap = new HashMap<>();

            // 2. Bucle para calcular las estadísticas por tipo
            for (String tipoActual : TIPOS_VALIDOS) {
                Long totalTipo = (long) lugaresRepository.findByTipo(tipoActual).size();
                Long ocupadosTipo = lugaresRepository.contarLugaresOcupadosPorTipo(tipoActual);
                Long libresTipo = totalTipo - ocupadosTipo;
            
                LugaresEstadisticasDTO.EstadisticasPorTipo statsTipo = 
                    new LugaresEstadisticasDTO.EstadisticasPorTipo(libresTipo, ocupadosTipo);
                
                porTipoMap.put(tipoActual, statsTipo);
            }
        
            // 3. Crear y devolver el DTO final
            LugaresEstadisticasDTO resultado = new LugaresEstadisticasDTO(
                totalLugares, 
                lugaresOcupados, 
                lugaresLibres, 
                null, // Campo tipo, ahora siempre null para estadísticas generales
                porTipoMap
            );
        
            return resultado;

        } catch (Exception e) {
            throw new DatabaseException("Error al obtener estadísticas de lugares.", e);
        }
    }
}
