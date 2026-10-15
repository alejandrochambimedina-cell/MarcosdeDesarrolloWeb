package pe.edu.utp.techlab.controller;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pe.edu.utp.techlab.dto.*;
import pe.edu.utp.techlab.exception.CourseNotFoundException;
import pe.edu.utp.techlab.service.CourseService;
@RestController @RequestMapping("/api/v1/cursos")
public class CourseController {
 private final CourseService service;
 public CourseController(CourseService s){service=s;}
 @GetMapping public List<CourseDto> listar(@RequestParam(defaultValue="") String q){return service.buscar(q);}
 @GetMapping("/{id}") public CourseDto detalle(@PathVariable long id){return service.buscarPorId(id).orElseThrow(()->new CourseNotFoundException(id));}
 @PostMapping public ResponseEntity<CourseDto> crear(@RequestBody CourseInputDto input,UriComponentsBuilder b){
  CourseDto c=service.crear(input); URI u=b.path("/api/v1/cursos/{id}").buildAndExpand(c.id()).toUri();return ResponseEntity.created(u).body(c);
 }
 @PutMapping("/{id}") public CourseDto actualizar(@PathVariable long id,@RequestBody CourseInputDto input){return service.actualizar(id,input);}
 @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable long id){service.eliminar(id);return ResponseEntity.noContent().build();}
}
