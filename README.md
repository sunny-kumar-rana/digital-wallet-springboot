# Digital Wallet

A production-oriented RESTful digital wallet application built with Spring Boot, PostgreSQL, Spring Security, JWT authentication, and a React frontend.

The application supports user registration, authentication, wallet management, deposits, withdrawals, user-to-user transfers, transaction history, and safeguards against duplicate and concurrent transfer processing.

## Live Demo

- **Frontend:** https://digitalwallet-su.vercel.app
- **Backend API:** https://digital-wallet-api-6465.onrender.com
- **Health Check:** https://digital-wallet-api-6465.onrender.com/actuator/health
- **Swagger UI:** https://digital-wallet-api-6465.onrender.com/swagger-ui.html

> The backend is deployed as a Docker-based Spring Boot service on Render.
> The React frontend is deployed on Vercel.
> The production database is PostgreSQL hosted on Render.

---

# Features

## Authentication & Security

- User registration
- User login
- JWT-based authentication
- Spring Security
- BCrypt password hashing
- Stateless authentication
- Protected REST endpoints
- Authenticated user resolution from the security context
- JWT validation and expiration handling
- Explicit `401 Unauthorized` and `403 Forbidden` responses
- Bean Validation
- Global exception handling
- Configurable CORS
- Externalized secrets and database credentials

## Wallet Management

- Automatic wallet creation during registration
- Balance retrieval
- Deposit money
- Withdraw money
- Positive amount validation
- Sufficient-balance validation
- Transaction recording
- Transactional wallet operations

## Money Transfers

- Transfer money between users
- Sender and receiver validation
- Atomic transfer processing
- Sufficient-balance validation
- Transaction recording
- Idempotency using `Idempotency-Key`
- Duplicate transfer prevention
- PostgreSQL-backed idempotency records
- Pessimistic wallet locking
- Consistent wallet lock ordering
- Reduced deadlock risk during concurrent transfers

## Transaction History

- Retrieve authenticated user's transactions
- Transaction type tracking
- Transaction status tracking
- Transaction timestamps
- Transfer transaction records

## API & Operations

- OpenAPI/Swagger documentation
- Actuator health endpoint
- Structured API error responses
- Request validation
- Flyway database migrations
- Hibernate schema validation
- Docker deployment configuration
- Production environment configuration

---

# Tech Stack

## Backend

- Java 17
- Spring Boot 3.5
- Spring Security
- JWT
- JJWT
- Spring Data JPA
- Hibernate
- PostgreSQL 18
- Flyway
- Jakarta Bean Validation
- Springdoc OpenAPI
- Spring Boot Actuator
- Maven

## Frontend

- React 19
- Vite
- React Router
- Axios
- Tailwind CSS
- React Hook Form
- React Hot Toast
- Recharts

## Testing

- JUnit
- Spring Boot Test
- Testcontainers
- PostgreSQL Testcontainer

## Deployment

- Docker
- Render
- Vercel
- PostgreSQL on Render

---

# Architecture

The application follows a layered backend architecture.

```text
                         ┌──────────────────────┐
                         │     React Frontend   │
                         │     React + Vite      │
                         └──────────┬───────────┘
                                    │
                              HTTPS / REST
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │    Spring Boot API   │
                         │                      │
                         │  Spring Security     │
                         │  JWT Authentication  │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │    Controller Layer  │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │     Service Layer    │
                         │                      │
                         │ Business Logic       │
                         │ Transactions         │
                         │ Idempotency          │
                         │ Concurrency Control  │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │   Repository Layer   │
                         │   Spring Data JPA    │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │    PostgreSQL DB     │
                         │                      │
                         │ users                │
                         │ wallets              │
                         │ transactions         │
                         │ idempotency_records  │
                         └──────────────────────┘
```

---

# Backend Architecture

```text
src/main/java/com/wallet

├── advice
│   └── Global exception handling
│
├── config
│   ├── SecurityConfig
│   ├── DatabaseConfig
│   └── OpenApiConfig
│
├── controller
│   ├── AuthController
│   ├── RegistrationController
│   ├── WalletController
│   ├── TransferController
│   └── TransactionController
│
├── dto
│   ├── LoginRequestDto
│   ├── RegisterRequestDto
│   ├── MoneyRequestDto
│   ├── TransferRequestDto
│   ├── BalanceResponseDto
│   ├── TransactionResponseDto
│   ├── TransferResponseDto
│   ├── OperationResponseDto
│   └── ApiErrorResponseDto
│
├── exception
│   └── Application-specific exceptions
│
├── model
│   └── JPA entities
│
├── repository
│   └── Spring Data JPA repositories
│
├── security
│   ├── JwtService
│   ├── JwtAuthenticationFilter
│   ├── CustomUserDetailsService
│   └── CustomUserPrincipal
│
├── service
│   └── Business logic
│
└── DigitalWalletApplication.java
```

---

# Database Design

The application uses PostgreSQL for persistent wallet and transaction data.

## Tables

### `users`

Stores registered users and authentication information.

Main responsibilities:

- User identity
- Name
- Email
- BCrypt password hash

### `wallets`

Stores wallet balances associated with users.

Main responsibilities:

- Wallet ownership
- Current balance
- Balance constraints

### `transactions`

Stores wallet operations and transfer records.

Main responsibilities:

- Transaction type
- Amount
- Status
- Sender/receiver relationships
- Transaction timestamps

### `idempotency_records`

Stores idempotency information for transfer requests.

Main responsibilities:

- Identify previously processed transfer requests
- Prevent duplicate transfer execution
- Enforce uniqueness for a user/idempotency-key combination

---

# Database Migrations

Database schema management is handled by Flyway.

Migration files are located at:

```text
src/main/resources/db/migration/
```

Current migration:

```text
V1__initial_schema.sql
```

Hibernate is configured for schema validation rather than automatic schema creation:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

This allows Flyway to remain responsible for database schema changes while Hibernate validates the resulting schema.

---

# Transaction & Concurrency Handling

Financial operations must maintain database consistency.

Wallet modifications are executed inside transactional service methods.

## Pessimistic Locking

Wallet records are locked before modifying balances.

This prevents concurrent operations from reading and modifying the same balance simultaneously.

## Consistent Lock Ordering

For transfers involving two wallets, wallet locks are acquired in ascending user-ID order.

```text
User 2 → User 7

Lock User 2 wallet
       ↓
Lock User 7 wallet
       ↓
Validate balance
       ↓
Debit sender
       ↓
Credit receiver
       ↓
Create transactions
       ↓
Commit
```

Using a consistent ordering reduces the possibility of deadlocks when multiple transfers execute concurrently.

---

# Idempotency

Transfers support an `Idempotency-Key` request header.

Example:

```http
POST /transfer
Authorization: Bearer <jwt-token>
Idempotency-Key: 4c8d1b7e-...
Content-Type: application/json
```

Request:

```json
{
  "receiverId": 2,
  "amount": 100.00
}
```

The idempotency record is stored in PostgreSQL.

The database enforces uniqueness for the user/idempotency-key combination.

Conceptually:

```text
Transfer Request
      │
      ▼
Check / create idempotency record
      │
      ├── Already exists ──► Return previous result
      │
      └── New request
             │
             ▼
       Lock wallets
             │
             ▼
       Validate balance
             │
             ▼
       Execute transfer
             │
             ▼
       Record transaction
```

This protects against accidental duplicate transfer processing caused by repeated client requests.

---

# Authentication Flow

The application uses stateless JWT authentication.

```text
Client
  │
  │ POST /login
  ▼
AuthController
  │
  ▼
UserService
  │
  ├── Find user
  ├── Verify BCrypt password
  │
  ▼
Generate JWT
  │
  ▼
Client receives token
```

Subsequent protected requests include:

```http
Authorization: Bearer <jwt-token>
```

The JWT authentication filter:

1. Extracts the token.
2. Validates the JWT signature.
3. Extracts the username/user ID.
4. Loads the authenticated user.
5. Creates the Spring Security authentication.
6. Places the authentication in the security context.

Protected wallet operations obtain the authenticated user's identity from the security context instead of trusting a client-supplied sender ID.

---

# API Endpoints

| Method | Endpoint | Authentication | Description |
|---|---|---|---|
| POST | `/register` | Public | Register a new user |
| POST | `/login` | Public | Authenticate and obtain JWT |
| GET | `/balance` | Required | Retrieve authenticated user's balance |
| POST | `/deposit` | Required | Deposit money |
| POST | `/withdraw` | Required | Withdraw money |
| POST | `/transfer` | Required | Transfer money to another user |
| GET | `/transactions` | Required | Retrieve authenticated user's transaction history |

## Register

```http
POST /register
Content-Type: application/json
```

```json
{
  "name": "John",
  "email": "john@example.com",
  "password": "your-password"
}
```

## Login

```http
POST /login
Content-Type: application/json
```

```json
{
  "email": "john@example.com",
  "password": "your-password"
}
```

## Balance

```http
GET /balance
Authorization: Bearer <jwt-token>
```

## Deposit

```http
POST /deposit
Authorization: Bearer <jwt-token>
Content-Type: application/json
```

```json
{
  "amount": 500.00
}
```

## Withdraw

```http
POST /withdraw
Authorization: Bearer <jwt-token>
Content-Type: application/json
```

```json
{
  "amount": 100.00
}
```

## Transfer

```http
POST /transfer
Authorization: Bearer <jwt-token>
Idempotency-Key: unique-request-key
Content-Type: application/json
```

```json
{
  "receiverId": 2,
  "amount": 100.00
}
```

## Transactions

```http
GET /transactions
Authorization: Bearer <jwt-token>
```

---

# Validation Rules

Wallet operations validate input before modifying balances.

Current validation includes:

- Amount must be greater than zero
- Amount must be at least `0.01`
- Amount supports a maximum of two decimal places
- Receiver ID must be positive
- Sender and receiver cannot be the same user
- Sender must have sufficient balance
- Transfer requests require an idempotency key
- Idempotency keys cannot exceed 100 characters
- Required request fields use Jakarta Bean Validation

---

# Error Handling

The application uses centralized exception handling.

API errors are returned using a structured response format.

Example:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Insufficient wallet balance",
  "timestamp": "2026-01-01T12:00:00"
}
```

Authentication failures are handled explicitly:

```text
401 Unauthorized
403 Forbidden
```

---

# API Documentation

OpenAPI documentation is provided using Springdoc.

Swagger UI:

https://digital-wallet-api-6465.onrender.com/swagger-ui.html

OpenAPI documentation allows the API endpoints, request models, responses, and JWT bearer authentication scheme to be explored interactively.

---

# Health & Operations

Spring Boot Actuator exposes the application health endpoint:

```text
GET /actuator/health
```

Production endpoint:

https://digital-wallet-api-6465.onrender.com/actuator/health

Example response:

```json
{
  "status": "UP",
  "groups": [
    "liveness",
    "readiness"
  ]
}
```

Only the health endpoint is exposed through Actuator.

---

# Configuration

Sensitive configuration is supplied through environment variables.

The application uses:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/digital_wallet}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

jwt.secret=${JWT_SECRET}
jwt.expiration=86400000

app.cors.allowed-origin=${CORS_ALLOWED_ORIGIN:http://localhost:5173}

server.port=${PORT:8080}
```

Required environment variables:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
CORS_ALLOWED_ORIGIN
```

Never commit real credentials, JWT secrets, or production database connection details to the repository.

---

# Running Locally

## Prerequisites

- Java 17+
- Maven
- PostgreSQL
- Node.js and npm for the frontend

## Backend Database

Create a PostgreSQL database:

```text
digital_wallet
```

Configure:

```text
DB_URL=jdbc:postgresql://localhost:5432/digital_wallet
DB_USERNAME=<your-db-username>
DB_PASSWORD=<your-db-password>
JWT_SECRET=<your-jwt-secret>
CORS_ALLOWED_ORIGIN=http://localhost:5173
```

## Run Backend

```bash
mvn spring-boot:run
```

Backend:

```text
http://localhost:8080
```

## Build Backend

```bash
mvn clean package
```

Run the generated JAR:

```bash
java -jar target/<application-name>.jar
```

---

# Docker

The backend includes a multi-stage Dockerfile.

Build:

```bash
docker build -t digital-wallet .
```

Run:

```bash
docker run -p 8080:8080 \
  -e DB_URL=<database-url> \
  -e DB_USERNAME=<database-username> \
  -e DB_PASSWORD=<database-password> \
  -e JWT_SECRET=<jwt-secret> \
  -e CORS_ALLOWED_ORIGIN=<frontend-origin> \
  digital-wallet
```

The Docker image:

1. Builds the application using Maven and Java 17.
2. Creates a runtime image using Eclipse Temurin 17 JRE.
3. Runs the packaged Spring Boot JAR.

---

# Testing

The backend contains unit/integration test coverage using Spring Boot testing infrastructure and Testcontainers.

Testcontainers provides an isolated PostgreSQL container for integration tests.

Run the complete test suite:

```bash
mvn test
```

The current test suite completes successfully with:

```text
Tests run: 18
Failures: 0
Errors: 0
BUILD SUCCESS
```

---

# Deployment

## Production Architecture

```text
React Frontend
      │
      ▼
    Vercel
      │
      │ HTTPS
      ▼
Spring Boot API
      │
      ▼
   Render
      │
      ▼
PostgreSQL
   Render
```

## Frontend

The React application is deployed on Vercel.

Production URL:

https://digitalwallet-su.vercel.app

## Backend

The Spring Boot application is packaged using Docker and deployed as a Render Web Service.

Production URL:

https://digital-wallet-api-6465.onrender.com

## Database

The production database is PostgreSQL hosted by Render.

Flyway applies the database migration automatically when the application starts.

## CORS

The production backend receives the frontend origin through:

```text
CORS_ALLOWED_ORIGIN
```

Local development:

```text
http://localhost:5173
```

Production:

```text
https://digitalwallet-su.vercel.app
```

---

# Security Considerations

The project implements several security and consistency measures:

- Passwords are hashed using BCrypt
- JWT secrets are externalized
- Database credentials are externalized
- Protected endpoints require authentication
- JWT tokens are validated by a security filter
- Authenticated identity is derived from the security context
- CORS is configurable
- Database schema is managed through Flyway
- Wallet updates execute transactionally
- Pessimistic locking protects concurrent wallet modifications
- Consistent lock ordering reduces deadlock risk
- Idempotency prevents duplicate transfer processing
- Database constraints enforce important invariants

---

# Project Goals

This project was built to demonstrate practical backend development concepts beyond basic CRUD operations.

The implementation focuses particularly on:

- REST API design
- Authentication and authorization
- Database transactions
- Concurrency control
- Idempotent financial operations
- Database schema management
- Integration testing
- Containerization
- Cloud deployment
- React-to-Spring Boot integration

---

# Future Improvements

Potential future improvements include:

- Refresh-token authentication
- Role-based authorization
- API versioning
- Pagination for transaction history
- Structured logging
- Audit logging
- CI/CD pipeline
- Redis caching
- Rate limiting
- Enhanced observability
- Automated deployment checks

---

# Status

**Deployed and operational.**

The project currently includes:

- Spring Boot REST API
- React frontend
- JWT authentication
- BCrypt password hashing
- PostgreSQL persistence
- Flyway migrations
- Transactional wallet operations
- Pessimistic locking
- Consistent lock ordering
- Idempotent transfers
- Duplicate transfer prevention
- Bean validation
- Global exception handling
- OpenAPI/Swagger documentation
- Actuator health monitoring
- Unit/integration testing
- Testcontainers
- Docker deployment
- Render backend deployment
- Vercel frontend deployment

---

# Author

**Sunny**

GitHub:

https://github.com/sunny-kumar-rana