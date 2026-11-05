
package com.example.demo.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exception.CourseNotFoundException;
import com.example.demo.exception.DuplicateCourseTitleException;
import com.example.demo.exception.InvalidCourseDataException;

@RestControllerAdvice(assignableTypes = CourseRestController.class)
public class CourseApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> validation(
            MethodArgumentNotValidException exception) {

        Map<String, String> errores = new LinkedHashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error ->
            errores.putIfAbsent(
                error.getField(),
                error.getDefaultMessage()
            )
        );

        ProblemDetail body = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "Uno o más campos contienen datos inválidos"
        );

        body.setTitle("Error de validación");
        body.setProperty("errores", errores);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(InvalidCourseDataException.class)
    public ResponseEntity<ProblemDetail> invalidData(
            InvalidCourseDataException exception) {
        return problem(HttpStatus.BAD_REQUEST,
                "Datos inválidos", exception.getMessage());
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ProblemDetail> notFound(
            CourseNotFoundException exception) {
        return problem(HttpStatus.NOT_FOUND,
                "Curso no encontrado", exception.getMessage());
    }

    @ExceptionHandler({
        DuplicateCourseTitleException.class,
        DataIntegrityViolationException.class
    })
    public ResponseEntity<ProblemDetail> conflict(RuntimeException exception) {
        String detail = exception instanceof DuplicateCourseTitleException
                ? exception.getMessage()
                : "La operación incumple una restricción de la base de datos";

        return problem(HttpStatus.CONFLICT, "Conflicto de datos", detail);
    }

    private ResponseEntity<ProblemDetail> problem(
            HttpStatus status, String title, String detail) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(status, detail);
        body.setTitle(title);
        return ResponseEntity.status(status).body(body);
    }
}
