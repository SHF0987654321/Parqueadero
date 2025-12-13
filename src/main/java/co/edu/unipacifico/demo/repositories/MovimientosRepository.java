package co.edu.unipacifico.demo.repositories;

import co.edu.unipacifico.demo.models.Movimientos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovimientosRepository extends JpaRepository<Movimientos, Long> {
    
    // Buscar movimientos activos (sin fecha de salida) de un vehículo
    @Query("SELECT m FROM Movimientos m WHERE m.vehiculo.id = :vehiculoId AND m.fechaSalida IS NULL")
    Optional<Movimientos> findMovimientoActivoByVehiculoId(@Param("vehiculoId") Long vehiculoId);
    
    // Buscar movimientos activos de un lugar
    @Query("SELECT m FROM Movimientos m WHERE m.lugar.id = :lugarId AND m.fechaSalida IS NULL")
    List<Movimientos> findMovimientosActivosByLugarId(@Param("lugarId") Long lugarId);
    
    // Buscar todos los movimientos activos
    @Query("SELECT m FROM Movimientos m WHERE m.fechaSalida IS NULL")
    List<Movimientos> findMovimientosActivos();
    
    // Buscar historial de movimientos de un vehículo
    List<Movimientos> findByVehiculoIdOrderByFechaEntradaDesc(Long vehiculoId);
    
    // Buscar historial de movimientos de un usuario
    List<Movimientos> findByUsuarioIdOrderByFechaEntradaDesc(Long usuarioId);

    // Buscar todos los movimientos (activos e inactivos)
    List<Movimientos> findAllByOrderByFechaEntradaDesc();
}