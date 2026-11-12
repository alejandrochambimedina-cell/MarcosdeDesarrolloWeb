
package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        @Order(1)
        public SecurityFilterChain apiSecurityFilterChain(
                        HttpSecurity http) throws Exception {

                http
                                .securityMatcher("/api/**")
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(HttpMethod.GET, "/api/**")
                                                .hasAnyRole("USER", "ADMIN")
                                                .requestMatchers(HttpMethod.HEAD, "/api/**")
                                                .hasAnyRole("USER", "ADMIN")
                                                .anyRequest().denyAll())
                                .httpBasic(Customizer.withDefaults())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .csrf(csrf -> csrf.disable());

                return http.build();
        }

        @Bean
        @Order(2)
        public SecurityFilterChain webSecurityFilterChain(
                        HttpSecurity http) throws Exception {

                http

                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/login",
                                                                "/error",
                                                                "/css/**",
                                                                "/js/**",
                                                                "/Imagenes/**",
                                                                "/portal")
                                                .permitAll()

                                                // Páginas administrativas: solo ADMIN
                                                .requestMatchers(HttpMethod.GET,
                                                                "/cursos/nuevo",
                                                                "/cursos/{id}/editar")
                                                .hasRole("ADMIN")

                                                // Operaciones de escritura: solo ADMIN
                                                .requestMatchers(HttpMethod.POST,
                                                                "/cursos",
                                                                "/cursos/{id}",
                                                                "/cursos/{id}/eliminar")
                                                .hasRole("ADMIN")

                                                // Consultas públicas del catálogo
                                                .requestMatchers(HttpMethod.GET,
                                                                "/", "/cursos", "/cursos/resumen", "/cursos/{id}")
                                                .permitAll()

                                                .requestMatchers("/tasks", "/users")
                                                .authenticated()

                                                .anyRequest().authenticated())

                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/cursos")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll());

                return http.build();
        }
}
