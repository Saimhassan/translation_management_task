Translation Management System

This project is a Spring Boot-based Translation Management System that supports locale-based translations, API documentation, security with JWT, test coverage, and Docker support.


Project Setup

1. Project Initialization
The project was created using Spring Initializr.

Java Version: 17

Build Tool: Maven

Spring Boot Version: 3.5.3

2. Database & ORM Configuration

JPA is used as the Object-Relational Mapping (ORM) framework.

MySQL is used as the relational database.

Necessary dependencies for JPA and MySQL are included in pom.xml.

3. Application Configuration

Database configurations are specified in application.properties.

The application runs on port 8080 by default. To change it, modify the server.port property in application.properties.

Architecture

4. Architectural Pattern
The project follows the Three-Tier Architecture Pattern:

Controller Layer – Handles HTTP requests

Service Layer – Contains business logic

Repository Layer – Handles data access

5. Entity Management

Required entities are created under the entity package.

The spring.jpa.hibernate.ddl-auto property is configured to automatically create or update database tables.

Tools & Libraries

6. MapStruct

Used for mapping between entities and DTOs, promoting clean separation between persistence and representation layers.

7. API Documentation

SpringDoc OpenAPI is used for Swagger UI support.

Relevant dependency is added to pom.xml.

8. Security

Spring Security is integrated for securing APIs.

JWT (JSON Web Token) support is added to encode and decode tokens.

Required dependencies for JWT handling are included.

9. Testing

Spring Boot Starter Test is used for unit and integration testing.

TestMe plugin is used to auto-generate test code for service and repository layers.

10. Code Coverage

SonarQube plugin is integrated for static code analysis and test coverage reporting.

11. Docker Support

A Dockerfile is included in the project root to enable Docker containerization of the application.
