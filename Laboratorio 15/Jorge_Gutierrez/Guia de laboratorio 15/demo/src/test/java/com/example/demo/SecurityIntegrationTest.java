package com.example.demo;

import com.example.demo.entity.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    private static final String USERNAME = "docente-prueba";
    private static final String PASSWORD = "Prueba-Seguridad-2026!";

    private static final String ADMIN_USERNAME = "admin-prueba";
    private static final String ADMIN_PASSWORD = "Admin-Seguridad-2026!";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void prepararUsuariosDePrueba() {

        if (!userAccountRepository.existsByUsername(USERNAME)) {
            userAccountRepository.save(new UserAccount(
                    USERNAME,
                    passwordEncoder.encode(PASSWORD),
                    Set.of("ROLE_USER")
            ));
        }

        if (!userAccountRepository.existsByUsername(ADMIN_USERNAME)) {
            userAccountRepository.save(new UserAccount(
                    ADMIN_USERNAME,
                    passwordEncoder.encode(ADMIN_PASSWORD),
                    Set.of("ROLE_USER", "ROLE_ADMIN")
            ));
        }
    }

    private String obtenerToken(String username, String password)
            throws Exception {

        String cuerpo = objectMapper.writeValueAsString(
                java.util.Map.of(
                        "username", username,
                        "password", password
                )
        );

        MvcResult resultado = mockMvc.perform(
                        post("/api/v1/auth/token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(cuerpo))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode respuesta = objectMapper.readTree(
                resultado.getResponse().getContentAsString());

        return respuesta.get("access_token").asText();
    }

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
    void apiRechazaAutenticacionBasic() throws Exception {
        mockMvc.perform(get("/api/v1/cursos")
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void credencialesValidasGeneranToken() throws Exception {
        obtenerToken(USERNAME, PASSWORD);
    }

    @Test
    void usuarioConRolUserPuedeConsultarCursos() throws Exception {
        String token = obtenerToken(USERNAME, PASSWORD);

        mockMvc.perform(get("/api/v1/cursos")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void usuarioConRolUserNoPuedeCrearCursos() throws Exception {
        String token = obtenerToken(USERNAME, PASSWORD);

        mockMvc.perform(post("/api/v1/cursos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void administradorPuedeConsultarCursos() throws Exception {
        String token = obtenerToken(ADMIN_USERNAME, ADMIN_PASSWORD);

        mockMvc.perform(get("/api/v1/cursos")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void credencialesIncorrectasDevuelven401() throws Exception {
        String cuerpo = """
                {
                    "username": "docente-prueba",
                    "password": "clave-incorrecta"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo))
                .andExpect(status().isUnauthorized());
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
