package co.edu.unipacifico.demo.repositories;

import co.edu.unipacifico.demo.dtos.LugarConEstadoProjection;
import co.edu.unipacifico.demo.models.Lugares;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LugaresRepository extends JpaRepository<Lugares, Long> {

    List<Lugares> findByTipo(String tipo);

    Optional<Lugares> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    @Query("""
    SELECT COUNT(DISTINCT m.lugar.id)
    FROM Movimientos m
    WHERE m.fechaSalida IS NULL
    """)
    Long contarLugaresOcupados();

    @Query("""
    SELECT COUNT(DISTINCT m.lugar.id)
    FROM Movimientos m
    WHERE m.fechaSalida IS NULL
    AND m.lugar.tipo = :tipo
    """)
    Long contarLugaresOcupadosPorTipo(@Param("tipo") String tipo);

    @Query("""
    SELECT 
        l.id AS id,
        l.nombre AS nombre,
        l.tipo AS tipo,
        CASE 
            WHEN m.id IS NOT NULL THEN 'OCUPADO'
            ELSE 'LIBRE'
        END AS estado
    FROM Lugares l
    LEFT JOIN Movimientos m 
        ON m.lugar.id = l.id 
        AND m.fechaSalida IS NULL
    """)
    List<LugarConEstadoProjection> findAllWithEstado();

    @Query("""
    SELECT 
        l.id AS id,
        l.nombre AS nombre,
        l.tipo AS tipo,
        CASE 
            WHEN m.id IS NOT NULL THEN 'OCUPADO'
            ELSE 'LIBRE'
        END AS estado
    FROM Lugares l
    LEFT JOIN Movimientos m 
        ON m.lugar.id = l.id 
        AND m.fechaSalida IS NULL
    WHERE (:tipo IS NULL OR l.tipo = :tipo)
    """)
    List<LugarConEstadoProjection> findAllWithEstadoByTipo(@Param("tipo") String tipo);
    
    @Query("""
    SELECT l
    FROM Lugares l
    LEFT JOIN Movimientos m 
        ON m.lugar.id = l.id 
        AND m.fechaSalida IS NULL
    WHERE l.tipo = :tipo
    AND m.id IS NULL
    ORDER BY l.id
    """)
    List<Lugares> findLugarLibrePorTipo(
            @Param("tipo") String tipo,
            Pageable pageable);
}
