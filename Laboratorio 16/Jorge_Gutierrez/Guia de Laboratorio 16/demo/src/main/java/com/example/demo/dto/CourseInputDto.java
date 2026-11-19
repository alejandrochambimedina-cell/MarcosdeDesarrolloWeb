
package com.example.demo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CourseInputDto {

    @NotBlank(message = "{NotBlank.titulo}")
    @Size(max = 120, message = "{Size.titulo}")
    private String titulo;

    @NotNull(message = "{NotNull.horas}")
    @Min(value = 1, message = "{Min.horas}")
    @Max(value = 500, message = "{Max.horas}")
    private Integer horas;

    @NotNull(message = "{NotNull.categoriaId}")
    @Min(value = 1, message = "{Min.categoriaId}")
    private Long categoriaId;

    public CourseInputDto() {
    }

    public CourseInputDto(String titulo, Integer horas, Long categoriaId) {
        this.titulo = titulo;
        this.horas = horas;
        this.categoriaId = categoriaId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getHoras() {
        return horas;
    }

    public void setHoras(Integer horas) {
        this.horas = horas;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }
}
