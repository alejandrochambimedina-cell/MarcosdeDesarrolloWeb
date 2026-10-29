# TechLab Web — Semana 7

Marcos de Desarrollo Web · UTP · Thymeleaf: expresiones, atributos y plantillas.

Java 25 LTS · Spring Boot 4.1.1 · Thymeleaf 3.1.5 · Bootstrap 5.3.8

## Qué hace
- **API REST** (semana 6) en `/api/v1/cursos` → JSON.
- **Vistas Thymeleaf** sobre el mismo `CourseService`:
  - `GET /cursos` catálogo, con búsqueda `?q=` (máx. 60 caracteres)
  - `GET /cursos/{id}` detalle (404 si no existe)
  - `GET /cursos/resumen` **(reto)** total de cursos y de horas
  - `GET /` y `/portal` → redirección 302 a `/cursos`
- Fragmentos (`head`, `cabecera`, `pie`), mensajes en `messages.properties`, página de error `error/4xx.html`.

## Ejecutar
Linux / macOS / Git Bash:
```bash
./mvnw clean test
./mvnw spring-boot:run
```
Windows PowerShell:
```powershell
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```
Sin wrapper: usa `mvn` con los mismos objetivos, o genera el wrapper con `mvn wrapper:wrapper`.

Empaquetar y probar el JAR:
```bash
./mvnw clean package
java -jar target/techlab-web-0.0.1-SNAPSHOT.jar
```
App en http://localhost:8080/cursos · salud en `/actuator/health`.

## Estructura
```
src/main/java/pe/edu/utp/techlab/{controller,dto,service}
src/main/resources/{messages.properties,application.properties,static/css,templates}
src/test/java/.../CourseViewControllerTests.java
docs/  matriz de pruebas y reflexión
```

## Notas
- No subir `target/` ni datos sensibles (ver `.gitignore`).
- Las plantillas viven en `templates/`, nunca en `static/`.
- Evidencias (capturas) en `docs/evidencias/`.
