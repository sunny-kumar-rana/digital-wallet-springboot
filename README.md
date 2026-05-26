# Digital Wallet Backend

A full-stack digital wallet backend built using Spring Boot, Spring Data JPA, Hibernate, and Oracle Database.

This backend provides REST APIs for:
- User registration
- User login
- Wallet balance retrieval
- Money transfer
- Transaction history

---

# Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- Oracle Database
- Maven
- REST APIs

---

# Features Implemented

## Authentication
- User Registration
- User Login
- DTO-based request handling
- Global exception handling

---

## Wallet System
- Automatic wallet creation during registration
- Balance retrieval
- Money transfer between users
- Transaction history tracking

---

## Backend Architecture
- Layered architecture
    - Controller Layer
    - Service Layer
    - Repository Layer
- Dependency Injection
- REST API architecture
- DTO usage
- Validation handling
- Global exception handling
- Transaction management using `@Transactional`

---

# Database Tables

## users
Stores user information.

## wallets
Stores wallet balances linked to users.

## transactions
Stores transfer history.

---

# Current API Endpoints

## Register User

POST `/register`

```json
{
  "name": "Shubh",
  "email": "shubh@gmail.com",
  "password": "1234"
}
```

---

## Login User

POST `/login`

```json
{
  "email": "shubh@gmail.com",
  "password": "1234"
}
```

---

## Get Balance

GET `/balance?userId=1`

---

## Transfer Money

POST `/transfer`

```json
{
  "senderId": 1,
  "receiverId": 2,
  "amount": 500
}
```

---

## Transaction History

GET `/transactions?userId=1`

---

# Running the Backend

## Prerequisites
- Java 17+
- Maven
- Oracle Database

---

## Configure Database

Update:

`src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=wallet
spring.datasource.password=Admin
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
```

---

## Run Application

```bash
mvn spring-boot:run
```

Backend runs on:

```text
http://localhost:8080
```

---

# Project Structure

```text
src/main/java/com/wallet
│
├── advice
├── config
├── controller
├── dto
├── exception
├── model
├── repository
├── service
└── DigitalWalletApplication.java
```

---

# Current Limitations

- No JWT authentication
- No Spring Security
- Plain text password storage
- No refresh tokens
- No role-based authorization
- No deposit/withdraw functionality
- No pagination
- No API documentation
- No unit/integration tests

---

# Future Plans

## Security
- Spring Security
- JWT authentication
- BCrypt password encryption
- Protected routes

---

## Wallet Features
- Deposit money
- Withdraw money
- Transaction categorization
- Transaction status improvements

---

## Backend Improvements
- Proper entity relationships
- Response DTO standardization
- API versioning
- Swagger/OpenAPI documentation
- Logging system
- Audit system
- Redis caching

---

## DevOps
- Docker support
- CI/CD pipeline
- Cloud deployment

---

# Status

Backend is currently functional and integrated with the React frontend.
