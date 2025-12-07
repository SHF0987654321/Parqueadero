package co.edu.unipacifico.demo.services.security;

import java.util.Optional;

import co.edu.unipacifico.demo.dtos.UsuariosDTO;

public interface UsuariosService {

    UsuariosDTO getUsuarioByNombre(String nombre);
    Optional<UsuariosDTO> getUsuarioById(Long id);
    UsuariosDTO registrarUsuario(UsuariosDTO usuarioDTO);
    UsuariosDTO actualizarUsuario(Long id, UsuariosDTO cambiosDTO);
    void eliminarUsuario(Long id);
    
}
