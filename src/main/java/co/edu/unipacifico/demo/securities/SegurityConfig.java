package co.edu.unipacifico.demo.securities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import co.edu.unipacifico.demo.services.UsuariosService;
import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SegurityConfig {

    private final UsuariosService usuariosService;

    @Bean
    public PasswordEncoder codificarClave() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) 
            throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain securityChain(HttpSecurity http,
                                             AuthenticationManager authManager) 
        throws Exception {
        
        // Uso de lambdas para configuración moderna
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF (Típico para APIs REST/JWT)

            .authorizeHttpRequests(auth -> auth
                // Rutas públicas (login, registro, etc.)
                .requestMatchers("/api/auth/**").permitAll() 
                
                // Rutas con control de roles
                // Requiere que el rol en tu DB sea "ADMIN" o "USER" (sin el prefijo ROLE_)
                .requestMatchers("/api/parqueadero/**").hasAnyRole("ADMIN", "USER") 
                
                // Cualquier otra solicitud requiere autenticación
                .anyRequest().authenticated() 
            )
            .authenticationManager(authManager)
            // Configuración de la autenticación
            .userDetailsService(usuariosService) // Conecta tu UsuariosService
            
            // Habilita el inicio de sesión con formulario (puedes usar JWT en su lugar)
            .formLogin(form -> form.permitAll()) 
            
            // Habilita la autenticación básica HTTP (opcional)
            .httpBasic(httpBasic -> {}); 

        return http.build();
    }
}
