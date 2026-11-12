
package com.example.demo.security;

import com.example.demo.entity.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Set;

@Component
@Profile("lab")
public class LabUserSeeder implements ApplicationRunner {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminPassword;
    private final String estudiantePassword;

    public LabUserSeeder(
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.security.seed.admin-password}")
            String adminPassword,
            @Value("${app.security.seed.estudiante-password}")
            String estudiantePassword) {

        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminPassword = adminPassword;
        this.estudiantePassword = estudiantePassword;
    }

    @Override
    public void run(ApplicationArguments args) {

        validarPassword("admin", adminPassword);
        validarPassword("estudiante", estudiantePassword);

        if (!userAccountRepository.existsByUsername("admin")) {
            UserAccount admin = new UserAccount(
                    "admin",
                    passwordEncoder.encode(adminPassword),
                    Set.of("ROLE_USER", "ROLE_ADMIN")
            );

            userAccountRepository.save(admin);
        }

        if (!userAccountRepository.existsByUsername("estudiante")) {
            UserAccount estudiante = new UserAccount(
                    "estudiante",
                    passwordEncoder.encode(estudiantePassword),
                    Set.of("ROLE_USER")
            );

            userAccountRepository.save(estudiante);
        }
    }

    private void validarPassword(String usuario, String password) {

        int longitudBytes = password.getBytes(StandardCharsets.UTF_8).length;

        if (longitudBytes < 12 || longitudBytes > 72) {
            throw new IllegalArgumentException(
                    "La contraseña de " + usuario
                            + " debe tener entre 12 y 72 bytes."
            );
        }
    }
}
