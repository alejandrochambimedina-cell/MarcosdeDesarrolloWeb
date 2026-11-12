# Laboratorio 14 – TechLab

## Descripción

TechLab es una aplicación web desarrollada con Spring Boot para la gestión de cursos. El proyecto utiliza una arquitectura basada en Spring MVC, persistencia de datos con JPA y una base de datos MySQL.

Incluye configuración de seguridad, validación de datos, migraciones de base de datos y monitoreo del estado de la aplicación.

## Tecnologías utilizadas

- Java 25
- Spring Boot 4.1.2
- Maven
- Spring MVC
- Thymeleaf
- Spring Data JPA
- MySQL 8.4
- Flyway
- Spring Security
- Spring Boot Actuator
- Docker Compose

## Requisitos previos

Antes de ejecutar el proyecto, es necesario contar con:

- Java 25 o superior.
- Docker Desktop con Docker Compose.
- Git.
- Un entorno de terminal.

## Estructura del proyecto

```text
Laboratorio 14/
├── README.md
└── Jorge_Gutierrez/
    └── demo/
        ├── src/
        │   ├── main/
        │   └── test/
        ├── compose.yaml
        ├── mvnw
        ├── mvnw.cmd
        └── pom.xml
```

## Configuración del entorno

Ingresar a la carpeta del proyecto:

```bash
cd "Laboratorio 14/Jorge_Gutierrez/demo"
```

Crear un archivo `.env` en la raíz del proyecto con las siguientes variables:

```env
DB_USER=techlab
DB_PASSWORD=tu_clave_segura
MYSQL_ROOT_PASSWORD=tu_clave_root
```

Reemplazar los valores de ejemplo por las credenciales que se utilizarán localmente.

**Importante:** El archivo `.env` contiene información privada y no debe subirse al repositorio.

## Ejecución del proyecto

### 1. Iniciar la base de datos

Levantar el contenedor de MySQL mediante Docker Compose:

```bash
docker compose up -d
```

La base de datos estará disponible en el puerto `3307` del equipo local.

### 2. Ejecutar la aplicación

En Git Bash, cargar las variables de entorno:

```bash
set -a
source .env
set +a
```

Ejecutar Spring Boot:

```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en:

http://localhost:8080

### 3. Verificar el estado de la aplicación

Spring Boot Actuator permite consultar el estado de salud mediante:

http://localhost:8080/actuator/health

## Base de datos y migraciones

El proyecto utiliza MySQL como sistema de gestión de base de datos.

Flyway administra las migraciones ubicadas en:

```text
src/main/resources/db/migration/
```

Hibernate utiliza la estrategia `validate` para comprobar que la estructura de la base de datos coincida con las entidades de la aplicación.

## Pruebas

Para ejecutar las pruebas automatizadas:

```bash
./mvnw test
```

## Detener los servicios

Para detener el contenedor de MySQL:

```bash
docker compose down
```

Los datos se conservan en el volumen persistente `techlab_mysql_data_lab14`.

## Autor

Jorge Gutierrez

Laboratorio 14 – Marcos de Desarrollo Web.
