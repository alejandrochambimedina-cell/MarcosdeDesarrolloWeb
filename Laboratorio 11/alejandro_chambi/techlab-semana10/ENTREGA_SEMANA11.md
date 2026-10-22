# Informe de entrega — Semana 11

## Objetivo
Ampliar el CRUD de TechLab con categorías relacionadas con cursos y validación consistente en formulario MVC, API REST y base de datos.

## Implementación incluida
- Entidad `Category` y relación `Course` → `Category` mediante `@ManyToOne(fetch = LAZY, optional = false)`.
- DTO de categoría, salida de curso con `categoriaId` y `categoriaNombre`, y entrada con restricciones Jakarta Validation.
- Repositorios con consultas de carga planificada usando `@EntityGraph` y `join fetch` para detalle.
- Servicio con validación de existencia de categoría, normalización de título y control de títulos duplicados.
- API REST con `@Valid` y errores `ProblemDetail` para validación, categoría/curso inexistente y conflictos.
- Formulario Thymeleaf con selección de categoría, mensajes de error junto a cada campo y conservación de valores enviados.
- Migración incremental Flyway y mensajes de validación/conversión externalizados.

## Matriz de comprobación

| Caso | Resultado esperado | Estado |
|---|---|---|
| Listado de cursos | Incluye nombre de categoría | Pendiente de ejecutar |
| Búsqueda por título | Devuelve coincidencias | Pendiente de ejecutar |
| Filtro por categoría | Filtra sin consulta por tarjeta | Pendiente de ejecutar |
| Título vacío | Error en campo, no persiste | Pendiente de ejecutar |
| Horas menores que 1 o mayores que 500 | Error de validación | Pendiente de ejecutar |
| Horas con texto no numérico en MVC | Mensaje de conversión | Pendiente de ejecutar |
| Categoría inexistente en REST | HTTP 400 y `errors.categoriaId` | Pendiente de ejecutar |
| Curso inexistente | HTTP 404 | Pendiente de ejecutar |
| Título duplicado | HTTP 409 | Pendiente de ejecutar |
| Integridad referencial SQL | FK impide categoría inexistente | Pendiente de ejecutar |

## Respuestas de cierre

1. **¿Por qué la clave foránea está en `cursos`?** Porque cada curso pertenece a una categoría; muchas filas de cursos pueden referenciar la misma fila de categorías. La columna FK va en el lado propietario, `Course`.
2. **¿Qué diferencia hay entre `optional = false` y `NOT NULL`?** `optional = false` expresa la obligatoriedad en el mapeo de objetos JPA; `NOT NULL` la impone en el esquema SQL incluso ante escrituras fuera del flujo de la aplicación.
3. **¿Por qué no se usa `CascadeType.REMOVE`?** Una categoría puede ser compartida por varios cursos. Eliminar un curso no debe borrar el catálogo de categorías ni otros cursos.
4. **¿Qué problema evita un DTO plano en JSON?** Evita exponer entidades/proxies JPA, ciclos de serialización y detalles internos de persistencia; entrega un contrato estable.
5. **¿Por qué `@Valid` aparece en `@ModelAttribute` y `@RequestBody`?** Ambos son puntos de entrada distintos: formulario MVC y cuerpo JSON REST. Cada uno debe validar el DTO recibido.
6. **¿Qué hace `BindingResult` y por qué importa su posición?** Recoge errores de enlace/validación para volver a mostrar el formulario con los valores y errores. Debe ir inmediatamente después del parámetro validado para asociarse con él.
7. **¿Por qué `exists` no reemplaza `UNIQUE`?** La consulta previa mejora el mensaje, pero dos solicitudes concurrentes pueden pasarla a la vez. La restricción única en la base es la última defensa.
8. **¿Cómo ayuda `EntityGraph` contra N+1?** Solicita que la categoría se cargue junto con los cursos del listado, evitando una consulta adicional por cada tarjeta.
9. **¿Qué capa decide si una categoría existe?** El servicio consulta el repositorio y aplica la regla de negocio; la FK de MySQL protege además la integridad referencial.
10. **¿Qué conservar para integrar MVC, JPA y MySQL en semana 12?** Migraciones aplicadas e inmutables, relación/DTO/validaciones coherentes, manejo de errores, consultas verificadas, pruebas reproducibles y configuración sin credenciales sensibles en el repositorio.

## Limitaciones de verificación
El paquete incluye pruebas de validación DTO y una lista de comprobación, pero no se afirma que las pruebas de integración ni las verificaciones con MySQL hayan sido ejecutadas. Deben correrse en el entorno con Docker/MySQL configurado.
