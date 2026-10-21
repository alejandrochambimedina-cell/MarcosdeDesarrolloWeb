package com.example.demo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

/**
 * Registro y autenticación de usuarios (almacenados en memoria).
 * Las contraseñas nunca se guardan en texto plano: se guarda un hash con sal.
 */
@Service
public class UsuarioService {

    private final Map<String, Usuario> usuarios = new ConcurrentHashMap<>();
    private final SecureRandom aleatorio = new SecureRandom();

    public UsuarioService() {
        // Cuenta de prueba para la demostración (se pierde al reiniciar el servidor).
        registrar("Usuario", "Demo", "demo@rutaperu.com", "demo123");
    }

    public boolean existeCorreo(String correo) {
        return usuarios.containsKey(normalizar(correo));
    }

    public Usuario registrar(String nombres, String apellidos, String correo, String clave) {
        String sal = generarSal();
        Usuario usuario = new Usuario(
                nombres.trim(),
                apellidos.trim(),
                normalizar(correo),
                sal,
                hash(clave, sal));
        usuarios.put(usuario.getCorreo(), usuario);
        return usuario;
    }

    /** Devuelve el usuario si las credenciales son correctas; si no, null. */
    public Usuario autenticar(String correo, String clave) {
        Usuario usuario = usuarios.get(normalizar(correo));
        if (usuario == null) {
            return null;
        }
        byte[] esperado = usuario.getClaveHash().getBytes(StandardCharsets.UTF_8);
        byte[] recibido = hash(clave, usuario.getSal()).getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(esperado, recibido) ? usuario : null;
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
            byte[] resultado = digest.digest((sal + clave).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(resultado);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no está disponible", e);
        }
    }
}
