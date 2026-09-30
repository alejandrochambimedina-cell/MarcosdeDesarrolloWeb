package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

    @GetMapping("/")
    public String raiz(Model model) {
        model.addAttribute("tituloHero", "Tu viaje soñado empieza con RutaPerú");
        model.addAttribute("mensajeHero", "Conectamos los principales destinos del país con el máximo confort, seguridad y los mejores precios del mercado. Reserva tu pasaje en minutos.");
        return "inicio";
    }

    @GetMapping("/inicio")
    public String inicio(Model model) {
        model.addAttribute("tituloHero", "Tu viaje soñado empieza con RutaPerú");
        model.addAttribute("mensajeHero", "Conectamos los principales destinos del país con el máximo confort, seguridad y los mejores precios del mercado. Reserva tu pasaje en minutos.");
        return "inicio";
    }

    // Rutas de tu apartado: Login y Registro
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    // Rutas de tus compañeros
    @GetMapping("/buses")
    public String buses() {
        return "buses";
    }

    @GetMapping("/reserva")
    public String reserva() {
        return "reserva";
    }

    @GetMapping("/servicios")
    public String servicios() {
        return "servicios";
    }

    @GetMapping("/pasajero")
    public String pasajero() {
        return "pasajero";
    }

    @GetMapping("/boleto")
    public String boleto() {
        return "boleto";
    }
}