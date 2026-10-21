package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Pone el usuario logueado (si lo hay) disponible en TODAS las plantillas
 * como ${usuarioActual}. Así el menú puede cambiar según haya sesión o no.
 */
@ControllerAdvice
public class SesionAdvice {

    @ModelAttribute("usuarioActual")
    public Usuario usuarioActual(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (Usuario) session.getAttribute(AuthController.SESION_USUARIO);
    }
}
