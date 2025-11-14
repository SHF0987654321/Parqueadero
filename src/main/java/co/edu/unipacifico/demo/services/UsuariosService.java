package co.edu.unipacifico.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.edu.unipacifico.demo.repositories.UsuariosRepository;
import lombok.var;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
public class UsuariosService implements UserDetailsService { // **CORRECCIÓN 1: Implementa UserDetailsService**

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Override 
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
        
        var usuario = usuariosRepository.findByNombre(nombre)
            .orElseThrow(() -> new UsernameNotFoundException(
                "Usuario no encontrado: " + nombre));
        
        return new User(
            usuario.getNombre(),
            usuario.getClave(), // Ojo: La contraseña debe estar codificada (hashed)
            List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()))
        );
    }
}
