package co.edu.unipacifico.demo.configs;

import co.edu.unipacifico.demo.services.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos (no requieren autenticación)
                .requestMatchers("/api/auth/**").permitAll()
                
                // Endpoints de LUGARES - Todos pueden ver estadísticas
                .requestMatchers(HttpMethod.GET, "/api/lugares/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/lugares/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/lugares/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/lugares/**").hasRole("ADMIN")
                
                // Endpoints de MOVIMIENTOS - Solo ADMIN puede gestionar entradas/salidas
                .requestMatchers(HttpMethod.POST, "/api/movimientos/entrada").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/movimientos/salida/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/movimientos/**").hasAnyRole("USER", "ADMIN")
                
                // Endpoints de VEHÍCULOS - Todos pueden ver, solo ADMIN puede modificar
                .requestMatchers(HttpMethod.GET, "/api/vehiculos/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/vehiculos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/vehiculos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/vehiculos/**").hasRole("ADMIN")
                
                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200", "http://localhost:8000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}