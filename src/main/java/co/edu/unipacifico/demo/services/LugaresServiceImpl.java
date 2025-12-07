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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LugaresServiceImpl implements LugaresService {

    private final LugaresRepository lugaresRepository;
    private final LugaresMapper lugaresMapper;

    @Override
    public LugaresDTO crearLugar(LugaresDTO lugarDTO) {
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
            return lugaresRepository.findAll().stream()
                    .map(lugaresMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseException("Error al consultar todos los lugares.", e);
        }
    }

    @Override
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
            return lugaresRepository.findByTipo(tipo).stream()
                    .map(lugaresMapper::toDTO)
                    .collect(Collectors.toList());
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
            return lugares.stream()
                    .map(lugaresMapper::toDTO)
                    .collect(Collectors.toList());
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
            return lugares.stream()
                    .map(lugaresMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseException("Error al consultar lugares libres.", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public LugaresEstadisticasDTO obtenerEstadisticas(String tipo) {
        try {
            Long totalLugares;
            Long lugaresOcupados;
            
            if (tipo != null && !tipo.isEmpty()) {
                totalLugares = (long) lugaresRepository.findByTipo(tipo).size();
                lugaresOcupados = lugaresRepository.contarLugaresOcupadosPorTipo(tipo);
            } else {
                totalLugares = lugaresRepository.count();
                lugaresOcupados = lugaresRepository.contarLugaresOcupados();
            }
            
            Long lugaresLibres = totalLugares - lugaresOcupados;
            
            return new LugaresEstadisticasDTO(totalLugares, lugaresOcupados, lugaresLibres, tipo);
        } catch (Exception e) {
            throw new DatabaseException("Error al obtener estadísticas de lugares.", e);
        }
    }
}
