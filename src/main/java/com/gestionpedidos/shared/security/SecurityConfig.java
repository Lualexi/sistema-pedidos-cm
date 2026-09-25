package com.gestionpedidos.shared.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas: Auth, Recursos Estáticos, Vistas de Páginas y Manejo de Errores
                .requestMatchers("/api/auth/**", "/pages/**", "/css/**", "/js/**", "/index.html", "/", "/error").permitAll()
                
                // Reglas por Rol en Endpoints REST
                .requestMatchers(HttpMethod.POST, "/api/products/**").hasAuthority("ADMIN_ALMACEN")
                .requestMatchers(HttpMethod.PUT, "/api/products/**").hasAuthority("ADMIN_ALMACEN")
                .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasAuthority("ADMIN_ALMACEN")
                .requestMatchers("/api/orders/**").hasAnyAuthority("VENDEDOR", "ADMIN_ALMACEN")
                
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
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