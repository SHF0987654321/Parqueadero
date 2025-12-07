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

import co.edu.unipacifico.demo.dtos.ErroresDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Manejo de operaciones inválidas y errores de parámetros
    @ExceptionHandler({InvalidOperationExeception.class, ParameterException.class})
    public ResponseEntity<ErroresDTO> handleBadRequest(Exception ex) {
        ErroresDTO errorDTO = new ErroresDTO(
            ex instanceof InvalidOperationExeception ? "INVALID_OPERATION" : "PARAMETER_ERROR",
            ex.getMessage(), 
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    // Manejo de errores de base de datos
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ErroresDTO> handleDatabaseError(DatabaseException ex) {
        ErroresDTO errorDTO = new ErroresDTO(
            "DB_INTERNAL_ERROR", 
            "Fallo en la capa de persistencia: " + ex.getMessage(), 
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
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
    @ExceptionHandler(ResouseNotFoundException.class)
    public ResponseEntity<ErroresDTO> handleNotFound(ResouseNotFoundException ex) {
        ErroresDTO errorDTO = new ErroresDTO(
            "RESOURCE_NOT_FOUND", 
            ex.getMessage(), 
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }

    // Manejo de errores de autenticación (401)
    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<ErroresDTO> handleUnauthorized(AuthenticationException ex) {
        ErroresDTO errorDTO = new ErroresDTO(
            "UNAUTHORIZED", 
            ex.getMessage(), 
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDTO);
    }

    // Manejo de acceso denegado (403)
    // NOTA: Este NO se ejecutará si CustomAccessDeniedHandler está activo en Security
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroresDTO> handleAccessDenied(AccessDeniedException ex) {
        ErroresDTO errorDTO = new ErroresDTO(
            "FORBIDDEN", 
            "No tiene permisos suficientes para acceder a este recurso", 
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDTO);
    }

    // Manejo de usuarios inválidos o no encontrados (401)
    @ExceptionHandler({UsernameNotFoundException.class, InvalidUserExeception.class})
    public ResponseEntity<ErroresDTO> handleInvalidUser(Exception ex) {
        ErroresDTO errorDTO = new ErroresDTO(
            "INVALID_USER", 
            ex.getMessage(), 
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDTO);
    }

    // Manejo de RequestException (si tienes lógica específica para esta)
    @ExceptionHandler(RequestException.class)
    public ResponseEntity<ErroresDTO> handleRequestException(RequestException ex) {
        ErroresDTO errorDTO = new ErroresDTO(
            "REQUEST_ERROR", 
            ex.getMessage(), 
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    // Manejo genérico de RuntimeException (último recurso para errores no manejados)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroresDTO> handleRuntimeException(RuntimeException ex) {
        ErroresDTO errorDTO = new ErroresDTO(
            "INTERNAL_ERROR", 
            "Error interno del servidor: " + ex.getMessage(), 
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }

    // Manejo genérico de cualquier excepción no manejada (safety net)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroresDTO> handleGenericException(Exception ex) {
        ErroresDTO errorDTO = new ErroresDTO(
            "UNEXPECTED_ERROR", 
            "Error inesperado: " + ex.getMessage(), 
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }
}
