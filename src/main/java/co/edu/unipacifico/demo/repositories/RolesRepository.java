package co.edu.unipacifico.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unipacifico.demo.dtos.UsuariosDTO;
import co.edu.unipacifico.demo.models.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByNombre(String nombre);
}
