
package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.demo.JwtAuthenticationFilter;
import com.example.demo.UsuarioAuthenticationProvider;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(
                        UsuarioAuthenticationProvider authenticationProvider) {
                return new ProviderManager(authenticationProvider);
        }

        // Primera cadena: API protegida con JWT.
        @Bean
        @Order(1)
        public SecurityFilterChain apiSecurityFilterChain(
                        HttpSecurity http,
                        AuthenticationManager authenticationManager,
                        JwtAuthenticationFilter jwtAuthenticationFilter)
                        throws Exception {

                http
                                .securityMatcher("/api/**")
                                .authenticationManager(authenticationManager)
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/auth/login").permitAll()
                                                .anyRequest().authenticated())
                                .exceptionHandling(ex -> ex
                                                .authenticationEntryPoint(
                                                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                                .addFilterBefore(
                                                jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        // Segunda cadena: páginas web con sesiones tradicionales.
        @Bean
        @Order(2)
        public SecurityFilterChain webSecurityFilterChain(
                        HttpSecurity http,
                        AuthenticationManager authenticationManager)
                        throws Exception {

                http
                                .authenticationManager(authenticationManager)
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/h2-console/**"))
                                .headers(headers -> headers
                                                .frameOptions(frame -> frame.sameOrigin()))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/",
                                                                "/inicio",
                                                                "/servicios",
                                                                "/login",
                                                                "/registro",
                                                                "/css/**",
                                                                "/js/**",
                                                                "/CSS/**",
                                                                "/JS/**",
                                                                "/imagenes/**",
                                                                "/images/**",
                                                                "/h2-console/**")
                                                .permitAll()
                                                .requestMatchers("/admin/**")
                                                .hasRole("ADMIN")
                                                .anyRequest()
                                                .hasAnyRole("USER", "ADMIN"))
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .usernameParameter("correo")
                                                .passwordParameter("clave")
                                                .defaultSuccessUrl("/servicios", true)
                                                .failureUrl("/login?error")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/inicio")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID"));

                return http.build();
        }
}
