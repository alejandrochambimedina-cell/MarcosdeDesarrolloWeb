
package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthApiController(
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(
            @Valid @RequestBody LoginSolicitud solicitud) {

        try {
            Authentication autenticacion =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    solicitud.correo(),
                                    solicitud.clave()));

            UsuarioPrincipal principal =
                    (UsuarioPrincipal) autenticacion.getPrincipal();

            String token = jwtService.generarToken(principal);

            return ResponseEntity.ok(
                    new LoginRespuesta(
                            token,
                            "Bearer",
                            jwtService.obtenerDuracion()));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Correo o contraseña incorrectos.");
        }
    }

    public record LoginSolicitud(
            @NotBlank(message = "El correo es obligatorio.")
            @Email(message = "El correo no es válido.")
            String correo,

            @NotBlank(message = "La contraseña es obligatoria.")
            String clave) {
    }

    public record LoginRespuesta(
            String token,
            String tipo,
            long expiraEn) {
    }
}
