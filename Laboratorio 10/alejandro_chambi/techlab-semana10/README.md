# TechLab Web — Semana 10

Proyecto de laboratorio de Marcos de Desarrollo Web: CRUD de cursos con Spring Boot, Spring Data JPA, MySQL, Flyway, Thymeleaf y API REST. Incluye además el reto de concurrencia optimista (`@Version`) de la guía.

## Requisitos
- JDK 21
- Maven 3.9+
- Docker Desktop con Docker Compose

## 1. Iniciar MySQL
```bash
docker compose up -d mysql
docker compose ps
```
La salud del contenedor debe aparecer como `healthy`. Para usar otras credenciales, defina `DB_PASSWORD` antes de iniciar el contenedor y la aplicación. No suba secretos reales al repositorio.

## 2. Ejecutar pruebas y aplicación
```bash
mvn clean test
mvn clean package
mvn spring-boot:run
```
En PowerShell:
```powershell
$env:DB_USER='techlab_app'
$env:DB_PASSWORD='techlab_local_2026'
mvn clean test
mvn clean package
java -jar target/techlab-web-0.0.1-SNAPSHOT.jar
```
La configuración predeterminada es local para facilitar el laboratorio. Cambie las contraseñas de ejemplo para cualquier entorno compartido.

## 3. Rutas web
- `http://localhost:8080/cursos` — catálogo y búsqueda `?q=Spring`
- `http://localhost:8080/cursos/nuevo` — crear curso
- `http://localhost:8080/cursos/resumen` — total de cursos y horas
- `/cursos/{id}` — detalle
- `/cursos/{id}/editar` — edición

Los formularios usan POST-Redirect-Get y confirmación de eliminación.

## 4. API REST
- `GET /api/v1/cursos?q=Spring` — lista / búsqueda (200)
- `GET /api/v1/cursos/{id}` — detalle (200 o 404)
- `POST /api/v1/cursos` — crear (201 y cabecera Location)
- `PUT /api/v1/cursos/{id}` — actualizar (200; 400, 404 o 409)
- `DELETE /api/v1/cursos/{id}` — eliminar (204 o 404)

Ejemplo:
```bash
curl -i -X POST http://localhost:8080/api/v1/cursos \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Java profesional","horas":24,"version":0}'
```
Para actualizar, use la `version` devuelta en el GET o POST; al editar la entidad la versión cambia. Si el cliente envía una versión antigua, se devuelve `409 Conflict`.

## 5. Migraciones y base
- V1 crea `cursos`.
- V2 inserta cursos de ejemplo.
- V3 agrega `version BIGINT NOT NULL DEFAULT 0` para concurrencia optimista.
No edite migraciones ya aplicadas. `spring.jpa.hibernate.ddl-auto=validate` hace que Hibernate valide el esquema.

## 6. Matriz funcional sugerida
| ID | Prueba | Resultado esperado |
|---|---|---|
| W01 | Abrir formulario nuevo | 200, campos vacíos |
| W02 | Crear válido | Redirección al detalle y mensaje |
| W03 | Título duplicado | Formulario conserva datos y muestra error |
| W04 | Editar existente | Cambio persistente |
| W05 | Editar ID inexistente | 404 |
| W06 | Cancelar confirmación de eliminación | Curso permanece |
| W07 | Confirmar eliminación | Redirección al catálogo y curso ausente |
| A01 | POST válido | 201, JSON y Location |
| A02 | POST con horas 0 | 400 ProblemDetail |
| A03 | POST con título duplicado | 409 ProblemDetail |
| A04 | PUT existente | 200 con datos nuevos |
| A05 | PUT inexistente | 404 ProblemDetail |
| A06 | DELETE existente | 204 sin cuerpo |
| A07 | GET tras borrar | 404 |
| P01 | Reiniciar aplicación | Datos persisten en MySQL |
| T01 | `mvn clean test` | BUILD SUCCESS |

## 7. Preguntas de cierre
1. **¿Por qué POST devuelve 201 mientras PUT normalmente devuelve 200?** POST crea un recurso nuevo y 201 Created comunica su creación; Location identifica el recurso. PUT modifica un recurso conocido y normalmente devuelve 200 con su representación actualizada (también puede usar 204 si no devuelve cuerpo).
2. **¿Qué problema evita Post Redirect Get?** Evita que una actualización del navegador vuelva a enviar el POST y duplique la operación; tras el POST válido se redirige a un GET.
3. **¿Por qué el id no debe recibirse en CourseInputDto?** Al crear lo genera la base de datos; al actualizar ya viene en la ruta. Así el cliente no puede elegir o alterar arbitrariamente la identidad.
4. **¿Qué diferencia hay entre validar antes de guardar y mantener UNIQUE en MySQL?** La validación anticipada da mensajes claros, pero dos solicitudes concurrentes podrían pasarla al mismo tiempo. La restricción UNIQUE de la base es la garantía final.
5. **¿Cuándo genera Hibernate el UPDATE si el servicio no llama save durante actualizar?** La entidad cargada por `findById` sigue administrada; el dirty checking detecta cambios y Hibernate emite UPDATE durante flush o commit de la transacción.
6. **¿Por qué una confirmación JavaScript no es una medida de seguridad?** El cliente puede desactivar JavaScript o llamar directamente al endpoint. La autorización y validación deben hacerse en el servidor.
7. **¿Qué responsabilidad añadirá Bean Validation en la semana 11?** Declarar reglas con anotaciones como `@NotBlank`, `@Size` y `@Min`/`@Max`, además de facilitar errores de validación por campo; no reemplaza reglas de negocio ni restricciones de base de datos.

## Notas de entrega
La guía presupone una base previa de semana 9; este ZIP es un proyecto autocontenido equivalente, con migraciones propias para poder iniciarlo desde cero. Se incluye V3 para el reto de concurrencia optimista. Las pruebas de integración requieren MySQL en ejecución. Verifique localmente `mvn clean test`; el entorno de generación no ejecutó una base MySQL.
