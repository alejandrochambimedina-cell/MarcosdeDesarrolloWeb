
package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MisReservasController {

    private final ReservaService reservaService;

    public MisReservasController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping("/mis-reservas")
    public String misReservas(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute(
                AuthController.SESION_USUARIO);

        if (usuario == null) {
            return "redirect:/login?requerido";
        }

        model.addAttribute(
                "reservas",
                reservaService.listarPorUsuario(usuario.getId()));

        return "mis-reservas";
    }
}
