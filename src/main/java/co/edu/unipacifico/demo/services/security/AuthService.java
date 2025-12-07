package co.edu.unipacifico.demo.services.security;

import co.edu.unipacifico.demo.dtos.UsuariosDTO;
import co.edu.unipacifico.demo.exceptions.InvalidUserExeception;
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

    /**
     * Login: Autentica usuario y genera tokens
     */
    public TokenInfo login(UsuariosDTO usuarioDTO) {
        try {
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                usuarioDTO.getNombre(), 
                usuarioDTO.getClave()
            );

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            return jwtUtilsService.crearTokens(authentication);

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
