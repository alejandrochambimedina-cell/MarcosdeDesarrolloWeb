package pe.edu.utp.techlab.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import pe.edu.utp.techlab.dto.CourseDto;
import pe.edu.utp.techlab.service.CourseService;

@RestController
@RequestMapping("/api/v1/cursos")
public class CourseController {
    private final CourseService service;
    public CourseController(CourseService service) { this.service = service; }

    @GetMapping
    public List<CourseDto> listar(@RequestParam(defaultValue="") String q) {
        return service.buscar(q);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> detalle(@PathVariable long id) {
        return service.buscarPorId(id).map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
