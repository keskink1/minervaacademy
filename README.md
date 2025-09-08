# Minerva - Academy Management System

Minerva is a Spring Boot application designed for academies and educational institutions to manage students, teachers, and lessons. It includes authentication, role-based access, JWT-based security, and full CRUD operations for students, teachers, and lessons.

---

## Table of Contents

1. [Technologies](#technologies)
2. [Project Structure](#project-structure)
3. [Configuration](#configuration)
4. [Authentication & Authorization](#authentication--authorization)
5. [Endpoints](#endpoints)
    - [Authentication](#authentication)
    - [Students](#students)
    - [Teachers](#teachers)
    - [Lessons](#lessons)
    - [User](#user)
    - [Status](#status)
6. [Database & Migrations](#database--migrations)
7. [Auditing](#auditing)

---

## Technologies

- Java 17
- Spring Boot 3.5.5
- Spring Data JPA
- Spring Security
- JWT (JSON Web Tokens)
- MySQL 8
- Flyway for database migrations
- MapStruct for DTO mapping
- Spring Validation
- Swagger / OpenAPI (springdoc-openapi)
- Lombok

---

## Project Structure


---

## Configuration

### `pom.xml`

Includes dependencies for Spring Boot, JPA, Validation, Security, MySQL, JWT, MapStruct, Flyway, and Swagger/OpenAPI. Also includes plugins for Lombok and MapStruct annotation processing.

### `SecurityConfig.java`

- JWT-based stateless security
- Role-based endpoint access
- Password encoder using BCrypt
- Custom authentication provider
- CSRF disabled
- All requests authenticated except `/auth/login` and `/status`
- Access denied returns 403, unauthenticated returns 401

### `JwtConfig.java`

- Configurable secret and token expiration times
- Generates secret key for signing JWTs

---

## Authentication & Authorization

- Login via `/auth/login` returns access and refresh tokens (HTTP-only cookie for refresh token)
- Refresh token endpoint: `/auth/refresh`
- Logout endpoint: `/auth/logout`
- Change password endpoint: `/auth/change-password`
- Authenticated endpoints check roles (`ADMIN`, `STUDENT`, `TEACHER`)
- Users: `AppUser` base class with roles and auditing fields

---

## Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /auth/login | Login user and return JWT access token |
| POST | /auth/refresh | Refresh JWT token using refresh token cookie |
| POST | /auth/logout | Logout user, delete refresh token cookie |
| POST | /auth/change-password | Change current user's password |
| GET | /auth/me | Get current user's info |

### Students

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| GET | /api/students/getAll | ADMIN | Get all students |
| GET | /api/students/getAllActive | ADMIN | Get all active students |
| GET | /api/students/{id} | ADMIN | Get student by ID |
| POST | /api/students/{studentId}/lessons/{lessonId} | ADMIN | Assign a lesson to a student |
| DELETE | /api/students/{studentId}/lessons/{lessonId} | ADMIN | Remove a lesson from a student |
| PUT | /api/students/{id} | ADMIN | Update student info |
| PATCH | /api/students/{id} | ADMIN | Freeze student |
| DELETE | /api/students/{id} | ADMIN | Delete student |
| POST | /api/students/{id}/notes | ADMIN | Add teacher note to student |
| DELETE | /api/students/{studentId}/notes/{teacherId} | ADMIN | Delete teacher note |
| GET | /api/students/{id}/notes | ADMIN | Get all teacher notes for a student |

### Teachers

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| GET | /api/teachers/getAll | ADMIN | Get all teachers |
| GET | /api/teachers/getAllActive | ADMIN | Get all active teachers |
| GET | /api/teachers/{teacherId}/lessons | ADMIN | Get all lessons of a teacher |
| POST | /api/teachers/{teacherId}/lessons/{lessonId} | ADMIN | Assign a lesson to a teacher |
| PUT | /api/teachers/{id} | ADMIN | Update teacher info |
| PATCH | /api/teachers/{id} | ADMIN | Freeze teacher |
| DELETE | /api/teachers/{id} | ADMIN | Delete teacher |
| DELETE | /api/teachers/{teacherId}/lessons/{lessonId} | ADMIN | Remove a lesson from a teacher |

### Lessons

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| GET | /api/lessons/getAll | ADMIN | Get all lessons |
| GET | /api/lessons/{id} | ADMIN | Get lesson by ID |
| POST | /api/lessons/create | ADMIN | Create a new lesson |
| DELETE | /api/lessons/{id} | ADMIN | Delete lesson |

### User (Self-Service)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /users/register/teacher | Register a new teacher |
| POST | /users/register/student | Register a new student |
| PUT | /users/students/me | Update current student info |
| PUT | /users/teachers/me | Update current teacher info |
| GET | /users/week-hours | Get weekly hours of current user |

### Status

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /status | Returns application status message |

---

---

## **Create Student Example JSON**

This is an example request body for creating a new student using `CreateStudentRequestDto`:

```json
{
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@student.com",
  "password": "StrongPassword123!",
  "role": "STUDENT",
  "enrollmentNumber": "20251234",
  "dateOfBirth": "2005-08-15"
}
```
---

### **Create Teacher Example**

```json
{
  "firstName": "Jane",
  "lastName": "Doe",
  "email": "jane.doe@school.com",
  "password": "StrongPassword123!",
  "phoneNumber": "+34123456789"
}

```

---
## Database & Migrations

- MySQL is used as the database.
- Flyway handles versioned migrations.
- Entities are audited with `@CreatedDate`, `@LastModifiedDate`, `@CreatedBy`, `@LastModifiedBy`.
- Tables: `users` (with inheritance for students and teachers), `lessons`, `student_lesson`, `teacher_lesson`, etc.

---

## **Auditing**

- Tracks `createdAt`, `createdBy`, `updatedAt`, `updatedBy` for all entities.
- Default `createdBy` and `updatedBy` come from the authenticated user.
- If no authenticated user is present, `"System admin"` is used.

```java
@Component("auditorProvider")
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of("System admin");
        }

        return Optional.of(authentication.getPrincipal().toString());
    }
}

