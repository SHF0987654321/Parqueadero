package co.edu.unipacifico.demo.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipacifico.demo.dtos.UsuariosDTO;
import co.edu.unipacifico.demo.exceptions.InvalidUserExeception;
import co.edu.unipacifico.demo.exceptions.ResouseNotFoundException;
import co.edu.unipacifico.demo.services.security.UsuariosService;
import co.edu.unipacifico.demo.services.security.UsuariosServiceImpl;
import co.edu.unipacifico.demo.services.security.data.TokenInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {

    private final UsuariosService usuariosService;
    private final UsuariosServiceImpl usuariosServiceImpl;

    /**
     * Registrar nuevo usuario
     */
    @PostMapping("/registrar")
    public ResponseEntity<UsuariosDTO> registrarUsuarios(@RequestBody @Valid UsuariosDTO usuariosDTO) {
        UsuariosDTO usuarioRegistrado = usuariosService.registrarUsuario(usuariosDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRegistrado);
    }

    /**
     * Obtener usuario por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuariosDTO> getUsuarios(@PathVariable Long id) { 
        Optional<UsuariosDTO> usuario = usuariosService.getUsuarioById(id);
        
        return usuario
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResouseNotFoundException("Usuario no encontrado con id: " + id));
    }

    /**
     * Buscar usuario por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<UsuariosDTO> getUsuarioPorNombre(@RequestParam String nombre) {
        UsuariosDTO usuario = usuariosService.getUsuarioByNombre(nombre);
        return ResponseEntity.ok(usuario);
    }

    /**
     * Actualizar usuario
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuariosDTO> actualizarUsuario(
        @PathVariable Long id, 
        @RequestBody @Valid UsuariosDTO usuariosDTO
    ) {
        UsuariosDTO usuarioActualizado = usuariosService.actualizarUsuario(id, usuariosDTO);
        return ResponseEntity.ok(usuarioActualizado);
    }

    /**
     * Eliminar usuario
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuariosService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
