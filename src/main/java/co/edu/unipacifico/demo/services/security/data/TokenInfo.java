package co.edu.unipacifico.demo.services.security.data;

public record TokenInfo(String accessToken,
    String refreshToken,
    String tokenType,
    Long expiresIn) {

        public TokenInfo(String accessToken, String refreshToken) {
        this(accessToken, refreshToken, "Bearer", 3600L);
    }
    
    public TokenInfo(String accessToken) {
        this(accessToken, null, "Bearer", 3600L);
    }

}
