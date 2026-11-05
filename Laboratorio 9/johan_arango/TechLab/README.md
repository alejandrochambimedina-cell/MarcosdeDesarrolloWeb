# TechLab — Semana 9: Persistencia con JPA/Hibernate, Spring Data JPA, Flyway y MySQL

Entrega resuelta siguiendo la guía de laboratorio Semana 9.

## Incluye
- Spring Boot 4.1.1 + Java 25
- MySQL 8.4.12 mediante Docker Compose
- Flyway V1 y V2
- Entidad JPA `Course`
- `CourseRepository`, `CourseMapper` y `CourseService`
- Controladores MVC y REST
- DTO separado de la entidad
- Pruebas de persistencia y web
- Vistas Thymeleaf mínimas para `/cursos`, detalle y resumen

## Ejecución en Windows PowerShell
1. Copiar `.env.example` a `.env` y colocar claves locales.
2. Ejecutar:
   `docker compose up -d mysql`
3. Definir:
   `$env:DB_USER='techlab_app'`
   `$env:DB_PASSWORD='tu_clave'`
4. Ejecutar:
   `.\mvnw.cmd clean test`
5. Ejecutar:
   `.\mvnw.cmd spring-boot:run`

> Nota: el proyecto se entrega sin `.env` real ni `target/`.

## Rutas
- GET `/cursos`
- GET `/cursos?q=boot`
- GET `/cursos/2`
- GET `/cursos/999` → 404
- GET `/cursos/resumen`
- GET `/api/v1/cursos`
- GET `/api/v1/cursos?q=boot`
- GET `/api/v1/cursos/999` → 404
- GET `/actuator/health`

## Datos esperados
HTML y CSS — 12 horas
Bootstrap — 16 horas
Spring Boot — 20 horas
Total: 3 cursos, 48 horas.

## Preguntas de cierre resueltas

1. **¿Qué problema resuelve Flyway que `ddl-auto=update` no resuelve de forma auditable?**
   Flyway registra migraciones versionadas, su orden, ejecución y checksum. `ddl-auto=update` modifica el esquema implícitamente y no ofrece una historia de cambios revisable.

2. **¿Por qué una entidad JPA no debería convertirse automáticamente en la respuesta REST?**
   Porque la entidad pertenece al modelo de persistencia y puede contener detalles de infraestructura o asociaciones administradas por Hibernate. El DTO mantiene separado el contrato de la API y evita exponer ese modelo directamente.

3. **¿Dónde termina la transacción cuando `open-in-view` está desactivado?**
   El acceso a datos debe resolverse dentro de la transacción del servicio. En este proyecto `CourseService` está marcado con `@Transactional(readOnly = true)` y convierte las entidades a DTO antes de retornar.

4. **¿Qué prueba fallaría primero si se renombra la columna `titulo` solo en la entidad?**
   La validación del esquema de Hibernate al iniciar (`ddl-auto=validate`), porque el mapeo de la entidad dejaría de coincidir con la columna real creada por Flyway.

5. **¿Qué partes de TechLab permanecerán estables cuando se agregue CRUD en la semana 10?**
   Las rutas y contratos existentes, los DTO, la separación por capas y la estrategia de persistencia seguirán siendo la base. La semana 10 incorporará las operaciones de escritura y sus reglas.

## Reto opcional
La guía plantea V3 como reto incremental. No se incluye como parte de la solución base para respetar la entrega principal V1/V2.
