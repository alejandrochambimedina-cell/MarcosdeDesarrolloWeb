# Comprobación conceptual — Semana 7

1. **¿Por qué devolver `cursos/lista` desde `@Controller` no escribe ese texto en la respuesta?**
   Porque en un `@Controller` el `String` se interpreta como *nombre lógico de vista*. El `ViewResolver` lo convierte en `templates/cursos/lista.html`, Thymeleaf lo procesa y lo que se envía es el HTML resultante. En un `@RestController` (o con `@ResponseBody`) sí se escribiría el valor directamente.

2. **Diferencia entre `${curso.titulo}` y `*{titulo}`.**
   `${...}` evalúa contra el contexto completo (Model, variables). `*{...}` evalúa contra el objeto seleccionado con `th:object`, así que no hay que repetir el nombre del objeto.

3. **¿Por qué `@{}` es mejor que concatenar rutas?**
   Respeta el context path del despliegue, codifica variables de ruta y parámetros, y centraliza la construcción de URLs, evitando enlaces rotos al cambiar el contexto.

4. **¿Qué problema de mantenimiento resuelve un fragmento?**
   Evita mantener copias de cabecera, pie y `<head>` en cada página: un cambio se hace en un solo archivo (`fragments/layout.html`).

5. **¿Por qué la búsqueda usa GET y conserva `q` en la URL?**
   Consultar no cambia estado (operación segura e idempotente), y la URL queda compartible, con marcador y con historial funcional.

6. **¿Qué riesgo introduce `th:utext` con contenido no confiable?**
   Inserta el contenido sin escapar, así que el navegador interpretaría marcado o scripts del usuario: riesgo de XSS. `th:text` escapa por defecto.

7. **¿Qué demuestra MockMvc y qué falta verificar en el navegador?**
   Demuestra rutas, nombre de vista, atributos del Model, contenido renderizado, estados HTTP y redirecciones, sin abrir puerto. No cubre teclado/foco, diseño responsivo, carga real de CSS/CDN ni la consola del navegador.

8. **¿Por qué no consultar el origen de datos desde una plantilla?**
   La plantilla solo presenta. Consultar ahí mezcla responsabilidades, dificulta las pruebas, oculta consultas repetidas y rompe la arquitectura cuando el origen cambie a base de datos (semana 9).
