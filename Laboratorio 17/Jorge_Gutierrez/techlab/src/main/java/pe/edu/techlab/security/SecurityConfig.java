package pe.edu.techlab.security;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.server.resource.authentication.*;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfig {
    static final String ISSUER="techlab", AUDIENCE="techlab-api";
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean UserDetailsService users(PasswordEncoder encoder,
        @Value("${techlab.user-password}") String userPassword,
        @Value("${techlab.admin-password}") String adminPassword) {
        if (userPassword.length()<12 || adminPassword.length()<12 || userPassword.equals(adminPassword))
            throw new IllegalArgumentException("Use contraseñas distintas de al menos 12 caracteres");
        return new InMemoryUserDetailsManager(
            User.withUsername("user").password(encoder.encode(userPassword)).roles("USER").build(),
            User.withUsername("admin").password(encoder.encode(adminPassword)).roles("USER","ADMIN").build());
    }
    @Bean AuthenticationManager authManager(UserDetailsService users, PasswordEncoder encoder) {
        var provider=new org.springframework.security.authentication.dao.DaoAuthenticationProvider(users);
        provider.setPasswordEncoder(encoder);
        return new ProviderManager(provider);
    }
    @Bean SecretKey jwtKey(@Value("${techlab.jwt-secret}") String encoded) {
        byte[] bytes=Base64.getDecoder().decode(encoded);
        if(bytes.length<32) throw new IllegalArgumentException("JWT requiere al menos 32 bytes");
        return new SecretKeySpec(bytes,"HmacSHA256");
    }
    @Bean JwtEncoder jwtEncoder(SecretKey key) {
        return NimbusJwtEncoder.withSecretKey(key).algorithm(MacAlgorithm.HS256).build();
    }
    @Bean JwtDecoder jwtDecoder(SecretKey key) {
        var decoder=NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
        OAuth2TokenValidator<Jwt> audience=jwt -> jwt.getAudience()!=null && jwt.getAudience().contains(AUDIENCE)
            ? OAuth2TokenValidatorResult.success()
            : OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token","Audiencia inválida",null));
        OAuth2TokenValidator<Jwt> required=jwt -> jwt.getExpiresAt()!=null && jwt.getIssuedAt()!=null &&
            jwt.getSubject()!=null && !jwt.getSubject().isBlank()
            ? OAuth2TokenValidatorResult.success()
            : OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token","Claims obligatorios ausentes",null));
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
            JwtValidators.createDefaultWithIssuer(ISSUER),audience,required));
        return decoder;
    }
    @Bean @Order(1) SecurityFilterChain api(HttpSecurity http) throws Exception {
        var roles=new JwtGrantedAuthoritiesConverter();
        roles.setAuthoritiesClaimName("roles"); roles.setAuthorityPrefix("ROLE_");
        var converter=new JwtAuthenticationConverter(); converter.setJwtGrantedAuthoritiesConverter(roles);
        return http.securityMatcher("/api/**")
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a -> a
                .requestMatchers(HttpMethod.POST,"/api/auth/token").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/courses","/api/courses/**").hasRole("USER")
                .requestMatchers("/api/courses","/api/courses/**").hasRole("ADMIN")
                .anyRequest().denyAll())
            .exceptionHandling(e -> e.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint()))
            .oauth2ResourceServer(o -> o.jwt(j -> j.jwtAuthenticationConverter(converter)))
            .build();
    }
    @Bean @Order(2) SecurityFilterChain web(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(a -> a
                .requestMatchers("/css/**","/js/**","/login","/error").permitAll()
                .requestMatchers("/courses/new","/courses/*/edit").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,"/courses","/courses/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/","/courses").hasRole("USER")
                .anyRequest().denyAll())
            .formLogin(Customizer.withDefaults())
            .logout(l -> l.logoutSuccessUrl("/login"))
            .build();
    }
}
