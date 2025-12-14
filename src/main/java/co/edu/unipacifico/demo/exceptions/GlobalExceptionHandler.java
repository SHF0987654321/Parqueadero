package co.edu.unipacifico.demo.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import co.edu.unipacifico.demo.dtos.ErroresResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Manejo de operaciones inválidas y errores de parámetros
    @ExceptionHandler({InvalidOperationExeception.class, ParameterException.class})
    public ResponseEntity<ErroresResponse> handleBadRequest(Exception ex) {
        ErroresResponse error = ErroresResponse.builder()
            .codigo("INVALID_OPERATION")
            .mensaje(ex.getMessage())
            .hora(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Manejo de errores de base de datos
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ErroresResponse> handleDatabaseError(DatabaseException ex) {
        ErroresResponse error = ErroresResponse.builder()
            .codigo("DB_INTERNAL_ERROR")
            .mensaje("Fallo en la capa de persistencia: " + ex.getMessage())
            .hora(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // Manejo de errores de validación (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationError(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        BindingResult result = ex.getBindingResult();
        
        for (ObjectError error : result.getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                errores.put(fieldError.getField(), error.getDefaultMessage());
            } else {
                errores.put("error", error.getDefaultMessage());
            }
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("codigo", "VALIDATION_ERROR");
        response.put("mensaje", "Error de validación en los datos enviados");
        response.put("errores", errores);
        response.put("hora", LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Manejo de recursos no encontrados (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErroresResponse> handleNotFound(ResourceNotFoundException ex) {
        ErroresResponse error = ErroresResponse.builder()
            .codigo("RESOURCE_NOT_FOUND")
            .mensaje(ex.getMessage())
            .hora(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Manejo de errores de autenticación (401)
    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<ErroresResponse> handleUnauthorized(AuthenticationException ex) {
        ErroresResponse error = ErroresResponse.builder()
            .codigo("UNAUTHORIZED")
            .mensaje(ex.getMessage())
            .hora(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // Manejo de acceso denegado (403)
    // NOTA: Este NO se ejecutará si CustomAccessDeniedHandler está activo en Security
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroresResponse> handleAccessDenied(AccessDeniedException ex) {
        ErroresResponse error = ErroresResponse.builder()
            .codigo("FORBIDDEN")
            .mensaje("No tiene permisos suficientes para acceder a este recurso")
            .hora(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    // Manejo de usuarios inválidos o no encontrados (401)
    @ExceptionHandler({UsernameNotFoundException.class, InvalidUserExeception.class})
    public ResponseEntity<ErroresResponse> handleInvalidUser(Exception ex) {
        ErroresResponse error = ErroresResponse.builder()
            .codigo("INVALID_USER")
            .mensaje(ex.getMessage())
            .hora(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // Manejo de RequestException (si tienes lógica específica para esta)
    @ExceptionHandler(RequestException.class)
    public ResponseEntity<ErroresResponse> handleRequestException(RequestException ex) {
        ErroresResponse error = ErroresResponse.builder()
            .codigo("REQUEST_ERROR")
            .mensaje(ex.getMessage())
            .hora(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Manejo genérico de RuntimeException (último recurso para errores no manejados)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroresResponse> handleRuntimeException(RuntimeException ex) {
        ErroresResponse error = ErroresResponse.builder()
            .codigo("INTERNAL_ERROR")
            .mensaje("Error interno del servidor: " + ex.getMessage())
            .hora(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // Manejo genérico de cualquier excepción no manejada (safety net)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroresResponse> handleGenericException(Exception ex) {
        ErroresResponse error = ErroresResponse.builder()
            .codigo("UNEXPECTED_ERROR")
            .mensaje("Error inesperado: " + ex.getMessage())
            .hora(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
