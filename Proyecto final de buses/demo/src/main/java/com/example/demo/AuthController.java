
package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador de inicio de sesión y registro.
 * Spring Security gestiona la autenticación y el cierre de sesión.
 */
@Controller
public class AuthController {

    // Se conserva temporalmente para adaptar los demás controladores.
    public static final String SESION_USUARIO = "usuario";

    private static final Pattern CORREO_VALIDO =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    private boolean usuarioAutenticado() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

    // ---------- LOGIN ----------

    @GetMapping("/login")
    public String login(
            @RequestParam(value = "requerido", required = false) String requerido,
            @RequestParam(value = "error", required = false) String error,
            Model model) {

        if (usuarioAutenticado()) {
            return "redirect:/servicios";
        }

        if (requerido != null) {
            model.addAttribute(
                    "aviso",
                    "Inicia sesión para continuar con tu reserva.");
        }

        if (error != null) {
            model.addAttribute(
                    "error",
                    "Correo o contraseña incorrectos.");
        }

        return "login";
    }

    // ---------- REGISTRO ----------

    @GetMapping("/registro")
    public String registro() {
        if (usuarioAutenticado()) {
            return "redirect:/servicios";
        }

        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam("nombres") String nombres,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("correo") String correo,
            @RequestParam("clave") String clave,
            @RequestParam("confirmarClave") String confirmarClave,
            Model model,
            RedirectAttributes redirect) {

        List<String> errores = new ArrayList<>();

        if (nombres.trim().length() < 2) {
            errores.add("Ingresa tus nombres.");
        }

        if (apellidos.trim().length() < 2) {
            errores.add("Ingresa tus apellidos.");
        }

        if (!CORREO_VALIDO.matcher(correo.trim()).matches()) {
            errores.add("Ingresa un correo electrónico válido.");
        } else if (usuarioService.existeCorreo(correo)) {
            errores.add("Ya existe una cuenta con ese correo.");
        }

        if (clave.length() < 6) {
            errores.add("La contraseña debe tener al menos 6 caracteres.");
        }

        if (!clave.equals(confirmarClave)) {
            errores.add("Las contraseñas no coinciden.");
        }

        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            model.addAttribute("nombres", nombres);
            model.addAttribute("apellidos", apellidos);
            model.addAttribute("correo", correo);

            return "registro";
        }

        usuarioService.registrar(nombres, apellidos, correo, clave);

        redirect.addFlashAttribute(
                "exito",
                "Cuenta creada correctamente. Ya puedes iniciar sesión.");

        return "redirect:/login";
    }
}
