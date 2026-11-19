package com.example.demo.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

import com.example.demo.security.dto.LoginRequest;
import com.example.demo.security.dto.TokenResponse;

@Service
public class TokenService {

    private static final long TOKEN_DURATION_SECONDS = 600;

    private final AuthenticationProvider authenticationProvider;
    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final String audience;

    public TokenService(
            AuthenticationProvider authenticationProvider,
            JwtEncoder jwtEncoder,
            @Value("${app.jwt.issuer}") String issuer,
            @Value("${app.jwt.audience}") String audience) {

        this.authenticationProvider = authenticationProvider;
        this.jwtEncoder = jwtEncoder;
        this.issuer = issuer;
        this.audience = audience;
    }

    public TokenResponse issue(LoginRequest request)
            throws AuthenticationException {

        String username = request.username();
        String password = request.password();

        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        Authentication authentication =
                authenticationProvider.authenticate(
                        UsernamePasswordAuthenticationToken.unauthenticated(
                                username, password));

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        List<String> roles = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .filter(role -> role.equals("ROLE_USER")
                        || role.equals("ROLE_ADMIN"))
                .distinct()
                .toList();

        if (roles.isEmpty()) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        Instant issuedAt = Instant.now().truncatedTo(
                java.time.temporal.ChronoUnit.SECONDS);

        Instant expiresAt = issuedAt.plusSeconds(TOKEN_DURATION_SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .subject(authentication.getName())
                .audience(List.of(audience))
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .id(UUID.randomUUID().toString())
                .claim("roles", roles)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256)
                .type("JWT")
                .build();

        String token = jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims))
                .getTokenValue();

        return new TokenResponse(
                token,
                "Bearer",
                TOKEN_DURATION_SECONDS);
    }
}
