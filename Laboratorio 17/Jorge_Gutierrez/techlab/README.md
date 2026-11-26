# TechLab — Marcos de Desarrollo Web

Proyecto integrador didáctico. Java 25, Spring Boot 4.1.2 (baseline del curso), Maven 3.9+, Thymeleaf, Bootstrap 5.3.8, JPA, MySQL 8.4, Flyway, Spring Security, JWT HS256. Antes de uso público, actualizar a versiones soportadas y verificar dependencias/vulnerabilidades. No es una plataforma productiva de identidad.

## Prototipo estático

```bash
cd prototype
python3 -m http.server 8000 --bind 127.0.0.1
```

Abrir http://127.0.0.1:8000. Bootstrap usa CDN; requiere conectividad. Detener con Ctrl+C; regresar a raíz.

## Desarrollo (Linux/macOS, Bash)

Necesitas JDK 25, Maven, Docker Compose v2+, OpenSSL, Python 3. Node 22+ y PyYAML solo para comprobaciones locales.

```bash
bash scripts/init-env.sh    # solo la primera vez
# cargar variables sin imprimir valores
set -a; source .env; set +a
docker compose up -d --wait db
mvn clean verify
mvn spring-boot:run
```

Abrir http://127.0.0.1:8080. Usuarios `user` y `admin`; consultar sus contraseñas en `.env` de forma local. No publicar dicho archivo. H2 en pruebas Maven; MySQL durante ejecución real. Para verificar la instancia en otra terminal:

```bash
set -a; source .env; set +a
python3 scripts/smoke.py
```

El script comprueba JWT, rechazo de credenciales/claims incorrectos, permisos, CRUD, login web, rechazo sin CSRF y logout. Crea y elimina un registro temporal. Usar en entorno de laboratorio únicamente.

## Empaquetado y despliegue local

Detener el proceso Maven antes de iniciar el JAR o contenedor (mismo puerto).

```bash
mvn clean verify
java -jar target/techlab-1.0.0.jar
# O bien (desde raíz, con .env existente):
docker compose -f compose.yml -f compose.deploy.yml up -d --build
```

Verificar logs y smoke. `docker compose -f compose.yml -f compose.deploy.yml down` conserva volumen. No usar `down -v` salvo eliminación intencional de todos los datos.

## Validación local sin Spring

```bash
bash scripts/check-local.sh
```

Requiere JDK, Node y Python con PyYAML. Comprueba reglas de negocio, normalización/búsqueda JavaScript, sintaxis JS, XML y YAML. No sustituye `mvn clean verify` ni las pruebas MySQL reales. Consulte `VALIDACION.txt` para los resultados efectivamente obtenidos.

## Seguridad

La sesión de navegador y la API Bearer están separadas. El JWT expira en 15 minutos, no se almacena en localStorage y la clave procede de `.env`. El login web utiliza el formulario por defecto de Spring Security. Los dos usuarios son en memoria, no registros persistidos; solo para el laboratorio. El cambio de roles no revoca tokens existentes. No exponer HTTP a Internet: usar proxy HTTPS, cookie Secure, gestión de secretos, identidad centralizada, rate limiting y observabilidad. Los puertos publicados se limitan a loopback. El contenedor usa un usuario no root. Las etiquetas de imágenes son de familia; fijar digests y actualizar regularmente en producción.
