
package com.example.demo;

import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;

        // Crear el usuario de prueba solo si todavía no existe.
        if (!existeCorreo("demo@rutaperu.com")) {
            registrar("Usuario", "Demo", "demo@rutaperu.com", "demo123");
        }
    }

    public boolean existeCorreo(String correo) {
        return usuarioRepository.existsByCorreo(normalizar(correo));
    }

    public Usuario registrar(
            String nombres,
            String apellidos,
            String correo,
            String clave) {

        String sal = generarSal();

        Usuario usuario = new Usuario(
                nombres.trim(),
                apellidos.trim(),
                normalizar(correo),
                sal,
                passwordEncoder.encode(clave));

        return usuarioRepository.save(usuario);
    }

    private String normalizar(String correo) {
        return correo == null
                ? ""
                : correo.trim().toLowerCase(Locale.ROOT);
    }

    private String generarSal() {
        // Se conserva el campo sal por compatibilidad con las cuentas antiguas.
        // BCrypt genera y almacena su propia sal dentro del hash.
        byte[] bytes = new byte[16];
        new java.security.SecureRandom().nextBytes(bytes);

        return java.util.HexFormat.of().formatHex(bytes);
    }

    public Usuario asignarRolAdmin(String correo) {

        Usuario usuario = usuarioRepository.findByCorreo(normalizar(correo))
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe."));

        usuario.actualizarRol("ADMIN");

        return usuarioRepository.save(usuario);
    }
}
