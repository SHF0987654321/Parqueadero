package co.edu.unipacifico.demo.exceptions;

import java.io.IOException;
import java.time.Instant;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
       
        // 1. Establece el tipo de contenido de la respuesta a JSON.
        response.setContentType("application/json");
        
        // 2. Establece el código de estado HTTP 401: Unauthorized.
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 

        // 3. Escribe el cuerpo de la respuesta en formato JSON.
        response.getWriter().write(
            """
            {"codigo": 6, "mensaje": "No está autorizado para hacer esta solicitud", "hora": "%s"}
            """.formatted(Instant.now()));
    }

    

}
