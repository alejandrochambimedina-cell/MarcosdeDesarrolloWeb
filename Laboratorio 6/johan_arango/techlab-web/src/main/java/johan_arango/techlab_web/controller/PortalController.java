package johan_arango.techlab_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PortalController {

    @GetMapping("/portal")
    public String catalogo() {
        return "redirect:/catalogo.html";
    }

    @GetMapping("/inicio")
    public String inicio() {
        return "redirect:/";
    }
}