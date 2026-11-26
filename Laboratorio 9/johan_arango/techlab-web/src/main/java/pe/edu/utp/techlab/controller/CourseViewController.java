package pe.edu.utp.techlab.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.utp.techlab.dto.CourseDto;
import pe.edu.utp.techlab.service.CourseService;

@Controller
@RequestMapping("/cursos")
public class CourseViewController {

    private static final int MAX_QUERY_LENGTH = 60;
    private static final int MIN_HORAS_LIMITE = 0;
    private static final int MAX_HORAS_LIMITE = 200;

    private final CourseService service;

    public CourseViewController(CourseService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(required = false) String minHoras,
            Model model) {

        String consulta = normalizarConsulta(q);
        Integer horasMinimas = validarMinHoras(minHoras);

        model.addAttribute("cursos", service.buscar(consulta, horasMinimas));
        model.addAttribute("q", consulta);
        model.addAttribute("minHoras", minHoras != null ? minHoras.strip() : "");
        return "cursos/lista";
    }

    @GetMapping("/resumen")
    public String resumen(Model model) {
        model.addAttribute("cantidadCursos", service.cantidad());
        model.addAttribute("totalHoras", service.totalHoras());
        return "cursos/resumen";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable long id, Model model) {
        CourseDto curso = service.buscarPorId(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Curso no encontrado"));
        model.addAttribute("curso", curso);
        return "cursos/detalle";
    }

    private String normalizarConsulta(String q) {
        String consulta = q.strip();
        if (consulta.length() > MAX_QUERY_LENGTH) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "La búsqueda admite hasta 60 caracteres");
        }
        return consulta;
    }

    private Integer validarMinHoras(String minHoras) {
        if (minHoras == null || minHoras.isBlank()) {
            return null;
        }
        try {
            int valor = Integer.parseInt(minHoras.strip());
            if (valor < MIN_HORAS_LIMITE || valor > MAX_HORAS_LIMITE) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Las horas mínimas deben estar entre 0 y 200");
            }
            return valor;
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Las horas mínimas deben ser un número entero");
        }
    }
}
