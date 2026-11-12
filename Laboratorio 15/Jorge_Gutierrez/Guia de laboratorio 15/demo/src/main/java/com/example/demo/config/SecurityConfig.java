package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService users,
            PasswordEncoder encoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(users);

        provider.setPasswordEncoder(encoder);

        return provider;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(
            HttpSecurity http,
            JwtDecoder decoder,
            JwtAuthenticationConverter converter) throws Exception {

        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth

                        // Solicitud de token: pública
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/auth/token")
                        .permitAll()

                        // Consultas: USER y ADMIN
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/cursos",
                                "/api/v1/cursos/**")
                        .hasAnyRole("USER", "ADMIN")

                        .requestMatchers(
                                HttpMethod.HEAD,
                                "/api/v1/cursos",
                                "/api/v1/cursos/**")
                        .hasAnyRole("USER", "ADMIN")

                        // Escritura: solamente ADMIN
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/cursos",
                                "/api/v1/cursos/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/v1/cursos",
                                "/api/v1/cursos/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/v1/cursos",
                                "/api/v1/cursos/**")
                        .hasRole("ADMIN")

                        // Todo lo demás queda bloqueado
                        .anyRequest().denyAll())

                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(decoder)
                                .jwtAuthenticationConverter(converter)))

                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(
            HttpSecurity http,
            AuthenticationProvider provider) throws Exception {

        http
                .authenticationProvider(provider)

                .authorizeHttpRequests(auth -> auth

                        // Recursos y páginas públicas
                        .requestMatchers(
                                "/",
                                "/login",
                                "/error",
                                "/css/**",
                                "/js/**",
                                "/Imagenes/**",
                                "/portal")
                        .permitAll()

                        // Páginas administrativas
                        .requestMatchers(
                                HttpMethod.GET,
                                "/cursos/nuevo",
                                "/cursos/*/editar")
                        .hasRole("ADMIN")

                        // Operaciones de escritura
                        .requestMatchers(
                                HttpMethod.POST,
                                "/cursos",
                                "/cursos/**")
                        .hasRole("ADMIN")

                        // Consultas públicas del catálogo
                        .requestMatchers(
                                HttpMethod.GET,
                                "/cursos",
                                "/cursos/",
                                "/cursos/resumen",
                                "/cursos/*")
                        .permitAll()

                        .requestMatchers("/tasks", "/users")
                        .authenticated()

                        .anyRequest().authenticated())

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/cursos")
                        .failureUrl("/login?error")
                        .permitAll())

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())

                .sessionManagement(session -> session
                        .sessionFixation(fixation ->
                                fixation.changeSessionId()));

        // CSRF permanece habilitado para la aplicación web.
        return http.build();
    }
}
