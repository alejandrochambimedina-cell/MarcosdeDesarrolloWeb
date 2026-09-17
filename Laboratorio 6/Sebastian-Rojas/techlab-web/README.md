# TechLab — Semana 6: Spring Web y arquitectura MVC

> Este proyecto incluye la base corregida de la semana 5 (`TechLabWebApplication`,
> `StartupReporter` en `pe.edu.utp.techlab.bootstrap`, `application-local.properties`
> y el frontend estático) más los controladores, el servicio y las pruebas de
> Spring MVC de esta semana.

## Cómo ejecutar

```
./mvnw spring-boot:run
```

Abrir `http://localhost:8080/portal`.

## Cómo probar

```
./mvnw test
./mvnw clean package
java -jar target/techlab-web-0.0.1-SNAPSHOT.jar
```

## Reto de transferencia: `GET /api/v1/cursos/{id}/duracion`

Reutiliza `CourseService.buscarPorId` (no duplica la lista ni agrega condiciones por ID)
y convierte el `CourseDto` encontrado a un nuevo record `DurationDto(id, horas)`.

- `GET /api/v1/cursos/2/duracion` → `200` con `{"id":2,"horas":16}`
- `GET /api/v1/cursos/999/duracion` → `404`
- `GET /api/v1/cursos/abc/duracion` → `400` (conversión de `long` falla)

**¿Por qué `/{id}/duracion` no choca con `/{id}`?**
Spring MVC compara la plantilla completa de la ruta, no solo el número de segmentos.
`/{id}` coincide con exactamente un segmento después de `/cursos`, mientras que
`/{id}/duracion` exige un segmento fijo adicional (`duracion`). El `PathPattern`
de cada mapeo es distinto, así que el `DispatcherServlet` los distingue sin ambigüedad:
una solicitud a `/2/duracion` nunca podría satisfacer `/{id}` completo porque le
sobra un segmento literal.

## Comprobación conceptual

1. **DispatcherServlet** actúa como controlador frontal: recibe toda solicitud que
   llega a Tomcat, la enruta al método Java correcto según los mapeos registrados,
   resuelve los argumentos y procesa el valor devuelto, antes de que el controlador
   ejecute su propia lógica.
2. Un JSON servido por Spring MVC en un `@RestController` se serializa directamente
   desde el objeto devuelto mediante un `HttpMessageConverter`; no pasa por un
   `ViewResolver` ni genera HTML en el servidor, que es justamente lo que hace
   Thymeleaf.
3. El **Model** es el conjunto de atributos que una vista puede leer para
   renderizarse; un **DTO** es un objeto de transporte inmutable pensado para la
   respuesta HTTP; una **entidad de persistencia** representa una fila de base de
   datos y suele llevar anotaciones JPA. Los tres son conceptos distintos aunque
   puedan contener datos parecidos.
4. En `@Controller`, un `String` devuelto normalmente selecciona el nombre de una
   vista (o, con el prefijo `redirect:`, dispara una nueva solicitud). En
   `@RestController` (que añade `@ResponseBody`), ese mismo `String` se escribe
   tal cual en el cuerpo de la respuesta.
5. `@RequestParam` conviene cuando el dato es opcional o de filtro (query string,
   como `q`); `@PathVariable` conviene cuando el dato identifica de forma única
   al recurso dentro de la ruta (como el `id` de un curso).
6. Una búsqueda sin coincidencias responde `200` con una lista vacía porque la
   operación en sí fue válida y se ejecutó correctamente; `404` se reserva para
   cuando el recurso solicitado (un ID puntual) no existe.
7. El `400` de una consulta larga lo decide explícitamente el controlador
   (`q.length() > 60`); el `400` de un ID no numérico lo genera Spring al fallar
   la conversión automática de `String` a `long` antes de invocar el método.
8. `fetch` solo rechaza la promesa ante fallos de red; una respuesta HTTP con
   estado de error (404, 500) se resuelve igual, por eso hay que revisar
   `respuesta.ok` explícitamente antes de leer el cuerpo como JSON.
9. `MockMvc` demuestra el comportamiento del transporte HTTP (rutas, estados,
   cuerpo JSON, redirecciones) sin abrir un puerto real. El navegador es
   necesario para comprobar lo que MockMvc no simula: renderizado visual,
   accesibilidad, ejecución real de `fetch` y carga de recursos estáticos.
10. `CourseService` no depende de HTTP ni de Spring MVC, así que al incorporar
    vistas dinámicas con Thymeleaf en la semana 7 se reutilizará sin cambios:
    un nuevo controlador basado en plantillas podrá inyectarlo igual que
    `CourseController` y agregar sus resultados como atributos del `Model`.

## Matriz de pruebas (semana 6)

| ID | Procedimiento | Esperado | Cubierto por |
|----|---|---|---|
| 01 | `GET /portal` sin seguir redirección | 302 → `/catalogo.html` | `WebRoutesTests` |
| 02 | Abrir `/portal` en navegador | Catálogo y tres cursos | Manual |
| 03 | Buscar "SPRING" con espacios | Un curso ID 3 | `CourseServiceTests` |
| 04 | Buscar "ZZZ" | 200, lista vacía | Manual |
| 05 | `GET /api/v1/cursos/2` | 200, Bootstrap | `WebRoutesTests` |
| 06 | `GET /api/v1/cursos/999` | 404 | `WebRoutesTests` |
| 07 | `GET /api/v1/cursos/abc` | 400 | `WebRoutesTests` |
| 08 | `q` de 61 caracteres | 400 | `WebRoutesTests` |
| 09 | `POST /api/v1/cursos` | 405 | `WebRoutesTests` |
| 10 | Detener servidor y buscar | Error visible, botón habilitado | Manual |
| 11 | Teclado y 360px | Controles usables | Manual |
| 12 | `test` y `clean package` | BUILD SUCCESS + JAR | Terminal |
| 13 | Ejecutar JAR y repetir catálogo | Mismas rutas y datos | Manual |
| 14 | Regresión APF1 y health | Funciones previas y UP | Manual |
| 15 | `GET /api/v1/cursos/{id}/duracion` (reto) | 200/404/400 según ID | `WebRoutesTests` |

> Nota: los IDs 02, 04, 10, 11, 13 y 14 requieren navegador real; ejecuta la app
> y complétalos a mano en tu evidencia final, con capturas de la pestaña Network.
