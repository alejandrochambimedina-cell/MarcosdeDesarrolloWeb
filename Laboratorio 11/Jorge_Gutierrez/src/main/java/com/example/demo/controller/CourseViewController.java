
package com.example.demo.controller;

import java.util.Locale;

import jakarta.validation.Valid;

import org.springframework.context.MessageSource;
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

import com.example.demo.dto.CourseDto;
import com.example.demo.dto.CourseInputDto;
import com.example.demo.exception.CourseNotFoundException;
import com.example.demo.exception.DuplicateCourseTitleException;
import com.example.demo.exception.InvalidCourseDataException;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CourseService;

@Controller
@RequestMapping("/cursos")
public class CourseViewController {

    private final CourseService service;
    private final CategoryRepository categoryRepository;
    private final MessageSource messageSource;

    public CourseViewController(
            CourseService service,
            CategoryRepository categoryRepository,
            MessageSource messageSource) {
        this.service = service;
        this.categoryRepository = categoryRepository;
        this.messageSource = messageSource;
    }

    @GetMapping
    public String listar(
            @RequestParam(defaultValue = "") String q,
            Model model) {

        String consulta = q.strip();

        if (consulta.length() > 60) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La búsqueda admite hasta 60 caracteres");
        }

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
                .orElseThrow(() -> notFound(id));

        model.addAttribute("curso", curso);
        return "cursos/detalle";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        return mostrarFormulario(
                new CourseInputDto(), null,
                "Nuevo curso", "Registrar curso", null, model);
    }

    @PostMapping
    public String crear(
            @Valid @ModelAttribute("form") CourseInputDto form,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirect) {

        if (resultado.hasErrors()) {
            return mostrarFormulario(
                    form, null, "Nuevo curso", "Registrar curso",
                    null, model);
        }

        try {
            CourseDto creado = service.crear(form);

            redirect.addFlashAttribute(
                    "mensaje",
                    messageSource.getMessage(
                            "curso.creado", null, Locale.getDefault()));

            return "redirect:/cursos/" + creado.id();

        } catch (InvalidCourseDataException
                | DuplicateCourseTitleException exception) {
            return mostrarFormulario(
                    form, null, "Nuevo curso", "Registrar curso",
                    exception.getMessage(), model);
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable long id, Model model) {
        CourseDto curso = service.buscarPorId(id)
                .orElseThrow(() -> notFound(id));

        CourseInputDto form = new CourseInputDto(
                curso.titulo(),
                curso.horas(),
                curso.categoria().id());

        return mostrarFormulario(
                form, id, "Editar curso", "Guardar cambios", null, model);
    }

    @PostMapping("/{id}")
    public String actualizar(
            @PathVariable long id,
            @Valid @ModelAttribute("form") CourseInputDto form,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirect) {

        if (resultado.hasErrors()) {
            return mostrarFormulario(
                    form, id, "Editar curso", "Guardar cambios",
                    null, model);
        }

        try {
            service.actualizar(id, form);

            redirect.addFlashAttribute(
                    "mensaje",
                    messageSource.getMessage(
                            "curso.actualizado", null, Locale.getDefault()));

            return "redirect:/cursos/" + id;

        } catch (InvalidCourseDataException
                | DuplicateCourseTitleException exception) {
            return mostrarFormulario(
                    form, id, "Editar curso", "Guardar cambios",
                    exception.getMessage(), model);
        } catch (CourseNotFoundException exception) {
            throw notFound(id);
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(
            @PathVariable long id,
            RedirectAttributes redirect) {

        try {
            service.eliminar(id);

            redirect.addFlashAttribute(
                    "mensaje",
                    messageSource.getMessage(
                            "curso.eliminado", null, Locale.getDefault()));

            return "redirect:/cursos";

        } catch (CourseNotFoundException exception) {
            throw notFound(id);
        }
    }

    private String mostrarFormulario(
            CourseInputDto form,
            Long cursoId,
            String tituloPagina,
            String textoBoton,
            String error,
            Model model) {

        model.addAttribute("form", form);
        model.addAttribute("cursoId", cursoId);
        model.addAttribute("tituloPagina", tituloPagina);
        model.addAttribute("textoBoton", textoBoton);
        model.addAttribute("error", error);
        model.addAttribute(
                "categorias",
                categoryRepository.findAllByOrderByNombreAsc());

        return "cursos/formulario";
    }

    private ResponseStatusException notFound(long id) {
        return new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No existe un curso con el identificador " + id);
    }
}
