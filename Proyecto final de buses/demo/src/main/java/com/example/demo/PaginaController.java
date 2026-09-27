package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

    @GetMapping("/")
    public String raiz() {
        return "inicio";
    }

    @GetMapping("/inicio")
    public String inicio() {
        return "inicio";
    }

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
