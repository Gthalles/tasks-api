# Task API

Simple REST API for creating, updating, listing and deleting tasks. Built with Spring Boot, JPA and MySQL; automated tests run on an in-memory H2 database.

## Features
- CRUD for tasks (`description`, `finished`).
- Validation with Jakarta Bean Validation and explicit business rules in the entity.
- Global error handling for validation errors, bad input and missing records.
- OpenAPI docs via springdoc (`/swagger-ui.html`).
- Test profile that runs against H2; default runtime profile uses MySQL.

## Tech Stack
- Java 17
- Spring Boot 3.3.5 (Web, Data JPA, Validation)
- MySQL 8.4 (default), H2 (tests)
- Maven Wrapper

## Requirements
- JDK 17+
- Maven 3.9+ (or use the provided `./mvnw`)
- Docker (optional, for local MySQL)

## Quick Start (MySQL default)
1. Start MySQL (Docker): `docker compose up -d mysql`
2. Check credentials in [`src/main/resources/application.yaml`](/Users/thallesgarbelotti/Desktop/todo-list/src/main/resources/application.yaml).
3. Run the app: `./mvnw spring-boot:run`
4. API base URL: `http://localhost:8080`
5. Swagger UI: `http://localhost:8080/swagger-ui.html` (spec at `/v3/api-docs`)

### Running with H2 (tests or local without MySQL)
- Use the `test` profile: `SPRING_PROFILES_ACTIVE=test ./mvnw spring-boot:run`
- Profile config: [`src/test/resources/application-test.yaml`](/Users/thallesgarbelotti/Desktop/todo-list/src/test/resources/application-test.yaml).

## Configuration
Key settings (default `application.yaml`):
- `spring.datasource.url`: `jdbc:mysql://localhost:3306/task_db`
- `spring.datasource.username`: `task_user`
- `spring.datasource.password`: `task_pass`
- `spring.jpa.hibernate.ddl-auto`: `update`

Docker MySQL credentials (from `docker-compose.yaml`):
- DB: `task_db`
- User/password: `task_user` / `task_pass`
- Root password: `root_pass`

## API
Endpoints use JSON.

- GET `/tasks` – list all tasks.
- GET `/tasks/{id}` – get a single task.
- POST `/tasks` – create. Body: `{"description":"Write docs","finished":false}` (only `description` is required).
- PATCH `/tasks/{id}` – update description and/or finished flag. Body: `{"description":"Reviewed docs","finished":true}`.
- DELETE `/tasks/{id}` – delete a task.

Example (create):
```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"description":"Write README","finished":false}'
```

## Tests
- Unit, controller, service and integration suites: `./mvnw test`
- Tests run with profile `test` against H2; database is created and dropped automatically.

## Project Layout
- [`src/main/java/com/thallesgarbelotti/task`](/Users/thallesgarbelotti/Desktop/todo-list/src/main/java/com/thallesgarbelotti/task) – application code (entity, repository, service, controller, exception handler).
- [`src/test/java/com/thallesgarbelotti/task`](/Users/thallesgarbelotti/Desktop/todo-list/src/test/java/com/thallesgarbelotti/task) – unit and integration tests.
- [`docker-compose.yaml`](/Users/thallesgarbelotti/Desktop/todo-list/docker-compose.yaml) – MySQL for local dev.

## Logging & Debug
- SQL logging is enabled (`org.hibernate.SQL=DEBUG`). Uncomment `org.hibernate.orm.jdbc.bind` in `application.yaml` to inspect parameter binding when needed.
