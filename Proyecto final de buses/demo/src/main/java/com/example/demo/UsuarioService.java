package com.example.demo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

private final UsuarioRepository usuarioRepository;
private final SecureRandom aleatorio = new SecureRandom();

public UsuarioService(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;

    // Crear el usuario de prueba solo si todavía no existe.
    if (!existeCorreo("demo@rutaperu.com")) {
        registrar("Usuario", "Demo", "demo@rutaperu.com", "demo123");
    }
}

public boolean existeCorreo(String correo) {
    return usuarioRepository.existsByCorreo(normalizar(correo));
}

public Usuario registrar(String nombres, String apellidos,
                         String correo, String clave) {
    String sal = generarSal();

    Usuario usuario = new Usuario(
            nombres.trim(),
            apellidos.trim(),
            normalizar(correo),
            sal,
            hash(clave, sal));

    return usuarioRepository.save(usuario);
}

/** Devuelve el usuario si las credenciales son correctas; si no, null. */
public Usuario autenticar(String correo, String clave) {
    Usuario usuario = usuarioRepository
            .findByCorreo(normalizar(correo))
            .orElse(null);

    if (usuario == null) {
        return null;
    }

    byte[] esperado = usuario.getClaveHash()
            .getBytes(StandardCharsets.UTF_8);
    byte[] recibido = hash(clave, usuario.getSal())
            .getBytes(StandardCharsets.UTF_8);

    return MessageDigest.isEqual(esperado, recibido)
            ? usuario : null;
}

private String normalizar(String correo) {
    return correo == null ? "" : correo.trim().toLowerCase();
}

private String generarSal() {
    byte[] bytes = new byte[16];
    aleatorio.nextBytes(bytes);
    return HexFormat.of().formatHex(bytes);
}

private String hash(String clave, String sal) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] resultado = digest.digest(
                (sal + clave).getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(resultado);
    } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException(
                "SHA-256 no está disponible", e);
    }
}

}