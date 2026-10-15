package pe.edu.utp.techlab.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CourseInputDto {

    @NotBlank(message = "{curso.titulo.NotBlank}")
    @Size(max = 120, message = "{curso.titulo.Size}")
    private String titulo;

    @Min(value = 1, message = "{curso.horas.Min}")
    @Max(value = 500, message = "{curso.horas.Max}")
    private int horas;

    public CourseInputDto() {
    }

    public CourseInputDto(String titulo, int horas) {
        this.titulo = titulo;
        this.horas = horas;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }
}