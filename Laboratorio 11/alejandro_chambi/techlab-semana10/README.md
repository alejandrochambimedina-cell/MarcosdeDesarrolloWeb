# TechLab — Guía de laboratorio Semana 11

Proyecto acumulativo de Marcos de Desarrollo Web. Parte del CRUD de cursos de la semana 10 y agrega categorías, relación JPA `ManyToOne`, migración Flyway, validación Jakarta Validation en MVC/REST y consultas con carga planificada.

## Estructura principal

```text
src/main/java/pe/edu/utp/techlab/
├── controller/
│   ├── CourseApiExceptionHandler.java
│   ├── CourseController.java
│   └── CourseViewController.java
├── dto/
│   ├── CategoryDto.java
│   ├── CourseDto.java
│   └── CourseInputDto.java
├── entity/
│   ├── Category.java
│   └── Course.java
├── exception/CategoryNotFoundException.java
├── mapper/CourseMapper.java
├── repository/
│   ├── CategoryRepository.java
│   └── CourseRepository.java
└── service/CourseService.java
src/main/resources/
├── db/migration/V3__add_categories_and_relationship.sql
├── ValidationMessages.properties
├── messages.properties
└── templates/cursos/
    ├── detalle.html
    ├── formulario.html
    └── lista.html
```

También se conservan los recursos estáticos, fragmentos Thymeleaf, `TechLabApplication`, configuración de la aplicación, Docker Compose y pruebas automatizadas.

## Requisitos

- JDK 21 (se conserva el nivel del proyecto base de semana 10).
- Docker Desktop con Docker Compose.
- MySQL 8.4 LTS mediante `compose.yaml`.

## Ejecutar

```bash
docker compose up -d mysql
docker compose ps
```

Configura las variables de conexión si tu entorno no usa los valores locales del `application.properties`. Después ejecuta con Maven desde tu IDE o terminal:

```bash
mvn clean test
mvn spring-boot:run
```

Abre `http://localhost:8080/cursos`. La API está en `http://localhost:8080/api/v1/cursos`.

## Importante sobre Flyway

La migración de semana 11 se entrega con el nombre solicitado `V3__add_categories_and_relationship.sql` y presupone que las migraciones V1 y V2 del proyecto base ya fueron aplicadas. No edites una migración que ya figure en `flyway_schema_history`. Si tu base ya registró una migración distinta como V3 (por ejemplo, una versión anterior que agregaba bloqueo optimista), detente y revisa el historial antes de ejecutar: Flyway no permite reutilizar el número V3 para un cambio diferente. En ese caso, adapta la numeración de la migración nueva a la siguiente versión libre y conserva el historial existente.

## Verificación manual

- Listado sin filtros y búsqueda por título.
- Filtrado por categoría y combinación título + categoría.
- Crear/editar con título vacío, horas fuera de 1–500 o categoría sin seleccionar.
- Enviar un número no entero en horas y comprobar el mensaje de conversión.
- Probar una categoría inexistente en REST y comprobar `400 ProblemDetail` con `errors.categoriaId`.
- Probar curso inexistente (`404`) y título duplicado (`409`).
- Confirmar en SQL que `cursos.categoria_id` es `NOT NULL` y tiene clave foránea a `categorias`.

Las comprobaciones que requieren iniciar Docker/MySQL y ejecutar la aplicación deben realizarse en el entorno local; no se marcan como ejecutadas desde este paquete.
