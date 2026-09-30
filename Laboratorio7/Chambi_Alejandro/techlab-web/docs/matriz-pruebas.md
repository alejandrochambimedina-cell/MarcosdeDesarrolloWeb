# Matriz de pruebas manuales — Semana 7

Completa "Resultado obtenido" y "OK" al ejecutar cada caso en el navegador (pestaña Network abierta).

| Caso | Acción | Resultado esperado | Estado esperado | Resultado obtenido | OK |
|------|--------|--------------------|-----------------|--------------------|----|
| M1 | GET /cursos | Tres tarjetas y HTML sin expresiones Thymeleaf | 200 | | ☐ |
| M2 | GET /cursos?q=Spring | Una tarjeta y campo con "Spring" | 200 | | ☐ |
| M3 | GET /cursos?q=NoExiste | Estado vacío y cero resultados | 200 | | ☐ |
| M4 | GET /cursos/2 | Detalle de Bootstrap con 16 horas | 200 | | ☐ |
| M5 | GET /cursos/999 | Vista 4xx y ruta informada | 404 | | ☐ |
| M6 | q con 61 caracteres | Solicitud rechazada | 400 | | ☐ |
| M7 | GET /api/v1/cursos | JSON de tres elementos | 200 | | ☐ |
| M8 | GET /portal | Redirección a /cursos | 302 | | ☐ |
| M9 (reto) | GET /cursos/resumen | 3 cursos y 48 horas | 200 | | ☐ |

## Responsivo y accesibilidad
- [ ] 320 px sin scroll horizontal
- [ ] 768 px sin scroll horizontal
- [ ] 1280 px sin scroll horizontal
- [ ] Navegación solo con Tab / Shift+Tab, foco visible
- [ ] "Saltar al contenido" aparece con foco y funciona
- [ ] `label` asociado a `q`; encabezados en orden lógico
- [ ] Zoom 200 % legible y operable
- [ ] Sin CDN: el contenido semántico se mantiene

## Evidencias sugeridas (guardar en docs/evidencias/)
1. `/cursos` con tres tarjetas
2. `/cursos?q=Spring` (URL visible)
3. `/cursos/2`
4. `/cursos/999` con 404 en Network
5. `/api/v1/cursos` en JSON
6. Ver código fuente de `/cursos` (sin `th:*`)
7. `/cursos/resumen`
8. Resumen de `./mvnw clean test` con 0 fallos
