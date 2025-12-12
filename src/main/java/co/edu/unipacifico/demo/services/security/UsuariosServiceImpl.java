package co.edu.unipacifico.demo.services.security;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unipacifico.demo.dtos.RegisterRequest;
import co.edu.unipacifico.demo.dtos.UsuariosRequest;
import co.edu.unipacifico.demo.dtos.UsuariosResponse;
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
 */
@Service
@RequiredArgsConstructor
public class UsuariosServiceImpl implements UsuariosService, UserDetailsService {

    private final UsuariosMapper usuariosMapper;
    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;

    @Override
    public Optional<UsuariosResponse> getUsuarioById(Long id) {
        try {
            Optional<Usuarios> optionalUsuario = usuariosRepository.findById(id);
            if (optionalUsuario.isEmpty()) {
                return Optional.empty();
            }
            Usuarios usuario = optionalUsuario.get();
            UsuariosResponse usuariosDTO = usuariosMapper.toDTO(usuario);
            return Optional.of(usuariosDTO);
        } catch (Exception e) {
            throw new DatabaseException("Error al consultar el usuario en la base de datos.", e);
        }
    }

    @Override
    public UsuariosResponse getUsuarioByNombre(String nombre) {
        Usuarios usuario = usuariosRepository.findByNombre(nombre)
            // CORRECCIÓN: ResouseNotFoundException -> ResourceNotFoundException
            .orElseThrow(() -> new ResouseNotFoundException("Usuario no encontrado con nombre: " + nombre));
        
        UsuariosResponse dto = usuariosMapper.toDTO(usuario);
        return dto;
    }

    @Override
    @Transactional
    public UsuariosResponse registrarUsuario(RegisterRequest registerRequest) {
        // 1. Validación: Usuario ya existe
        if (usuariosRepository.findByNombre(registerRequest.getNombre()).isPresent()) {
            // CORRECCIÓN: InvalidUserExeception -> InvalidUserException
            throw new InvalidUserExeception("Ya existe un usuario con el nombre: " + registerRequest.getNombre());
        }
        if (usuariosRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            // CORRECCIÓN: InvalidUserExeception -> InvalidUserException y mensaje
            throw new InvalidUserExeception("Ya existe un usuario con el email: " + registerRequest.getEmail());
        }

        // 2. Convertir DTO a Entity
        Usuarios nuevoUsuario = usuariosMapper.toEntity(registerRequest);

        // 3. Encriptar contraseña
        nuevoUsuario.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // 4. ASIGNAR ROL POR DEFECTO (USER)
        Roles rolDefault = rolesRepository.findByNombre("USER")
            // CORRECCIÓN: InvalidUserExeception -> InvalidUserException
            .orElseThrow(() -> new InvalidUserExeception("Rol por defecto 'USER' no encontrado"));
    
        nuevoUsuario.setRoles(Set.of(rolDefault));
    
        // 5. Guardar
        Usuarios usuarioGuardado = usuariosRepository.save(nuevoUsuario);
    
        UsuariosResponse resultado = usuariosMapper.toDTO(usuarioGuardado);
    
        return resultado;
    }

    @Override
    @Transactional
    public UsuariosResponse actualizarUsuario(Long id, UsuariosRequest usuario) {
        // 1. Buscar usuario existente
        Usuarios usuarioExistente = usuariosRepository.findById(id)
            // CORRECCIÓN: ResouseNotFoundException -> ResourceNotFoundException
            .orElseThrow(() -> new ResouseNotFoundException("Usuario no encontrado con id: " + id));
    
        // --- 2. ACTUALIZAR EMAIL (CRÍTICO) ---
        if (usuario.getEmail() != null && !usuario.getEmail().isEmpty()) {
            usuariosRepository.findByEmail(usuario.getEmail()) 
                .ifPresent(u -> {
                    if (!u.getId().equals(id)) {
                         // CORRECCIÓN: InvalidUserExeception -> InvalidUserException
                        throw new InvalidUserExeception("El correo electrónico ya está en uso por otro usuario");
                    }
                });
            usuarioExistente.setEmail(usuario.getEmail());
        }
    
        // --- 3. ACTUALIZAR NOMBRE (Si es editable) ---
        if (usuario.getNombre() != null && !usuario.getNombre().isEmpty()) {
            usuariosRepository.findByNombre(usuario.getNombre())
                .ifPresent(u -> {
                    if (!u.getId().equals(id)) {
                        // CORRECCIÓN: InvalidUserExeception -> InvalidUserException
                        throw new InvalidUserExeception("El nombre ya está en uso por otro usuario");
                    }
                });
            usuarioExistente.setNombre(usuario.getNombre());
        }
    
        // --- 4. ACTUALIZAR CONTRASEÑA ---
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
    
        // 6. Guardar cambios
        Usuarios usuarioActualizado = usuariosRepository.save(usuarioExistente);
    
        UsuariosResponse resultado = usuariosMapper.toDTO(usuarioActualizado);
    
        return resultado;
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuariosRepository.existsById(id)) {
            // CORRECCIÓN: ResouseNotFoundException -> ResourceNotFoundException
            throw new ResouseNotFoundException("Usuario no encontrado con id: " + id);
        }
        usuariosRepository.deleteById(id);
    }

    /**
     * Implementación de UserDetailsService para Spring Security
     * CORRECCIÓN: La búsqueda debe ser por email, no por nombre.
     */
    @Override 
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuarios usuario = usuariosRepository.findByEmail(email) // <-- ¡CORRECCIÓN CRÍTICA!
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
        
        List<SimpleGrantedAuthority> authorities = usuario.getRoles()
            .stream()
            .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
            .collect(Collectors.toList());

        return new User(
            usuario.getEmail(), // Devolvemos el email como el "username"
            usuario.getPassword(),
            authorities
        );
    }
    @Override
    public UsuariosResponse getUsuarioByEmail(String email) {
        Usuarios usuario = usuariosRepository.findByEmail(email)
            .orElseThrow(() -> new ResouseNotFoundException("Usuario no encontrado con email: " + email));

        UsuariosResponse dto = usuariosMapper.toDTO(usuario);
        return dto;
    }
}
