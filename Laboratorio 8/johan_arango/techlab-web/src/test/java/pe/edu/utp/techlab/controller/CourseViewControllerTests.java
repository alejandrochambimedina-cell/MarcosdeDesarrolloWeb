package pe.edu.utp.techlab.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.utp.techlab.dto.CourseDto;
import pe.edu.utp.techlab.service.CourseService;

@WebMvcTest({ CourseViewController.class, PortalController.class })
@Import(CourseService.class)
class CourseViewControllerTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void listadoRenderizaModeloYHtml() throws Exception {
        mvc.perform(get("/cursos"))
                .andExpect(status().isOk())
                .andExpect(view().name("cursos/lista"))
                .andExpect(model().attribute("q", ""))
                .andExpect(model().attribute("cursos", hasSize(3)))
                .andExpect(content().string(containsString("Catálogo de cursos")));
    }

    @Test
    void filtroConservaConsultaYReduceResultados() throws Exception {
        mvc.perform(get("/cursos").param("q", "Spring"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("q", "Spring"))
                .andExpect(model().attribute("cursos", hasSize(1)))
                .andExpect(content().string(containsString("Spring Boot")));
    }

    @Test
    void detalleRenderizaElCursoSolicitado() throws Exception {
        mvc.perform(get("/cursos/2"))
                .andExpect(status().isOk())
                .andExpect(view().name("cursos/detalle"))
                .andExpect(model().attribute("curso", new CourseDto(2L, "Bootstrap", 16)))
                .andExpect(content().string(containsString("Bootstrap")));
    }

    @Test
    void resumenMuestraCantidadYHoras() throws Exception {
        mvc.perform(get("/cursos/resumen"))
                .andExpect(status().isOk())
                .andExpect(view().name("cursos/resumen"))
                .andExpect(model().attribute("cantidadCursos", 3))
                .andExpect(model().attribute("totalHoras", 48))
                .andExpect(content().string(containsString("48")));
    }

    @Test
    void erroresConservanEstadosHttp() throws Exception {
        mvc.perform(get("/cursos/999"))
                .andExpect(status().isNotFound());

        mvc.perform(get("/cursos").param("q", "x".repeat(61)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void portalRedirigeAlCatalogo() throws Exception {
        mvc.perform(get("/portal"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/cursos"));
    }

    // --- PRUEBAS DEL RETO INDIVIDUAL (minHoras) ---

    @Test
    void filtroMinHorasValidoDevuelveCursosEsperados() throws Exception {
        mvc.perform(get("/cursos").param("minHoras", "16"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("minHoras", "16"))
                .andExpect(model().attribute("cursos", hasSize(2)))
                .andExpect(content().string(containsString("Bootstrap")))
                .andExpect(content().string(containsString("Spring Boot")));
    }

    @Test
    void filtroCombinadoTextoYMinHoras() throws Exception {
        mvc.perform(get("/cursos").param("q", "boot").param("minHoras", "18"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("q", "boot"))
                .andExpect(model().attribute("minHoras", "18"))
                .andExpect(model().attribute("cursos", hasSize(1)))
                .andExpect(content().string(containsString("Spring Boot")));
    }

    @Test
    void filtroMinHorasInvalidoDevuelveBadRequest() throws Exception {
        mvc.perform(get("/cursos").param("minHoras", "-1"))
                .andExpect(status().isBadRequest());

        mvc.perform(get("/cursos").param("minHoras", "abc"))
                .andExpect(status().isBadRequest());
    }
}
