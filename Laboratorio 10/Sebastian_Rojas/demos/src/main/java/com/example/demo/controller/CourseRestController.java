package com.example.demo.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.dto.CourseDto;
import com.example.demo.dto.CourseInputDto;
import com.example.demo.exception.CourseNotFoundException;
import com.example.demo.service.CourseService;

@RestController
@RequestMapping("/api/v1/cursos")
public class CourseRestController {

    private final CourseService courseService;

    public CourseRestController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<CourseDto> listar(
            @RequestParam(defaultValue = "") String q) {
        return courseService.buscar(q);
    }

    @GetMapping("/{id}")
    public CourseDto detalle(@PathVariable long id) {
        return courseService.buscarPorId(id)
                .orElseThrow(() -> new CourseNotFoundException(id));
    }

    @PostMapping
    public ResponseEntity<CourseDto> crear(
            @RequestBody CourseInputDto input,
            UriComponentsBuilder uriBuilder) {

        CourseDto creado = courseService.crear(input);

        URI location = uriBuilder
                .path("/api/v1/cursos/{id}")
                .buildAndExpand(creado.id())
                .toUri();

        return ResponseEntity.created(location).body(creado);
    }

    @PutMapping("/{id}")
    public CourseDto actualizar(
            @PathVariable long id,
            @RequestBody CourseInputDto input) {
        return courseService.actualizar(id, input);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable long id) {
        courseService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
