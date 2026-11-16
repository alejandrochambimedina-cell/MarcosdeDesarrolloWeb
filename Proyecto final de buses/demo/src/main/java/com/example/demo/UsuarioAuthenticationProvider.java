
package com.example.demo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UsuarioAuthenticationProvider implements AuthenticationProvider {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioAuthenticationProvider(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String correo = authentication.getName().trim().toLowerCase();
        String clave = authentication.getCredentials().toString();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new BadCredentialsException("Credenciales incorrectas"));

        String claveGuardada = usuario.getClaveHash();

        boolean esValida;

        if (esBCrypt(claveGuardada)) {
            esValida = passwordEncoder.matches(clave, claveGuardada);
        } else {
            esValida = verificarClaveAntigua(
                    clave, usuario.getSal(), claveGuardada);

            if (esValida) {
                usuario.actualizarClaveHash(passwordEncoder.encode(clave));
                usuarioRepository.save(usuario);
            }
        }

        if (!esValida) {
            throw new BadCredentialsException("Credenciales incorrectas");
        }

        UsuarioPrincipal principal = new UsuarioPrincipal(usuario);

        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                principal.getAuthorities()
        );
    }

    private boolean esBCrypt(String hash) {
        return hash != null &&
                (hash.startsWith("$2a$") ||
                 hash.startsWith("$2b$") ||
                 hash.startsWith("$2y$"));
    }

    private boolean verificarClaveAntigua(
            String clave, String sal, String hashGuardado) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] resultado = digest.digest(
                    (sal + clave).getBytes(StandardCharsets.UTF_8));

            String hashCalculado =
                    java.util.HexFormat.of().formatHex(resultado);

            return MessageDigest.isEqual(
                    hashCalculado.getBytes(StandardCharsets.UTF_8),
                    hashGuardado.getBytes(StandardCharsets.UTF_8));

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "SHA-256 no está disponible", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication);
    }
}
