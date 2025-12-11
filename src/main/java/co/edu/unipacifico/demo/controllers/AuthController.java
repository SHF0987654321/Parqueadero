package co.edu.unipacifico.demo.controllers;

import co.edu.unipacifico.demo.dtos.LoginRequest;
import co.edu.unipacifico.demo.dtos.LoginResponse;
import co.edu.unipacifico.demo.dtos.RegisterRequest;
import co.edu.unipacifico.demo.dtos.UsuariosResponse;
import co.edu.unipacifico.demo.services.security.AuthService;
import co.edu.unipacifico.demo.services.security.UsuariosServiceImpl;
import co.edu.unipacifico.demo.services.security.data.TokenInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<UsuariosResponse> registrarUsuario(@Valid @RequestBody RegisterRequest registerRequest) {
        UsuariosResponse nuevoUsuario = usuariosService.registrarUsuario(registerRequest);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    /**
     * Endpoint para login
     * POST /api/auth/login
     * Body: { "nombre": "usuario", "clave": "password" }
     * Response: { "accessToken": "...", "refreshToken": "...", "tokenType": "Bearer", "expiresIn": 3600 }
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
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
    public ResponseEntity<UsuariosResponse> obtenerUsuarioActual(@RequestHeader("Authorization") String authHeader) {
        // El token ya fue validado por el filtro JWT
        // Aquí puedes extraer el username del SecurityContext
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        
        UsuariosResponse usuario = usuariosService.getUsuarioByEmail(username);
        return ResponseEntity.ok(usuario);
    }
}
