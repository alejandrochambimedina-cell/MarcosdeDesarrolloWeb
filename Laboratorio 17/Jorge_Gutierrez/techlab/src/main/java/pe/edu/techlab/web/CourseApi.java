package pe.edu.techlab.web;
import pe.edu.techlab.service.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.net.URI;
import java.util.List;
@RestController @RequestMapping("/api/courses")
public class CourseApi {
    private final CourseService service;
    public CourseApi(CourseService service) { this.service=service; }
    @GetMapping public List<CourseView> list() { return service.list(); }
    @GetMapping("/{id}") public CourseView get(@PathVariable long id) {
        return service.get(id);
    }
    @PostMapping public ResponseEntity<CourseView> create(@Valid @RequestBody CourseInput input) {
        CourseView course=service.create(input);
        return ResponseEntity.created(URI.create("/api/courses/"+course.id())).body(course);
    }
    @PutMapping("/{id}") public CourseView update(@PathVariable long id,
        @Valid @RequestBody CourseInput input) { return service.update(id,input); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id); return ResponseEntity.noContent().build();
    }
}
