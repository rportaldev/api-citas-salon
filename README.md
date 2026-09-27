# API Citas Salón

API REST de un sistema de citas para un salón de belleza, desarrollada con Spring Boot. Reutiliza y amplía la autenticación JWT y roles construidos en los proyectos [api-auth-jwt](https://github.com/rportaldev/api-auth-jwt) y [api-pedidos-ecommerce](https://github.com/rportaldev/api-pedidos-ecommerce).

## Descripción

Permite a un salón gestionar su catálogo de servicios y profesionales, y a los clientes agendar citas combinando uno o más servicios con un profesional capacitado para todos ellos. El sistema valida automáticamente disponibilidad de horario y capacidad del profesional antes de confirmar una cita.

El diseño de este proyecto se definió mediante una entrevista de requisitos simulada con un cliente ficticio (dueño de un salón), antes de escribir código, como ejercicio de análisis de requisitos.

## Funcionalidades

- Registro e inicio de sesión con JWT.
- Tres roles: `ROLE_ADMIN` (dueño), `ROLE_PROFESIONAL` (estilistas), `ROLE_CLIENTE`.
- Gestión de catálogo de servicios (solo `ROLE_ADMIN`): nombre, duración, precio.
- Búsqueda de profesionales disponibles según los servicios solicitados — solo se muestran los que dominan **todos** los servicios pedidos.
- Creación de citas combinando varios servicios con un único profesional; el sistema calcula automáticamente la duración total y el precio total.
- Validación de cruce de horarios: un profesional no puede tener dos citas confirmadas que se solapen.
- Cancelación de citas, permitida hasta 2 horas antes de la hora agendada.
- Autorización por dueño del recurso: cada cliente ve solo sus propias citas; cada profesional ve solo las suyas.
- Manejo centralizado de errores con respuestas JSON estructuradas.
- Pruebas unitarias con JUnit y Mockito para la lógica de negocio más compleja (`CitaService`), incluyendo cruce de horarios, capacidad del profesional y reglas de cancelación con fechas relativas.

## Tecnologías

Java 17 · Spring Boot 3.4.10 · Spring Security · Spring Data JPA · PostgreSQL · JJWT · JUnit 5 · Mockito · Lombok · Maven

## Modelo de datos

```
Usuario (1) ──── (0 o 1) Profesional (muchos) ──── (muchos) Servicio
   │                          │
   │ (1)                      │ (1)
   ▼                          ▼
(muchos) Cita ──────────────── (muchos) Servicio
```

Un `Usuario` puede o no tener una fila asociada en `Profesional` (relación `@OneToOne` opcional) — solo los usuarios con rol `PROFESIONAL` la tienen. `Profesional` y `Cita` se relacionan con `Servicio` mediante `@ManyToMany`.

## Endpoints

### Autenticación (públicos)

| Método | Endpoint          | Descripción                    |
|--------|-------------------|----------------------------------|
| POST   | `/auth/register`  | Registra un nuevo usuario (rol `CLIENTE` por defecto) |
| POST   | `/auth/login`     | Inicia sesión y genera un JWT    |

### Usuarios

| Método | Endpoint            | Rol requerido | Descripción                    |
|--------|----------------------|---------------|----------------------------------|
| GET    | `/usuarios/perfil`  | Cualquiera    | Perfil del usuario autenticado   |

### Servicios

| Método | Endpoint          | Rol requerido | Descripción                    |
|--------|-------------------|---------------|----------------------------------|
| GET    | `/servicios`      | Cualquiera    | Lista el catálogo de servicios   |
| POST   | `/servicios`      | `ADMIN`       | Crea un servicio                 |
| PUT    | `/servicios/{id}` | `ADMIN`       | Actualiza un servicio            |
| DELETE | `/servicios/{id}` | `ADMIN`       | Elimina un servicio              |

### Profesionales

| Método | Endpoint                    | Rol requerido | Descripción                    |
|--------|------------------------------|---------------|----------------------------------|
| GET    | `/profesionales`            | Cualquiera    | Lista todos los profesionales    |
| GET    | `/profesionales/disponibles?servicioIds=1,3` | Cualquiera | Filtra profesionales que dominan todos los servicios indicados |

### Citas

| Método | Endpoint            | Rol requerido | Descripción                        |
|--------|----------------------|---------------|--------------------------------------|
| POST   | `/citas`            | Cualquiera    | Crea una cita (valida capacidad y horario) |
| DELETE | `/citas/{id}`       | Cualquiera    | Cancela una cita propia (hasta 2h antes) |
| GET    | `/citas/mias`       | Cualquiera    | Lista las citas del usuario autenticado |
| GET    | `/citas/profesional`| `PROFESIONAL` | Lista las citas del profesional autenticado |
| GET    | `/citas`            | `ADMIN`       | Lista todas las citas               |

**Códigos de error manejados:** 400 (datos inválidos), 401 (sin token o credenciales incorrectas), 403 (sin permisos), 404 (recurso no encontrado), 409 (correo duplicado, horario no disponible, profesional no capacitado, cancelación fuera de tiempo).

## Configuración

Edita `src/main/resources/application.properties` (usa `application.properties.example` como plantilla):

```
spring.datasource.url=jdbc:postgresql://localhost:5432/db_citas_salon
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password

jwt.secret=tu_clave_secreta_de_al_menos_32_caracteres
jwt.expiration=86400000

server.port=8084
```

## Cómo ejecutar el proyecto

1. Clona el repositorio:

   git clone https://github.com/rportaldev/api-citas-salon.git

2. Crea la base de datos en PostgreSQL:

   CREATE DATABASE db_citas_salon;

3. Copia `application.properties.example` a `application.properties` y configura tus credenciales.
4. Ejecuta la aplicación desde Spring Tool Suite (clic derecho sobre el proyecto → Run As → Spring Boot App), o desde consola:

   ./mvnw spring-boot:run

5. La API estará disponible en `http://localhost:8084`.

## Pruebas

**Unitarias:** `CitaServiceTest`, con JUnit 5 y Mockito — cubre creación de citas exitosa, validaciones de existencia, capacidad del profesional, cruce de horarios, y las 3 reglas de cancelación (éxito, fuera de tiempo, y protección de dueño), usando fechas relativas a la hora de ejecución para evitar pruebas frágiles.

**Manuales (Postman):** flujo completo de registro, login, gestión de catálogo, búsqueda de profesionales disponibles, creación de citas, y validación de las reglas de negocio (capacidad, cruce de horario, cancelación).

## Autor

Ricardo — Estudiante de Desarrollo de Software
Cibertec Perú — 4to ciclo
