package pe.edu.utp.techlab.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.utp.techlab.dto.CourseDto;
import pe.edu.utp.techlab.dto.CourseInputDto;
import pe.edu.utp.techlab.exception.DuplicateCourseException;
import pe.edu.utp.techlab.service.CourseService;

@Controller
@RequestMapping("/cursos")
public class CourseViewController {
    private static final int MAX_QUERY_LENGTH = 60;
    private final CourseService service;

    public CourseViewController(CourseService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(@RequestParam(defaultValue = "") String q, Model model) {
        String consulta = normalizar(q);
        model.addAttribute("cursos", service.buscar(consulta));
        model.addAttribute("q", consulta);
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));
        model.addAttribute("curso", curso);
        return "cursos/detalle";
    }

    @GetMapping("/nuevo")
    public String nuevoFormulario(Model model) {
        if (!model.containsAttribute("curso")) {
            model.addAttribute("curso", new CourseInputDto());
        }
        return "cursos/formulario";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("curso") CourseInputDto input,
                        BindingResult resultado,
                        RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            return "cursos/formulario";
        }
        try {
            service.crear(input);
            redirectAttributes.addFlashAttribute("mensajeExito", "Curso creado exitosamente.");
        } catch (DuplicateCourseException e) {
            resultado.rejectValue("titulo", "error.curso", e.getMessage());
            return "cursos/formulario";
        }
        return "redirect:/cursos";
    }

    @GetMapping("/{id}/editar")
    public String editarFormulario(@PathVariable long id, Model model) {
        CourseDto cursoDto = service.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));
        if (!model.containsAttribute("curso")) {
            model.addAttribute("curso", new CourseInputDto(cursoDto.titulo(), cursoDto.horas()));
        }
        model.addAttribute("cursoId", id);
        return "cursos/formulario";
    }

    @PostMapping("/{id}/editar")
    public String actualizar(@PathVariable long id,
                             @Valid @ModelAttribute("curso") CourseInputDto input,
                             BindingResult resultado,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            model.addAttribute("cursoId", id);
            return "cursos/formulario";
        }
        try {
            service.actualizar(id, input);
            redirectAttributes.addFlashAttribute("mensajeExito", "Curso actualizado exitosamente.");
        } catch (DuplicateCourseException e) {
            resultado.rejectValue("titulo", "error.curso", e.getMessage());
            model.addAttribute("cursoId", id);
            return "cursos/formulario";
        }
        return "redirect:/cursos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            service.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Curso eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No se pudo eliminar el curso.");
        }
        return "redirect:/cursos";
    }

    private String normalizar(String q) {
        String consulta = q.strip();
        if (consulta.length() > MAX_QUERY_LENGTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La búsqueda admite hasta 60 caracteres");
        }
        return consulta;
    }
}