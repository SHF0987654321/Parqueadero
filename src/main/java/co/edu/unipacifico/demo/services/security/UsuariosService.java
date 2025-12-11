package co.edu.unipacifico.demo.services.security;

import java.util.Optional;

import co.edu.unipacifico.demo.dtos.RegisterRequest;
import co.edu.unipacifico.demo.dtos.UsuariosResponse;

public interface UsuariosService {

    UsuariosResponse getUsuarioByNombre(String nombre);
    Optional<UsuariosResponse> getUsuarioById(Long id);
    UsuariosResponse registrarUsuario(RegisterRequest registerRequest);
    UsuariosResponse actualizarUsuario(Long id, UsuariosResponse cambiosDTO);
    void eliminarUsuario(Long id);
    UsuariosResponse getUsuarioByEmail(String email);
    
}
