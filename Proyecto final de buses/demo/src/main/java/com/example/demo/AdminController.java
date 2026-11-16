
package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/prueba")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public String pruebaAdmin() {
        return "Acceso permitido. El usuario tiene el rol ADMIN.";
    }
}
