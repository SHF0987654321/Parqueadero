// LoginResponse.java (Para el frontend)
package co.edu.unipacifico.demo.dtos;

import co.edu.unipacifico.demo.services.security.data.TokenInfo;

public record LoginResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    Long expiresIn,
    UsuariosResponse user // <-- ¡LO QUE EL FRONTEND ESPERA!
) { }