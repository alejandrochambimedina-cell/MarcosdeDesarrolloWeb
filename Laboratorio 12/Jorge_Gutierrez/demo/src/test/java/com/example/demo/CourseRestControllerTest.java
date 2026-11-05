
package com.example.demo;

import com.example.demo.controller.CourseRestController;
import com.example.demo.dto.CourseDto;
import com.example.demo.service.CourseService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebMvcTest(CourseRestController.class)
@Import(com.example.demo.controller.CourseApiExceptionHandler.class)
class CourseRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseService courseService;

    @Test
    void listarCursosDevuelve200() throws Exception {
        when(courseService.buscar("")).thenReturn(List.of(
                new CourseDto(1L, "HTML y CSS", 12,
                        new com.example.demo.dto.CategoryDto(1L, "Front-end"))
        ));

        mockMvc.perform(get("/api/v1/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("HTML y CSS"))
                .andExpect(jsonPath("$[0].categoria.nombre").value("Front-end"));
    }

    @Test
    void crearCursoConDatosInvalidosDevuelve400() throws Exception {
        mockMvc.perform(post("/api/v1/cursos")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "",
                                  "horas": 0,
                                  "categoriaId": 3
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
