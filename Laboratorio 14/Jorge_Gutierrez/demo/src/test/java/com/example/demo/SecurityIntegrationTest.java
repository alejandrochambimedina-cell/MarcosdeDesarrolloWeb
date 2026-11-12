
package com.example.demo;

import com.example.demo.entity.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void prepararUsuarioDePrueba() {

        if (!userAccountRepository.existsByUsername(USERNAME)) {
            UserAccount usuario = new UserAccount(
                    USERNAME,
                    passwordEncoder.encode(PASSWORD),
                    Set.of("ROLE_USER")
            );

            userAccountRepository.save(usuario);
        }
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
    void apiConCredencialesValidasDevuelve200() throws Exception {
        mockMvc.perform(get("/api/v1/cursos")
                        .with(httpBasic(USERNAME, PASSWORD)))
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
