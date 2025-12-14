package co.edu.unipacifico.demo.services;

import co.edu.unipacifico.demo.dtos.LugaresRequest;
import co.edu.unipacifico.demo.dtos.LugaresResponse;
import co.edu.unipacifico.demo.dtos.LugaresEstadisticas;
import co.edu.unipacifico.demo.exceptions.DatabaseException;
import co.edu.unipacifico.demo.exceptions.InvalidOperationExeception;
import co.edu.unipacifico.demo.exceptions.ResourceNotFoundException;
import co.edu.unipacifico.demo.mappers.LugaresMapper;
import co.edu.unipacifico.demo.models.Lugares;
import co.edu.unipacifico.demo.repositories.LugaresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LugaresServiceImpl implements LugaresService {

    private final LugaresRepository lugaresRepository;
    private final LugaresMapper lugaresMapper;

    @Override
    @Transactional
    public LugaresResponse crearLugar(LugaresRequest lugar) {
        try {
            // 1. Validar que no exista un lugar con el mismo nombre
            if (lugaresRepository.existsByNombre(lugar.getNombre())) {
                throw new InvalidOperationExeception(
                    "Ya existe un lugar con el nombre: " + lugar.getNombre()
                );
            }

            // 2. Mapear DTO → entidad
            Lugares nuevoLugar = lugaresMapper.toEntity(lugar);

            // 3. Guardar
            Lugares lugarCreado = lugaresRepository.save(nuevoLugar);

            // 4. Retornar DTO
            return lugaresMapper.toDTO(lugarCreado);

        } catch (RuntimeException e) {
            throw e; // reglas de negocio
        } catch (Exception e) {
            throw new DatabaseException(
                "Error al crear el lugar en la base de datos.", e
            );
        }
    }
    

    @Override
    @Transactional(readOnly = true)
    public LugaresResponse consultarLugarPorId(Long id) {
        try {
            Lugares lugar = lugaresRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Lugar no encontrado con ID: " + id));

            return lugaresMapper.toDTO(lugar);

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException(
                "Error al consultar el lugar por ID.", e);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<LugaresResponse> consultarTodosLosLugares() {
        try {
                return lugaresRepository.findAllWithEstado()
                        .stream()
                        .map(p -> new LugaresResponse(
                                p.getId(),
                                p.getNombre(),
                                p.getTipo(),
                                p.getEstado()
                        ))
                        .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseException("Error al consultar todos los lugares.", e);
        }
    }

    // ... (Métodos actualizarLugar y eliminarLugar sin cambios ya que no devuelven listas) ...

    @Override
    @Transactional(readOnly = true)
    public LugaresResponse actualizarLugar(Long id, LugaresRequest lugarDTO) {
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
    public List<LugaresResponse> consultarLugaresPorTipo(String tipo) {
        try {
                return lugaresRepository.findAllWithEstadoByTipo(tipo)
                        .stream()
                        .map(p -> new LugaresResponse(
                                p.getId(),
                                p.getNombre(),
                                p.getTipo(),
                                p.getEstado()
                        ))
                        .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseException("Error al consultar lugares por tipo.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LugaresResponse> consultarLugaresOcupados(String tipo) {
        try {
            return lugaresRepository.findAllWithEstadoByTipo(tipo)
                       .stream()
                      .filter(p -> "OCUPADO".equals(p.getEstado()))
                     .map(p -> new LugaresResponse(
                            p.getId(),
                            p.getNombre(),
                               p.getTipo(),
                               p.getEstado()
                     ))
                      .collect(Collectors.toList()); 
        } catch (Exception e) {
            throw new DatabaseException("Error al consultar lugares ocupados.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LugaresResponse> consultarLugaresLibres(String tipo) {
        try {
            return lugaresRepository.findAllWithEstadoByTipo(tipo)
                       .stream()
                      .filter(p -> "LIBRE".equals(p.getEstado()))
                     .map(p -> new LugaresResponse(
                            p.getId(),
                            p.getNombre(),
                               p.getTipo(),
                               p.getEstado()
                     ))
                      .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseException("Error al consultar lugares libres.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public LugaresEstadisticas obtenerEstadisticas(String tipo) { 
        try {
            Long totalLugares = lugaresRepository.count();
            Long lugaresOcupados = lugaresRepository.contarLugaresOcupados();
            Long lugaresLibres = totalLugares - lugaresOcupados;
        
            final List<String> TIPOS_VALIDOS = List.of("CARRO", "MOTO", "BUS");
            
            // 1. Declarar e inicializar el mapa
            Map<String, LugaresEstadisticas.EstadisticasPorTipo> porTipoMap = new HashMap<>();

            // 2. Bucle para calcular las estadísticas por tipo
            for (String tipoActual : TIPOS_VALIDOS) {
                Long totalTipo = (long) lugaresRepository.findByTipo(tipoActual).size();
                Long ocupadosTipo = lugaresRepository.contarLugaresOcupadosPorTipo(tipoActual);
                Long libresTipo = totalTipo - ocupadosTipo;
            
                LugaresEstadisticas.EstadisticasPorTipo statsTipo = 
                    new LugaresEstadisticas.EstadisticasPorTipo(libresTipo, ocupadosTipo);
                
                porTipoMap.put(tipoActual, statsTipo);
            }
        
            // 3. Crear y devolver el DTO final
            LugaresEstadisticas resultado = new LugaresEstadisticas(
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
