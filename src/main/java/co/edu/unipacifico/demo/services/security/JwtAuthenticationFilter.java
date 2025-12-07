package co.edu.unipacifico.demo.services.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtilsService jwtUtilsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Extraer el token del header Authorization
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // Remover "Bearer "

        try {
            // Validar el token
            DecodedJWT decodedJWT = jwtUtilsService.validarJwt(token);

            // Verificar que sea un access token
            if (!jwtUtilsService.esAccessToken(decodedJWT)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token inválido: Se esperaba un access token");
                return;
            }

            // Extraer información del token
            String username = jwtUtilsService.extractUsername(decodedJWT);
            Claim authoritiesClaim = jwtUtilsService.getSpecificClaim(decodedJWT, "authorities");

            // Convertir authorities a lista de SimpleGrantedAuthority
            List<SimpleGrantedAuthority> authorities = Arrays
                    .stream(authoritiesClaim.asString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // Crear Authentication y guardarlo en SecurityContext
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JWTVerificationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token inválido o expirado: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }
}
