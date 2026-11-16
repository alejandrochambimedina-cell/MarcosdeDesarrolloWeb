
package com.example.demo;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration) {

        this.secretKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secret));

        this.expiration = expiration;
    }

    public String generarToken(UsuarioPrincipal principal) {

        Date ahora = new Date();
        Date vencimiento = new Date(
                ahora.getTime() + expiration);

        return Jwts.builder()
                .subject(principal.getUsername())
                .claim("rol", principal.getUsuario().getRol())
                .issuedAt(ahora)
                .expiration(vencimiento)
                .signWith(secretKey)
                .compact();
    }

    public String obtenerCorreo(String token) {
        return obtenerClaims(token).getSubject();
    }

    public boolean esTokenValido(String token) {
        try {
            obtenerClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims obtenerClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long obtenerDuracion() {
        return expiration;
    }
}
