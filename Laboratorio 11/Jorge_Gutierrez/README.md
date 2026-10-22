\# Laboratorio 11 - Gestión de cursos



\*\*Estudiante:\*\* Jorge Gutierrez  

\*\*Proyecto:\*\* Sistema de gestión de cursos TechLab



\## Descripción



Aplicación web desarrollada con Spring Boot para administrar cursos y sus categorías. Incluye una interfaz web con Thymeleaf y una API REST para realizar operaciones CRUD.



\## Tecnologías utilizadas



\- Java 21 como versión de compilación.

\- Spring Boot 4.1.1.

\- Spring Data JPA e Hibernate.

\- MySQL 8.4.

\- Flyway para migraciones de base de datos.

\- Thymeleaf para las vistas web.

\- Jakarta Validation para validar los datos.

\- Maven para gestionar dependencias y ejecutar pruebas.

\- JUnit y Mockito para las pruebas automatizadas.

\- Docker Compose para ejecutar MySQL.



\## Funcionalidades



\- Registrar, consultar, actualizar y eliminar cursos.

\- Asociar cada curso con una categoría.

\- Validar títulos y horas de duración.

\- Evitar títulos duplicados.

\- Mostrar errores de validación en formularios y respuestas de la API.

\- Consultar cursos mediante la API REST.

\- Mostrar información de los cursos mediante DTO.



\## Estructura del proyecto



\- `src/main/java`: código fuente de la aplicación.

\- `src/main/resources`: configuración, plantillas, mensajes y migraciones Flyway.

\- `src/test/java`: pruebas automatizadas.

\- `compose.yaml`: configuración de MySQL con Docker Compose.

\- `pom.xml`: dependencias y configuración de Maven.



\## Configuración y ejecución



1\. Instalar Java 21 y Docker.

2\. Configurar las variables de entorno a partir de `.env.example`, usando credenciales propias.

3\. Iniciar la base de datos:



&#x20;  ```bash

&#x20;  docker compose up -d

&#x20;  ```



4\. Ejecutar las pruebas:



&#x20;  ```bash

&#x20;  ./mvnw test

&#x20;  ```



5\. Iniciar la aplicación:



&#x20;  ```bash

&#x20;  ./mvnw spring-boot:run

&#x20;  ```



6\. Abrir `http://localhost:8080`.



\## API REST



La API utiliza la ruta base `/api/v1/cursos`.



| Método | Endpoint | Operación |

|---|---|---|

| GET | `/api/v1/cursos` | Listar cursos |

| GET | `/api/v1/cursos/{id}` | Consultar un curso |

| POST | `/api/v1/cursos` | Registrar un curso |

| PUT | `/api/v1/cursos/{id}` | Actualizar un curso |

| DELETE | `/api/v1/cursos/{id}` | Eliminar un curso |



\## Pruebas



Se ejecutaron ocho pruebas automatizadas, con cero fallos y cero errores. La verificación completa con Maven finalizó correctamente con `BUILD SUCCESS`.



\## Seguridad



El archivo `.env` no se incluye en el repositorio. Configura tus propias credenciales locales antes de ejecutar el proyecto.



