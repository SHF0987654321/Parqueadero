package co.edu.unipacifico.demo.services.security;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unipacifico.demo.dtos.UsuariosDTO;
import co.edu.unipacifico.demo.exceptions.DatabaseException;
import co.edu.unipacifico.demo.exceptions.InvalidUserExeception;
import co.edu.unipacifico.demo.exceptions.ResouseNotFoundException;
import co.edu.unipacifico.demo.mappers.UsuariosMapper;
import co.edu.unipacifico.demo.models.Roles;
import co.edu.unipacifico.demo.models.Usuarios;
import co.edu.unipacifico.demo.repositories.RolesRepository;
import co.edu.unipacifico.demo.repositories.UsuariosRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Servicio de gestión de usuarios
 * Implementa UserDetailsService para Spring Security
 * Los métodos de autenticación (login, refresh) están en AuthService
 */
@Service
@RequiredArgsConstructor
public class UsuariosServiceImpl implements UsuariosService, UserDetailsService {

    private final UsuariosMapper usuariosMapper;
    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;

    @Override
    public Optional<UsuariosDTO> getUsuarioById(Long id) {
        try {
            Optional<Usuarios> optionalUsuario = usuariosRepository.findById(id);

            if (optionalUsuario.isEmpty()) {
                return Optional.empty();
            }

            Usuarios usuario = optionalUsuario.get();
            UsuariosDTO usuariosDTO = usuariosMapper.toDTO(usuario);
            // No exponer contraseña
            usuariosDTO.setClave(null);
            
            return Optional.of(usuariosDTO);

        } catch (Exception e) {
            throw new DatabaseException("Error al consultar el usuario en la base de datos.", e);
        }
    }

    @Override
    public UsuariosDTO getUsuarioByNombre(String nombre) {
        Usuarios usuario = usuariosRepository.findByNombre(nombre)
            .orElseThrow(() -> new ResouseNotFoundException("Usuario no encontrado con nombre: " + nombre));
        
        UsuariosDTO dto = usuariosMapper.toDTO(usuario);
        dto.setClave(null); // No exponer contraseña
        return dto;
    }

    @Override
    @Transactional
    public UsuariosDTO registrarUsuario(UsuariosDTO usuarioDTO) {
        // 1. Validación: Usuario ya existe
        if (usuariosRepository.findByNombre(usuarioDTO.getNombre()).isPresent()) {
            throw new InvalidUserExeception("Ya existe un usuario con el nombre: " + usuarioDTO.getNombre());
        }

        // 2. Convertir DTO a Entity
        Usuarios nuevoUsuario = usuariosMapper.toEntity(usuarioDTO);

        // 3. Encriptar contraseña
        nuevoUsuario.setClave(passwordEncoder.encode(usuarioDTO.getClave()));

        // 4. ASIGNAR ROL POR DEFECTO (USER) IGNORANDO EL ROL EN EL DTO DE ENTRADA
        // Esto asegura que nadie se auto-asigne el rol de ADMIN en el registro.
        Roles rolDefault = rolesRepository.findByNombre("USER")
            .orElseThrow(() -> new InvalidUserExeception("Rol por defecto 'USER' no encontrado"));
    
        nuevoUsuario.setRoles(Set.of(rolDefault));
    
        // 5. Guardar
        Usuarios usuarioGuardado = usuariosRepository.save(nuevoUsuario);
    
        // 6. Retornar DTO sin contraseña
        UsuariosDTO resultado = usuariosMapper.toDTO(usuarioGuardado);
        resultado.setClave(null);
    
        return resultado;
    }

    @Override
    @Transactional
    public UsuariosDTO actualizarUsuario(Long id, UsuariosDTO usuarioDTO) {
        // Buscar usuario existente
        Usuarios usuarioExistente = usuariosRepository.findById(id)
            .orElseThrow(() -> new ResouseNotFoundException("Usuario no encontrado con id: " + id));
        
        // Actualizar nombre si es proporcionado
        if (usuarioDTO.getNombre() != null && !usuarioDTO.getNombre().isEmpty()) {
            usuariosRepository.findByNombre(usuarioDTO.getNombre())
                .ifPresent(u -> {
                    if (!u.getId().equals(id)) {
                        throw new InvalidUserExeception("El nombre ya está en uso por otro usuario");
                    }
                });
            usuarioExistente.setNombre(usuarioDTO.getNombre());
        }
        
        // Actualizar contraseña si es proporcionada
        if (usuarioDTO.getClave() != null && !usuarioDTO.getClave().isEmpty()) {
            usuarioExistente.setClave(passwordEncoder.encode(usuarioDTO.getClave()));
        }
        
        // Actualizar roles si son proporcionados
        if (usuarioDTO.getRoles() != null && !usuarioDTO.getRoles().isEmpty()) {
            Set<Roles> roles = usuarioDTO.getRoles() 
                .stream()
                .map(idRol -> rolesRepository.findById(idRol)
                    .orElseThrow(() -> new InvalidUserExeception("Rol no encontrado con id: " + idRol))
                )
                .collect(Collectors.toSet());
            usuarioExistente.setRoles(roles);
        }
        
        // Guardar cambios
        Usuarios usuarioActualizado = usuariosRepository.save(usuarioExistente);
        
        UsuariosDTO resultado = usuariosMapper.toDTO(usuarioActualizado);
        resultado.setClave(null);
        
        return resultado;
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuariosRepository.existsById(id)) {
            throw new ResouseNotFoundException("Usuario no encontrado con id: " + id);
        }
        usuariosRepository.deleteById(id);
    }

    /**
     * Implementación de UserDetailsService para Spring Security
     * Este método es usado por Spring Security durante la autenticación
     */
    @Override 
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
        Usuarios usuario = usuariosRepository.findByNombre(nombre)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + nombre));
        
        List<SimpleGrantedAuthority> authorities = usuario.getRoles()
            .stream()
            .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
            .collect(Collectors.toList());

        return new User(
            usuario.getNombre(),
            usuario.getClave(),
            authorities
        );
    }
}
