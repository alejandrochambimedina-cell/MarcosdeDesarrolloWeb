# TechLab Web — Semana 5

## Descripción
Migración del portal TechLab (APF1, Bootstrap/HTML/CSS/JS) desde un servidor de
desarrollo (Live Server) a una aplicación Java ejecutable con Spring Boot. Esta
semana no incorpora controladores, base de datos ni seguridad: el frontend
estático se sirve desde el classpath (`static/`) y el único endpoint activo es
`/actuator/health`.

## Requisitos
- JDK 25 (LTS) instalado y activo (`java --version`, `JAVA_HOME`).
- Conexión a internet en la primera ejecución, para que Maven Wrapper
  descargue las dependencias.
- No se requiere Maven ni Tomcat instalados aparte: el wrapper y el starter
  `webmvc` los resuelven.

## Ejecución
```
# Windows
.\mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```
Luego abrir `http://localhost:8080/`.

## Pruebas
```
# Windows
.\mvnw.cmd test

# macOS / Linux
./mvnw test
```
Último resultado: **BUILD SUCCESS**, `contextLoads` pasa (1/1 pruebas, 0 fallos).

## Empaquetado
```
# Windows
.\mvnw.cmd clean package

# macOS / Linux
./mvnw clean package

# Todos los sistemas
java -jar target/techlab-web-0.0.1-SNAPSHOT.jar
```
JAR generado: `target/techlab-web-0.0.1-SNAPSHOT.jar`.

## Perfiles
| Perfil | Puerto | Activación |
|---|---|---|
| Base (sin perfil) | 8080 | `java -jar target/techlab-web-0.0.1-SNAPSHOT.jar` |
| `local` | 8081 | `java -jar target/techlab-web-0.0.1-SNAPSHOT.jar --spring.profiles.active=local` |

`application-local.properties` es un perfil de **desarrollo**: solo fija
`server.port=8081` y no contiene secretos ni datos sensibles.

## Alcance
Frontend estático completo (APF1) servido por Spring Boot. No incluye
controladores, persistencia ni seguridad — eso corresponde a la semana 6 en
adelante.

## Evidencias
Capturas y registros en `evidencias/` (fuera de `static`, para que no se
publiquen como recurso web). Incluye: versión de Java, arranque en 8080,
`/actuator/health` en `UP`, log de `StartupReporter`, resultado de `mvnw test`,
tamaño del JAR, y las dos ejecuciones del perfil (8080 y 8081).

### Matriz de pruebas

| ID | Procedimiento | Resultado esperado | Resultado real | Estado |
|----|---|---|---|---|
| CP-01 | `java --version` | JDK activo 25 | | Pasa / Falla |
| CP-02 | Ejecutar el proyecto base con Maven Wrapper | BUILD inicia y Tomcat escucha en 8080 | | Pasa / Falla |
| CP-03 | Abrir la raíz en el navegador | HTTP 200 y portal TechLab visible | | Pasa / Falla |
| CP-04 | Revisar CSS, JS, imágenes y CDN en Network | Sin 404 ni solicitudes bloqueadas esenciales | | Pasa / Falla |
| CP-05 | Repetir navbar, modal y formulario | El APF1 conserva su comportamiento | | Pasa / Falla |
| CP-06 | Consultar `/actuator/health` | HTTP 200 y status UP sin detalles internos | | Pasa / Falla |
| CP-07 | Revisar el log después del inicio | StartupReporter informa nombre y puerto | | Pasa / Falla |
| CP-08 | Ejecutar `mvnw test` | BUILD SUCCESS y cero fallos | | Pasa / Falla |
| CP-09 | Ejecutar `mvnw clean package` y luego `java -jar` | El JAR inicia y sirve el portal | | Pasa / Falla |
| CP-10 | Activar el perfil local | El mismo JAR escucha en 8081 | | Pasa / Falla |
| CP-11 | Ejecutar sin perfil después del reto | La aplicación vuelve a 8080 | | Pasa / Falla |
| CP-12 | Revisar `git status` | No hay `target`, secretos ni archivos del IDE versionados | | Pasa / Falla |

> Completa la columna "Resultado real" y el estado al ejecutar cada caso en tu
> propia máquina; son evidencia personal y no se pueden generar por adelantado.

## Créditos
Proyecto académico — Universidad Tecnológica del Perú (UTP), curso Marcos de
Desarrollo Web. Imagen de `assets/img/frontend.webp` de marcador de posición;
reemplázala por el recurso real del APF1 y ajusta su crédito aquí si aplica.

## Respuestas a la comprobación conceptual

1. **¿Qué responsabilidad añade Spring Boot al portal construido con Bootstrap?**
   Agrega un proceso Java con servidor embebido (Tomcat) que administra el
   ciclo de vida de la aplicación, sirve los recursos estáticos desde el
   classpath y expone capacidades operativas (salud, logging), sin reemplazar
   el HTML/CSS/JS que sigue interpretando el navegador.
2. **¿Por qué Spring Boot no reemplaza a Spring Framework?**
   Spring Boot se apoya en Spring Framework (IoC, inyección de dependencias,
   MVC); lo que aporta es arranque, autoconfiguración, starters y servidor
   embebido para operarlo más rápido, no un contenedor alternativo.
3. **¿Qué tres capacidades concentra `@SpringBootApplication`?**
   `@SpringBootConfiguration` (fuente de configuración), `@EnableAutoConfiguration`
   (activa configuraciones según dependencias presentes) y `@ComponentScan`
   (busca componentes desde el paquete de la clase principal hacia abajo).
4. **¿Por qué la clase principal debe ubicarse en un paquete raíz?**
   Porque `@ComponentScan` explora ese paquete y sus subpaquetes; si la clase
   principal quedara en un paquete lateral, componentes como `StartupReporter`
   (en `pe.edu.utp.techlab.bootstrap`) no serían detectados.
5. **¿Qué diferencia existe entre un starter y una dependencia aislada?**
   Un starter agrupa un conjunto coherente y versionado de dependencias para
   una capacidad completa (p. ej. `webmvc` trae Spring MVC y Tomcat
   compatibles); una dependencia aislada solo aporta una librería puntual sin
   garantizar compatibilidad con el resto del stack.
6. **¿Por qué no debe declararse la versión de Spring Framework en el POM?**
   Porque el *parent* `spring-boot-starter-parent` ya fija un conjunto de
   versiones probadas entre sí (Spring Framework, Tomcat, Jackson, etc.);
   forzar una versión individual puede romper esa compatibilidad.
7. **¿Qué problema evita Maven Wrapper en un equipo de estudiantes?**
   Evita que cada persona use una versión distinta de Maven instalada
   localmente; el wrapper descarga y fija una versión común por proyecto,
   haciendo el build reproducible sin instalación global.
8. **¿Por qué `index.html` funciona en la raíz sin un controlador?**
   Porque Spring Boot trata `src/main/resources/static` como classpath público
   y usa `index.html` como página de bienvenida automática cuando existe.
9. **¿Qué demuestra `contextLoads` y qué no demuestra todavía?**
   Demuestra que el `ApplicationContext` se puede construir sin errores de
   configuración o beans (prueba de humo). No demuestra comportamiento
   funcional de endpoints ni de la interfaz, porque aún no hay controladores.
10. **¿Qué diferencia existe entre `spring-boot:run` y `java -jar`?**
    `spring-boot:run` compila y ejecuta desde el código fuente vía Maven,
    útil en desarrollo; `java -jar` ejecuta el artefacto ya empaquetado,
    demostrando que el build es autosuficiente y reproducible fuera del IDE.
11. **¿Por qué se expone únicamente `health` en esta práctica?**
    Porque es el único endpoint necesario para verificar disponibilidad, y
    limitar `management.endpoints.web.exposure.include` evita filtrar
    información interna de la aplicación en un entorno público.
12. **¿Cómo demuestra `StartupReporter` la IoC y la inyección por constructor?**
    No crea su propia instancia de `Environment`; la recibe como parámetro del
    constructor, y es Spring quien construye el bean y le entrega esa
    dependencia ya resuelta, delegando el control de creación al contenedor.
13. **¿Qué ventaja aporta cambiar el puerto con un perfil en vez de editar el código?**
    Permite alternar configuración según el entorno (local, pruebas,
    producción) sin tocar el código fuente ni reconstruir el JAR; el mismo
    artefacto sirve para ambos casos.
14. **¿Qué partes del proyecto actual serán reemplazadas o extendidas al
    incorporar Spring MVC?**
    La entrega de `index.html` seguirá siendo estática, pero se añadirán
    controladores (`@RestController`/`@Controller`) y servicios que expondrán
    rutas dinámicas (p. ej. `/api/v1/cursos`) consumidas desde JavaScript,
    sin tocar `TechLabWebApplication` ni `StartupReporter`.
