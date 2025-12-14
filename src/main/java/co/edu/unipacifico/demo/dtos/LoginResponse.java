// LoginResponse.java (Para el frontend)
package co.edu.unipacifico.demo.dtos;

public record LoginResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    Long expiresIn,
    UsuariosResponse user // <-- ¡LO QUE EL FRONTEND ESPERA!
) { }