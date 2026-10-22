package pe.edu.utp.techlab.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.utp.techlab.dto.*;
import pe.edu.utp.techlab.exception.*;
import pe.edu.utp.techlab.service.CourseService;

@Controller
@RequestMapping("/cursos")
public class CourseViewController {
    private final CourseService service;
    public CourseViewController(CourseService service) { this.service = service; }
    @GetMapping public String listar(@RequestParam(defaultValue = "") String q, @RequestParam(required = false) Long categoriaId, Model model) {
        String query = q == null ? "" : q.strip();
        model.addAttribute("cursos", service.buscar(query, categoriaId));
        model.addAttribute("categorias", service.listarCategorias()); model.addAttribute("q", query); model.addAttribute("categoriaId", categoriaId);
        return "cursos/lista";
    }
    @GetMapping("/resumen") public String resumen(Model model) { model.addAttribute("cantidadCursos", service.cantidad()); model.addAttribute("totalHoras", service.totalHoras()); return "cursos/resumen"; }
    @GetMapping("/{id}") public String detalle(@PathVariable long id, Model model) { model.addAttribute("curso", service.buscarPorId(id).orElseThrow(() -> notFound(id))); return "cursos/detalle"; }
    @GetMapping("/nuevo") public String nuevo(Model model) { return mostrar(new CourseInputDto(), null, "Nuevo curso", "Registrar curso", model); }
    @PostMapping public String crear(@Valid @ModelAttribute("form") CourseInputDto form, BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) return mostrar(form, null, "Nuevo curso", "Registrar curso", model);
        try { CourseDto created = service.crear(form); redirect.addFlashAttribute("mensaje", "Curso registrado correctamente"); return "redirect:/cursos/" + created.id(); }
        catch (CategoryNotFoundException ex) { result.rejectValue("categoriaId", "categoria.inexistente", ex.getMessage()); return mostrar(form, null, "Nuevo curso", "Registrar curso", model); }
        catch (InvalidCourseDataException | DuplicateCourseTitleException ex) { model.addAttribute("error", ex.getMessage()); return mostrar(form, null, "Nuevo curso", "Registrar curso", model); }
    }
    @GetMapping("/{id}/editar") public String editar(@PathVariable long id, Model model) {
        CourseDto course = service.buscarPorId(id).orElseThrow(() -> notFound(id));
        return mostrar(new CourseInputDto(course.titulo(), course.horas(), course.categoriaId()), id, "Editar curso", "Guardar cambios", model);
    }
    @PostMapping("/{id}") public String actualizar(@PathVariable long id, @Valid @ModelAttribute("form") CourseInputDto form, BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) return mostrar(form, id, "Editar curso", "Guardar cambios", model);
        try { service.actualizar(id, form); redirect.addFlashAttribute("mensaje", "Curso actualizado correctamente"); return "redirect:/cursos/" + id; }
        catch (CategoryNotFoundException ex) { result.rejectValue("categoriaId", "categoria.inexistente", ex.getMessage()); return mostrar(form, id, "Editar curso", "Guardar cambios", model); }
        catch (InvalidCourseDataException | DuplicateCourseTitleException ex) { model.addAttribute("error", ex.getMessage()); return mostrar(form, id, "Editar curso", "Guardar cambios", model); }
        catch (CourseNotFoundException ex) { throw notFound(id); }
    }
    @PostMapping("/{id}/eliminar") public String eliminar(@PathVariable long id, RedirectAttributes redirect) { try { service.eliminar(id); redirect.addFlashAttribute("mensaje", "Curso eliminado correctamente"); return "redirect:/cursos"; } catch (CourseNotFoundException ex) { throw notFound(id); } }
    private String mostrar(CourseInputDto form, Long id, String title, String button, Model model) {
        model.addAttribute("form", form); model.addAttribute("cursoId", id); model.addAttribute("tituloPagina", title); model.addAttribute("textoBoton", button); model.addAttribute("categorias", service.listarCategorias()); return "cursos/formulario";
    }
    private ResponseStatusException notFound(long id) { return new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un curso con el identificador " + id); }
}
