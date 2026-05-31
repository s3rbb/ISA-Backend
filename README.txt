# Task Manager — Project Brief

## What this is
A full-stack task management app for a university final project (Internet Software Architectures course). Tech stack:
- **Backend:** Spring Boot 3.x REST API (Java 21, Maven)
- **Frontend:** React with Vite (separate project, separate folder, separate GitHub repo)
- **Database:** MySQL via XAMPP on localhost:3306
- **Auth:** JWT with access + refresh tokens

## Current state — already done, do NOT redo
- Spring Boot backend project generated via Spring Initializr
- Opens in IntelliJ, builds and runs successfully on port 8080
- Maven dependencies in pom.xml: Spring Web, Spring Data JPA, Spring Security, Validation, MySQL Driver, Lombok, Spring Boot DevTools
- `application.properties` already configured for MySQL
- MySQL database `taskmanager` exists in XAMPP (empty, no tables yet)
- Frontend does NOT exist yet — create it with Vite in a sibling folder

## Database connection (already set in application.properties)
- URL: `jdbc:mysql://localhost:3306/taskmanager`
- User: `root`
- Password: (empty)
- `ddl-auto=update` — Hibernate auto-creates tables from entities

## Data model — 5 entities

1. **User** — id, username (unique), password (BCrypt), email, role (USER/ADMIN)
2. **Project** — id, name, description, createdAt, owner (FK → User)
3. **Task** — id, title, description, status (TODO/IN_PROGRESS/DONE), priority (LOW/MEDIUM/HIGH), dueDate, project (FK → Project), assignee (FK → User)
4. **Comment** — id, text, createdAt, task (FK → Task), author (FK → User)
5. **Tag** — id, name, color

### Relations (grading requires both OneToMany AND ManyToMany)
- User → Project: OneToMany (owner)
- Project → Task: OneToMany
- User → Task: OneToMany (assignee)
- Task → Comment: OneToMany
- **Task ↔ Tag: ManyToMany** via `task_tags` join table

## REST Architecture
This is a RESTful API backend. Follow these principles strictly:
- All controllers use `@RestController` — no server-side rendering, no Thymeleaf
- Endpoints follow resource-based URL conventions: `GET /api/tasks`, `POST /api/tasks`, `PUT /api/tasks/{id}`, `DELETE /api/tasks/{id}`
- Use correct HTTP verbs: GET (read), POST (create), PUT/PATCH (update), DELETE (delete)
- Return appropriate HTTP status codes: 200 OK, 201 Created, 204 No Content, 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found
- API is stateless — no server-side sessions. Authentication is handled entirely via JWT tokens in the `Authorization: Bearer <token>` header on every request
- All request and response bodies are JSON (`@RequestBody`, `@ResponseBody`)
- Prefix all endpoints with `/api` (e.g. `/api/projects`, `/api/tasks`, `/api/auth/login`)

## Required features (graded)

**Backend:**
- Layered architecture: `controller / service / repository / entity / dto / config / security` packages
- Full CRUD endpoints for all 5 entities
- JWT auth endpoints: `/auth/register`, `/auth/login`, `/auth/refresh`
- Role-based authorization (USER vs ADMIN) using `@PreAuthorize` or `SecurityFilterChain` rules
- CORS configured for `http://localhost:5173` (Vite default)
- Passwords hashed with BCrypt
- DTOs for request/response — never expose entities directly
- Input validation with `@Valid` + Jakarta validation annotations
- JWT secret in `application.properties`, not hardcoded in Java

**Frontend:**
- Login + Register pages
- Store JWT in localStorage, attach via Axios request interceptor (`Authorization: Bearer ...`)
- **Automatic token refresh** — Axios response interceptor that catches 401, calls `/auth/refresh`, retries the original request
- Protected routes (redirect to /login if no token)
- CRUD pages for all entities (list + create + edit + delete)
- Conditional rendering based on role (admins see delete buttons; regular users don't)
- Use React Router for navigation, Axios for HTTP

**Documentation:**
- Postman collection with all endpoints, grouped in folders by resource, with saved example responses
- Will be published publicly via Postman after development

## Build order (follow strictly)
1. User entity + Spring Security config + JWT generation/validation → test register/login via Postman
2. Refresh token endpoint
3. Remaining 4 entities with JPA relations
4. Service + Controller layers with CRUD for all entities
5. Scaffold frontend: `npm create vite@latest taskmanager-frontend -- --template react`
6. Frontend: routing, login/register pages, Axios with auto-refresh interceptor
7. Frontend: CRUD pages for each entity
8. Postman collection cleanup with saved example responses

## Constraints — important
- Backend and frontend MUST live in separate folders and have separate GitHub repos (course requirement)
- Database is MySQL, not H2 — do not switch it
- No Thymeleaf, no server-side templating — frontend is pure React
- Do not modify the existing `application.properties` datasource lines
- Keep dependencies minimal — no Redux, no fancy UI libraries unless requested. Tailwind CSS is acceptable