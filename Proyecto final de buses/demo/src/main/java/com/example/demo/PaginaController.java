package com.example.demo;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Páginas del sitio.
 *
 * Públicas:   /inicio, /servicios
 * Con login:  /buses, /reserva, /boleto  (si no hay sesión, se envía a /login)
 *
 * Flujo de compra: Servicios -> Buses -> Reserva -> Boleto (+ datos del pasajero).
 */
@Controller
public class PaginaController {

    private static final String REDIRIGIR_A_LOGIN = "redirect:/login?requerido";

    // ---------- Páginas públicas ----------

    @GetMapping({"/", "/inicio"})
    public String inicio(Model model) {
        model.addAttribute("tituloHero", "Tu viaje soñado empieza con");
        model.addAttribute("mensajeHero",
                "Conectamos los principales destinos del país con el máximo confort, "
                + "seguridad y los mejores precios del mercado. Reserva tu pasaje en minutos.");
        return "inicio";
    }

    @GetMapping("/servicios")
    public String servicios() {
        return "servicios";
    }

    // ---------- Páginas que requieren sesión ----------

    @GetMapping("/buses")
    public String buses(HttpSession session) {
        return requiereSesion(session) ? "buses" : REDIRIGIR_A_LOGIN;
    }

    @GetMapping("/reserva")
    public String reserva(HttpSession session) {
        return requiereSesion(session) ? "reserva" : REDIRIGIR_A_LOGIN;
    }

    @GetMapping("/boleto")
    public String boleto(HttpSession session) {
        return requiereSesion(session) ? "boleto" : REDIRIGIR_A_LOGIN;
    }

    /** La antigua página de pasajero ahora es el último paso dentro del boleto. */
    @GetMapping("/pasajero")
    public String pasajero() {
        return "redirect:/boleto";
    }

    private boolean requiereSesion(HttpSession session) {
        return session.getAttribute(AuthController.SESION_USUARIO) != null;
    }
}
