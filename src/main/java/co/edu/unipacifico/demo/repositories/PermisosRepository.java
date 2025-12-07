package co.edu.unipacifico.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unipacifico.demo.models.Permisos;

@Repository
public interface PermisosRepository extends JpaRepository<Permisos, Long> {

}
