package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Inicio de sesión, registro y cierre de sesión.
 */
@Controller
public class AuthController {

    /** Nombre del atributo de sesión donde se guarda al usuario logueado. */
    public static final String SESION_USUARIO = "usuario";

    private static final Pattern CORREO_VALIDO =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ---------- LOGIN ----------

    @GetMapping("/login")
    public String login(
            @RequestParam(value = "requerido", required = false) String requerido,
            HttpSession session,
            Model model) {

        if (session.getAttribute(SESION_USUARIO) != null) {
            return "redirect:/servicios";
        }
        if (requerido != null) {
            model.addAttribute("aviso", "Inicia sesión para continuar con tu reserva.");
        }
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("correo") String correo,
            @RequestParam("clave") String clave,
            HttpServletRequest request,
            Model model) {

        Usuario usuario = usuarioService.autenticar(correo, clave);

        if (usuario == null) {
            model.addAttribute("error", "Correo o contraseña incorrectos.");
            model.addAttribute("correo", correo);
            return "login";
        }

        HttpSession session = request.getSession(true);
        request.changeSessionId(); // evita la fijación de sesión
        session.setAttribute(SESION_USUARIO, usuario);

        // Al entrar, el usuario aterriza en Servicios, donde ya verá el menú completo.
        return "redirect:/servicios";
    }

    // ---------- REGISTRO ----------

    @GetMapping("/registro")
    public String registro(HttpSession session) {
        if (session.getAttribute(SESION_USUARIO) != null) {
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
        redirect.addFlashAttribute("exito", "Cuenta creada correctamente. Ya puedes iniciar sesión.");
        return "redirect:/login";
    }

    // ---------- LOGOUT ----------

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/inicio";
    }
}
