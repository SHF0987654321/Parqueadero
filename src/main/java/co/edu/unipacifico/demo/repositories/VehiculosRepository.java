package co.edu.unipacifico.demo.repositories;

import co.edu.unipacifico.demo.models.Vehiculos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VehiculosRepository extends JpaRepository<Vehiculos, Long>, JpaSpecificationExecutor<Vehiculos> {
    List<Vehiculos> findByTipo(String tipo);
    Optional<Vehiculos> findByPlaca(String placa);

}
