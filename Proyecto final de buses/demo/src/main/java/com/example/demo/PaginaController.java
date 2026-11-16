
package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de las páginas de RutaPerú.
 *
 * Públicas: /inicio, /servicios
 * Protegidas por Spring Security: /buses, /reserva, /boleto
 */
@Controller
public class PaginaController {

    // ---------- Páginas públicas ----------

    @GetMapping({"/", "/inicio"})
    public String inicio(Model model) {

        model.addAttribute(
                "tituloHero",
                "Tu viaje soñado empieza con");

        model.addAttribute(
                "mensajeHero",
                "Conectamos los principales destinos del país con el máximo confort, "
                + "seguridad y los mejores precios del mercado. Reserva tu pasaje en minutos.");

        return "inicio";
    }

    @GetMapping("/servicios")
    public String servicios() {
        return "servicios";
    }

    // ---------- Páginas protegidas ----------

    @GetMapping("/buses")
    public String buses() {
        return "buses";
    }

    @GetMapping("/reserva")
    public String reserva() {
        return "reserva";
    }

    @GetMapping("/boleto")
    public String boleto() {
        return "boleto";
    }

    /** La antigua página de pasajero ahora es el último paso dentro del boleto. */
    @GetMapping("/pasajero")
    public String pasajero() {
        return "redirect:/boleto";
    }
}
