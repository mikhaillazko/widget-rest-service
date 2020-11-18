# widget-rest-service
Widget service with REST API

**Stack:**
- Java 11
- Spring Boot
- Maven
- TestNG

**Project Structure:**
- Application - REST API layer
- Domain - business logic layer
- Infrastructure - implementation of persistence layer

**Widget REST API endpoints**:

- POST /api/widgets/
- GET /api/widgets/{id}
- PUT /api/widgets/{id}
- DELETE /api/widgets/{id}
- GET /api/widgets

`InWidgetDTO{x integer, y integer, z integer, width	integer, height	integer}`

`OutWidgetDTO{id long, x integer, y integer, z integer, width integer, height integer, updatedAt datetime}`

_Error Statuses_: 400, 404