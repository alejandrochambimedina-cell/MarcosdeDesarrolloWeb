
package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

@Controller
public class MisReservasController {

    private final ReservaService reservaService;

    public MisReservasController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping("/mis-reservas")
    public String misReservas(Authentication authentication, Model model) {

        UsuarioPrincipal principal =
                (UsuarioPrincipal) authentication.getPrincipal();

        Usuario usuario = principal.getUsuario();

        model.addAttribute(
                "reservas",
                reservaService.listarPorUsuario(usuario.getId()));

        return "mis-reservas";
    }
}
