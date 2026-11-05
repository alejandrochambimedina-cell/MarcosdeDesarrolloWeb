package pe.edu.utp.techlab.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.utp.techlab.service.CourseService;

@Controller
@RequestMapping("/cursos")
public class CourseViewController {
    private final CourseService service;
    public CourseViewController(CourseService service) { this.service = service; }

    @GetMapping
    public String listado(@RequestParam(defaultValue="") String q, Model model) {
        if (q.length() > 60) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        model.addAttribute("q", q);
        model.addAttribute("cursos", service.buscar(q));
        return "cursos/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable long id, Model model) {
        var curso = service.buscarPorId(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("curso", curso);
        return "cursos/detalle";
    }

    @GetMapping("/resumen")
    public String resumen(Model model) {
        model.addAttribute("cantidadCursos", service.cantidad());
        model.addAttribute("totalHoras", service.totalHoras());
        return "cursos/resumen";
    }
}
