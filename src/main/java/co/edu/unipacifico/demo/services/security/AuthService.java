package co.edu.unipacifico.demo.services.security;

import co.edu.unipacifico.demo.dtos.LoginRequest;
import co.edu.unipacifico.demo.dtos.LoginResponse;
import co.edu.unipacifico.demo.dtos.UsuariosResponse;
import co.edu.unipacifico.demo.exceptions.InvalidUserExeception;
import co.edu.unipacifico.demo.mappers.UsuariosMapper;
import co.edu.unipacifico.demo.models.Usuarios;
import co.edu.unipacifico.demo.repositories.UsuariosRepository;
import co.edu.unipacifico.demo.services.security.data.TokenInfo;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * Servicio dedicado a la autenticación (login, refresh token)
 * Separado de UsuariosServiceImpl para romper el ciclo de dependencias
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtilsService jwtUtilsService;
    private final UserDetailsService userDetailsService;
    private final UsuariosRepository usuariosRepository; 
    private final UsuariosMapper usuariosMapper;

/**
     * Login: Autentica usuario y genera tokens + datos del usuario
     */
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            // 1. CORRECCIÓN CLAVE: Crear el token de autenticación
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), 
                loginRequest.getPassword()
            );

            // 2. Autenticar: PASAR EL ARGUMENTO
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 3. Crear Tokens
            TokenInfo tokenInfo = jwtUtilsService.crearTokens(authentication);

            // 4. Obtener UsuariosResponse para el frontend (usando las nuevas inyecciones)
            String email = authentication.getName(); // El nombre de autenticación es el email
            Usuarios usuario = usuariosRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario autenticado no encontrado en DB."));
            
            UsuariosResponse userResponse = usuariosMapper.toDTO(usuario);
            userResponse.setPassword(null); // Seguridad: no exponer el password/hash
            
            // 5. Devolver la respuesta unificada (LoginResponse)
            return new LoginResponse(
                tokenInfo.accessToken(),
                tokenInfo.refreshToken(),
                tokenInfo.tokenType(),
                tokenInfo.expiresIn(),
                userResponse // <-- Objeto de usuario requerido por el frontend
            );

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Credenciales inválidas o usuario no encontrado.", e);
        }
    }

    /**
     * Renovar access token usando refresh token
     */
    public TokenInfo renovarToken(String refreshToken) {
        try {
            DecodedJWT decodedJWT = jwtUtilsService.validarJwt(refreshToken);
            
            if (!jwtUtilsService.esRefreshToken(decodedJWT)) {
                throw new InvalidUserExeception("Token inválido para renovación");
            }
            
            String username = jwtUtilsService.extractUsername(decodedJWT);
            
            // Cargar usuario con authorities actualizadas
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            // Obtener authorities
            String authorities = userDetails.getAuthorities()
                .stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.joining(","));
            
            // Crear nuevo access token
            String newAccessToken = jwtUtilsService.crearNuevoAccessToken(username, authorities);
            
            return new TokenInfo(
                newAccessToken,
                refreshToken,
                "Bearer",
                jwtUtilsService.getAccessTokenExpirationInSeconds()
            );
            
        } catch (JWTVerificationException e) {
            throw new InvalidUserExeception("Refresh token inválido o expirado");
        } catch (UsernameNotFoundException e) {
            throw new InvalidUserExeception("Usuario no encontrado: " + e.getMessage());
        } catch (Exception e) {
            throw new InvalidUserExeception("Error al renovar el token: " + e.getMessage());
        }
    }
}
