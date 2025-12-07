package co.edu.unipacifico.demo.repositories;

import co.edu.unipacifico.demo.models.Lugares;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LugaresRepository extends JpaRepository<Lugares, Long> {
    
    // Buscar lugares por tipo
    List<Lugares> findByTipo(String tipo);
    
    // Contar lugares ocupados (que tienen movimientos sin fecha_salida)
    @Query("SELECT COUNT(DISTINCT m.lugar.id) FROM Movimientos m WHERE m.fechaSalida IS NULL")
    Long contarLugaresOcupados();
    
    // Contar lugares ocupados por tipo
    @Query("SELECT COUNT(DISTINCT m.lugar.id) FROM Movimientos m WHERE m.fechaSalida IS NULL AND m.lugar.tipo = :tipo")
    Long contarLugaresOcupadosPorTipo(@Param("tipo") String tipo);
    
    // Obtener lugares ocupados
    @Query("SELECT DISTINCT m.lugar FROM Movimientos m WHERE m.fechaSalida IS NULL")
    List<Lugares> findLugaresOcupados();
    
    // Obtener lugares ocupados por tipo
    @Query("SELECT DISTINCT m.lugar FROM Movimientos m WHERE m.fechaSalida IS NULL AND m.lugar.tipo = :tipo")
    List<Lugares> findLugaresOcupadosPorTipo(@Param("tipo") String tipo);
    
    // Obtener lugares libres (sin movimientos activos)
    @Query("SELECT l FROM Lugares l WHERE l.id NOT IN (SELECT DISTINCT m.lugar.id FROM Movimientos m WHERE m.fechaSalida IS NULL)")
    List<Lugares> findLugaresLibres();
    
    // Obtener lugares libres por tipo
    @Query("SELECT l FROM Lugares l WHERE l.tipo = :tipo AND l.id NOT IN (SELECT DISTINCT m.lugar.id FROM Movimientos m WHERE m.fechaSalida IS NULL AND m.lugar.tipo = :tipo)")
    List<Lugares> findLugaresLibresPorTipo(@Param("tipo") String tipo);
}
