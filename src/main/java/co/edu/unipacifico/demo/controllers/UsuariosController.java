package co.edu.unipacifico.demo.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipacifico.demo.dtos.UsuariosResponse;
import co.edu.unipacifico.demo.exceptions.ResouseNotFoundException;
import co.edu.unipacifico.demo.services.security.UsuariosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {

    private final UsuariosService usuariosService;
   /**
     * Obtener usuario por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuariosResponse> getUsuarios(@PathVariable Long id) { 
        Optional<UsuariosResponse> usuario = usuariosService.getUsuarioById(id);
        
        return usuario
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResouseNotFoundException("Usuario no encontrado con id: " + id));
    }

    /**
     * Buscar usuario por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<UsuariosResponse> getUsuarioPorNombre(@RequestParam String nombre) {
        UsuariosResponse usuario = usuariosService.getUsuarioByNombre(nombre);
        return ResponseEntity.ok(usuario);
    }

    /**
     * Actualizar usuario
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuariosResponse> actualizarUsuario(
        @PathVariable Long id, 
        @RequestBody @Valid UsuariosResponse usuariosDTO
    ) {
        UsuariosResponse usuarioActualizado = usuariosService.actualizarUsuario(id, usuariosDTO);
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
