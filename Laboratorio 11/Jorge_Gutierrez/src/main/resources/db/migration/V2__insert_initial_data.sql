INSERT INTO users (id, name, email) VALUES
(1, 'Jorge', 'jorge@gmail.com'),
(2, 'Ana', 'ana@gmail.com'),
(3, 'Carlos', 'carlos@gmail.com');

INSERT INTO tasks (id, title, completed) VALUES
(1, 'Estudiar Spring Boot', FALSE),
(2, 'Completar la guía de laboratorio', TRUE),
(3, 'Practicar Thymeleaf', FALSE);

ALTER TABLE users AUTO_INCREMENT = 4;
ALTER TABLE tasks AUTO_INCREMENT = 4;