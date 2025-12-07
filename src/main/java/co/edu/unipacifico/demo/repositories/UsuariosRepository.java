package co.edu.unipacifico.demo.repositories;

import co.edu.unipacifico.demo.models.Usuarios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {

    // 1. READ (Consultar por nombre) -> Usado en /inicio
    Optional<Usuarios> findByNombre(String nombre);

}
