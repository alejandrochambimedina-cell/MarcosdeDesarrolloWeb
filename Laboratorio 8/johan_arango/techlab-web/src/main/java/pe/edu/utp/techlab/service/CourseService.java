package pe.edu.utp.techlab.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pe.edu.utp.techlab.dto.CourseDto;

@Service
public class CourseService {

    private final List<CourseDto> cursos = List.of(
        new CourseDto(1L, "HTML y CSS", 12),
        new CourseDto(2L, "Bootstrap", 16),
        new CourseDto(3L, "Spring Boot", 20)
    );

    public List<CourseDto> buscar(String texto) {
        return buscar(texto, null);
    }

    public List<CourseDto> buscar(String texto, Integer minHoras) {
        String criterio = Optional.ofNullable(texto)
            .orElse("")
            .strip()
            .toLowerCase(Locale.ROOT);

        return cursos.stream()
            .filter(curso -> criterio.isBlank() || curso.titulo().toLowerCase(Locale.ROOT).contains(criterio))
            .filter(curso -> minHoras == null || curso.horas() >= minHoras)
            .toList();
    }

    public Optional<CourseDto> buscarPorId(long id) {
        return cursos.stream()
            .filter(curso -> curso.id() == id)
            .findFirst();
    }

    public int cantidad() {
        return cursos.size();
    }

    public int totalHoras() {
        return cursos.stream()
            .mapToInt(CourseDto::horas)
            .sum();
    }
}
