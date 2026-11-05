package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "app.security.demo-username=docente",
        "app.security.demo-password=Prueba-Seguridad-2026!"
})
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listaDeCursosEsPublica() throws Exception {
        mockMvc.perform(get("/cursos"))
                .andExpect(status().isOk());
    }

    @Test
    void formularioNuevoRequiereAutenticacion() throws Exception {
        mockMvc.perform(get("/cursos/nuevo"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/login"));
    }

    @Test
    void apiSinCredencialesDevuelve401() throws Exception {
        mockMvc.perform(get("/api/v1/cursos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void apiConCredencialesValidasDevuelve200() throws Exception {
        mockMvc.perform(get("/api/v1/cursos")
                        .with(httpBasic("docente", "Prueba-Seguridad-2026!")))
                .andExpect(status().isOk());
    }

    @Test
    void postSinTokenCsrfDevuelve403() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(status().isForbidden());
    }

    @Test
    void logoutConTokenCsrfRedirigeAlLogin() throws Exception {
        mockMvc.perform(post("/logout").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/login?logout"));
    }
}
