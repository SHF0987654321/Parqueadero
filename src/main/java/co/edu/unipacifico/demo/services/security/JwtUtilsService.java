package co.edu.unipacifico.demo.services.security;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import co.edu.unipacifico.demo.services.security.data.TokenInfo;

@Service
public class JwtUtilsService {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-time}") // 1 hora (3600000 ms)
    private Long accessTokenExpiration;

    @Value("${app.jwt.refresh-expiration-time}") // 7 días por defecto
    private Long refreshTokenExpiration;

    @Value("${app.jwt.user-generator}")
    private String issuer;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    // Crear Access Token y Refresh Token desde Authentication
    public TokenInfo crearTokens(Authentication authentication) {
        String username = authentication.getName();
        
        String authorities = authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
        
        String accessToken = crearAccessToken(username, authorities);
        String refreshToken = crearRefreshToken(username);
        
        return new TokenInfo(
            accessToken, 
            refreshToken, 
            "Bearer", 
            accessTokenExpiration / 1000 // Convertir a segundos
        );
    }

    // Crear Access Token (corta duración, contiene authorities)
    private String crearAccessToken(String username, String authorities) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

        return JWT.create()
                .withIssuer(issuer)
                .withSubject(username)
                .withClaim("authorities", authorities)
                .withClaim("type", "access")
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(getAlgorithm());
    }

    // Crear Refresh Token (larga duración, sin authorities)
    private String crearRefreshToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

        return JWT.create()
                .withIssuer(issuer)
                .withSubject(username)
                .withClaim("type", "refresh")
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(getAlgorithm());
    }

    // Validar y decodificar cualquier token
    public DecodedJWT validarJwt(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(getAlgorithm())
                .withIssuer(issuer)
                .build();
        return verifier.verify(token);
    }

    // Validar que sea un Access Token
    public boolean esAccessToken(DecodedJWT decodedJWT) {
        Claim typeClaim = decodedJWT.getClaim("type");
        return !typeClaim.isNull() && "access".equals(typeClaim.asString());
    }

    // Validar que sea un Refresh Token
    public boolean esRefreshToken(DecodedJWT decodedJWT) {
        Claim typeClaim = decodedJWT.getClaim("type");
        return !typeClaim.isNull() && "refresh".equals(typeClaim.asString());
    }

    // Extraer username
    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    // Obtener claim específico
    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }

    // Crear nuevo Access Token desde un username y authorities
    public String crearNuevoAccessToken(String username, String authorities) {
        return crearAccessToken(username, authorities);
    }

    // Obtener tiempo de expiración del access token en segundos
    public Long getAccessTokenExpirationInSeconds() {
        return accessTokenExpiration / 1000;
    }
}
