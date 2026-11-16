package com.example.demo;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {
        AuthApiController.class,
        ReservaApiController.class
})
public class ApiValidationAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> manejarValidacion(
            MethodArgumentNotValidException excepcion) {

        List<Map<String, String>> errores =
                excepcion.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error -> Map.of(
                                "campo", error.getField(),
                                "mensaje", error.getDefaultMessage()))
                        .toList();

        return ResponseEntity.badRequest().body(
                Map.of(
                        "mensaje", "Los datos enviados no son válidos.",
                        "errores", errores));
    }
}
