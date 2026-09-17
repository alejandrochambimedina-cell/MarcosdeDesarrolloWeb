# TechLab Web - Guía de Laboratorio Semana 6

Proyecto desarrollado para el laboratorio de la Semana 6 (Marcos de Desarrollo Web - UTP). 

## Estructura del Proyecto
- `src/main/java/pe/edu/utp/techlab/dto`: Objetos de transferencia de datos (`CourseDto`, `DurationDto`).
- `src/main/java/pe/edu/utp/techlab/service`: Lógica de negocio (`CourseService`).
- `src/main/java/pe/edu/utp/techlab/controller`: Controladores Web MVC y RestController (`CourseController`, `PortalController`).
- `src/main/resources/static`: Interfaz web estática (`catalogo.html`, `js/catalogo.js`, `index.html`).
- `src/test/java`: Pruebas unitarias y de capa web con MockMvc.

## Comandos para Ejecutar

### Ejecución en desarrollo:
```bash
./mvnw spring-boot:run
```
o en Windows:
```cmd
.\mvnw.cmd spring-boot:run
```

### Ejecutar Pruebas Automatizadas:
```bash
./mvnw test
```

### Generar y Ejecutar JAR:
```bash
./mvnw clean package
java -jar target/techlab-web-0.0.1-SNAPSHOT.jar
```

## Reto de Transferencia Implementado
Se ha agregado el endpoint `GET /api/v1/cursos/{id}/duracion`:
- **Respuesta para ID 2 (`GET /api/v1/cursos/2/duracion`)**:
  ```json
  {
    "id": 2,
    "horas": 16
  }
  ```
- **Sin conflicto con `/{id}`**: Spring Routing diferencia los endpoints porque uno mapea exactamente 2 segmentos de ruta (`/cursos/{id}`) y el otro 3 segmentos (`/cursos/{id}/duracion`).
