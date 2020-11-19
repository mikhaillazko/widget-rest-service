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

There are two implementation of WidgetRepository

**Widget REST API endpoints**:

- POST /api/widgets/            : Create widget
- GET /api/widgets/{id}         : Retrieve widget
- PUT /api/widgets/{id}         : Update widget
- DELETE /api/widgets/{id}      : Delete widget
- GET /api/widgets?page=&size=  : Get list widgets with pagination
- GET /api-docs                 : Open API docs

`InWidgetDTO{x integer, y integer, z integer, width	integer, height	integer}`

`OutWidgetDTO{id long, x integer, y integer, z integer, width integer, height integer, updatedAt datetime}`

_Error Statuses_: 400, 404

It's the result of my express course Java and Spring. Feel free to create issues)