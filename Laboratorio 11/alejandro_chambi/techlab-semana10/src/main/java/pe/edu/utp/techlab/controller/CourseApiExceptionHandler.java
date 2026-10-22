package pe.edu.utp.techlab.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.techlab.exception.*;

@RestControllerAdvice(assignableTypes = CourseController.class)
public class CourseApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> invalidBody(MethodArgumentNotValidException ex) {
        Map<String,String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> errors.putIfAbsent(e.getField(), e.getDefaultMessage()));
        ProblemDetail body = createProblem(HttpStatus.BAD_REQUEST, "Datos inválidos", "Revise los campos indicados");
        body.setProperty("errors", errors); return ResponseEntity.badRequest().body(body);
    }
    @ExceptionHandler(InvalidCourseDataException.class)
    public ResponseEntity<ProblemDetail> invalidData(InvalidCourseDataException ex) { return problem(HttpStatus.BAD_REQUEST, "Datos inválidos", ex.getMessage()); }
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ProblemDetail> invalidCategory(CategoryNotFoundException ex) {
        ProblemDetail body = createProblem(HttpStatus.BAD_REQUEST, "Categoría inválida", ex.getMessage());
        body.setProperty("errors", Map.of("categoriaId", ex.getMessage())); return ResponseEntity.badRequest().body(body);
    }
    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ProblemDetail> notFound(CourseNotFoundException ex) { return problem(HttpStatus.NOT_FOUND, "Curso no encontrado", ex.getMessage()); }
    @ExceptionHandler({DuplicateCourseTitleException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ProblemDetail> conflict(RuntimeException ex) {
        String detail = ex instanceof DuplicateCourseTitleException ? ex.getMessage() : "La operación incumple una restricción de la base de datos";
        return problem(HttpStatus.CONFLICT, "Conflicto de datos", detail);
    }
    private ResponseEntity<ProblemDetail> problem(HttpStatus status, String title, String detail) { return ResponseEntity.status(status).body(createProblem(status,title,detail)); }
    private ProblemDetail createProblem(HttpStatus status, String title, String detail) { ProblemDetail p = ProblemDetail.forStatusAndDetail(status, detail); p.setTitle(title); return p; }
}
