package co.edu.unipacifico.demo.controllers;

import co.edu.unipacifico.demo.dtos.UsuariosDTO;
import co.edu.unipacifico.demo.services.security.AuthService;
import co.edu.unipacifico.demo.services.security.UsuariosServiceImpl;
import co.edu.unipacifico.demo.services.security.data.TokenInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final UsuariosServiceImpl usuariosService;
    private final AuthService authService;

    /**
     * Endpoint para registro de nuevos usuarios
     * POST /api/auth/registro
     */
    @PostMapping("/registro")
    public ResponseEntity<UsuariosDTO> registrarUsuario(@Valid @RequestBody UsuariosDTO usuarioDTO) {
        UsuariosDTO nuevoUsuario = usuariosService.registrarUsuario(usuarioDTO);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    /**
     * Endpoint para login
     * POST /api/auth/login
     * Body: { "nombre": "usuario", "clave": "password" }
     * Response: { "accessToken": "...", "refreshToken": "...", "tokenType": "Bearer", "expiresIn": 3600 }
     */
    @PostMapping("/login")
    public ResponseEntity<TokenInfo> login(@Valid @RequestBody UsuariosDTO usuarioDTO) {
        TokenInfo tokenInfo = authService.login(usuarioDTO);
        return ResponseEntity.ok(tokenInfo);
    }

    /**
     * Endpoint para renovar el access token usando el refresh token
     * POST /api/auth/refresh
     * Body: { "refreshToken": "..." }
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenInfo> renovarToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        TokenInfo tokenInfo = authService.renovarToken(refreshToken);
        return ResponseEntity.ok(tokenInfo);
    }

    /**
     * Endpoint para obtener información del usuario autenticado
     * GET /api/auth/me
     * Header: Authorization: Bearer <token>
     */
    @GetMapping("/me")
    public ResponseEntity<UsuariosDTO> obtenerUsuarioActual(@RequestHeader("Authorization") String authHeader) {
        // El token ya fue validado por el filtro JWT
        // Aquí puedes extraer el username del SecurityContext
        String username = org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        
        UsuariosDTO usuario = usuariosService.getUsuarioByNombre(username);
        return ResponseEntity.ok(usuario);
    }
}
