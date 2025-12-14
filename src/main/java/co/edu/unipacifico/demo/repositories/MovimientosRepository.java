package co.edu.unipacifico.demo.repositories;

import co.edu.unipacifico.demo.models.Movimientos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovimientosRepository extends JpaRepository<Movimientos, Long> {
    
    // 🔹 Movimiento activo de un vehículo (fecha_salida IS NULL)
    Optional<Movimientos> findByVehiculoIdAndFechaSalidaIsNull(Long vehiculoId);

    // 🔹 Verificar si un lugar está ocupado
    boolean existsByLugarIdAndFechaSalidaIsNull(Long lugarId);

    // 🔹 Todos los movimientos activos
    List<Movimientos> findByFechaSalidaIsNull();

    // 🔹 Historial por vehículo
    List<Movimientos> findByVehiculoIdOrderByFechaEntradaDesc(Long vehiculoId);

    // 🔹 Historial por usuario
    List<Movimientos> findByUsuarioIdOrderByFechaEntradaDesc(Long usuarioId);

    // 🔹 Todos los movimientos ordenados
    List<Movimientos> findAllByOrderByFechaEntradaDesc();
}