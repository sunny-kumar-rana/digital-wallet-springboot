# Digital Wallet Backend

A RESTful digital wallet backend built using Spring Boot and PostgreSQL.

The application provides user authentication, wallet management, money transfers, transaction history, and safeguards for concurrent and duplicate transfer requests.

---

# Tech Stack

- Java
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- REST APIs

---

# Features

## Authentication & Security

- User registration
- User login
- JWT-based authentication
- Spring Security
- BCrypt password hashing
- Stateless session management
- Protected authenticated endpoints
- DTO-based request handling
- Bean validation
- Global exception handling
- CORS configuration for the React frontend

---

## Wallet Management

- Automatic wallet creation during registration
- Balance retrieval
- Deposit money
- Withdraw money
- Sufficient-balance validation
- Transaction recording for wallet operations

---

## Money Transfers

- Transfer money between users
- Sender and receiver validation
- Atomic transfer processing
- Balance validation
- Transaction recording
- Idempotency support using `Idempotency-Key`
- Duplicate transfer prevention
- Pessimistic database locking
- Consistent wallet lock ordering to reduce deadlock risk

---

## Transaction Management

Wallet operations are executed using Spring's `@Transactional` support to maintain database consistency.

Transfer operations lock the involved wallet records before modifying balances.

When two wallets are involved, locks are acquired in ascending user-ID order. This provides a consistent lock acquisition order and helps reduce the possibility of deadlocks during concurrent transfers.

---

# Backend Architecture

The application follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

## Controller Layer

Handles:

- HTTP requests
- Request validation
- Authentication context
- API responses

## Service Layer

Contains business logic for:

- Balance validation
- Deposits
- Withdrawals
- Transfers
- Transaction creation
- Idempotency handling
- Concurrency control

## Repository Layer

Uses Spring Data JPA repositories for database access.

Pessimistic write locking is used when wallet balances need to be safely modified.

## DTO Layer

Request-specific DTOs are used to separate API input models from persistence entities.

## Exception Handling

Centralized exception handling provides consistent responses for validation and business-rule failures.

---

# Database

The application uses PostgreSQL.

## Main Tables

### `users`

Stores registered user information and authentication-related data.

### `wallets`

Stores wallet balances associated with users.

### `transactions`

Records wallet operations and money transfers.

### `idempotency_records`

Stores idempotency information used to prevent duplicate transfer processing.

---

# API Endpoints

| Method | Endpoint | Authentication | Description |
|---|---|---|---|
| POST | `/register` | Public | Register a new user |
| POST | `/login` | Public | Authenticate a user and obtain a JWT |
| GET | `/balance` | Required | Retrieve the authenticated user's wallet balance |
| POST | `/deposit` | Required | Deposit money into the authenticated user's wallet |
| POST | `/withdraw` | Required | Withdraw money from the authenticated user's wallet |
| POST | `/transfer` | Required | Transfer money to another user's wallet |
| GET | `/transactions` | Required | Retrieve the authenticated user's transaction history |

---

# Authentication

The application uses stateless JWT authentication.

The following endpoints are publicly accessible:

```text
POST /register
POST /login
```

All other application endpoints require authentication.

After a successful login, the API returns a JWT.

Authenticated requests must include the token in the `Authorization` header:

```http
Authorization: Bearer <your-jwt-token>
```

The authenticated user's ID is obtained from the security context rather than being supplied by the client for protected wallet operations.

---

# API Examples

## Register User

**POST** `/register`

```json
{
  "name": "Shubh",
  "email": "shubh@gmail.com",
  "password": "your-password"
}
```

### Validation

- Name cannot be empty
- Email cannot be empty
- Email must have a valid email format
- Password cannot be empty
- Password must contain at least 4 characters

---

## Login User

**POST** `/login`

```json
{
  "email": "shubh@gmail.com",
  "password": "your-password"
}
```

A successful login returns a JWT along with authenticated user information.

Example response structure:

```json
{
  "message": "Login Successful",
  "token": "<jwt-token>",
  "userId": 1,
  "name": "Shubh"
}
```

---

## Get Wallet Balance

**GET** `/balance`

Requires authentication.

The authenticated user's ID is obtained from the JWT/security context.

Example:

```http
Authorization: Bearer <your-jwt-token>
```

---

## Deposit Money

**POST** `/deposit`

Requires authentication.

```json
{
  "amount": 500.00
}
```

The amount must be at least `0.01`.

---

## Withdraw Money

**POST** `/withdraw`

Requires authentication.

```json
{
  "amount": 200.00
}
```

The withdrawal is rejected if the wallet does not have sufficient funds.

---

## Transfer Money

**POST** `/transfer`

Requires authentication.

```json
{
  "receiverId": 2,
  "amount": 500.00
}
```

The sender is determined from the authenticated security context.

A unique `Idempotency-Key` must also be supplied:

```http
Authorization: Bearer <your-jwt-token>
Idempotency-Key: 7f3a2d9c-1234-4567-89ab-123456789abc
```

The idempotency key prevents the same transfer request from being processed multiple times.

---

## Get Transaction History

**GET** `/transactions`

Requires authentication.

The authenticated user's ID is obtained from the security context.

Example:

```http
Authorization: Bearer <your-jwt-token>
```

---

# Transfer Consistency

Money transfers are processed as database transactions.

The transfer flow is:

```text
Validate request
      ↓
Validate Idempotency-Key
      ↓
Create idempotency record
      ↓
Lock sender & receiver wallets
      ↓
Validate sender balance
      ↓
Debit sender
      ↓
Credit receiver
      ↓
Create transaction record
      ↓
Commit transaction
```

Wallet records are acquired using pessimistic write locking before their balances are modified.

For transfers involving two wallets, the application acquires locks in ascending user-ID order.

This consistent lock ordering helps reduce the possibility of deadlocks when multiple transfers occur concurrently.

---

# Idempotency

The transfer API supports idempotency through the `Idempotency-Key` request header.

Example:

```http
Idempotency-Key: 7f3a2d9c-1234-4567-89ab-123456789abc
```

The backend records the key for the authenticated sender.

If the same sender submits the same idempotency key again, the transfer is not processed a second time.

This protects against duplicate transfer requests caused by retries or repeated client submissions.

---

# Validation Rules

The backend validates wallet operations before modifying balances.

Current validation rules include:

- Amount must be greater than zero
- Amount must be at least `0.01`
- Amount supports a maximum of two decimal places
- Receiver ID must be positive
- Sender and receiver cannot be the same user
- Sender must have sufficient balance
- Transfer requests require an idempotency key
- Idempotency keys cannot exceed 100 characters
- Required request fields are validated using Jakarta Bean Validation

---

# Configuration

Database credentials are supplied through environment variables rather than being committed directly to the repository.

The application uses the following configuration:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/digital_wallet}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
```

Set the required environment variables before running the application.

Example:

```text
DB_URL=jdbc:postgresql://localhost:5432/digital_wallet
DB_USERNAME=<your-db-username>
DB_PASSWORD=<your-db-password>
```

Do not commit actual database credentials to the repository.

---

# Running the Backend

## Prerequisites

- Java 17+
- Maven
- PostgreSQL

## Database

Create a PostgreSQL database named:

```text
digital_wallet
```

Configure the required database environment variables.

## Run the Application

Using Maven:

```bash
mvn spring-boot:run
```

Or build and run the application:

```bash
mvn clean package
java -jar target/<application-name>.jar
```

The backend runs on:

```text
http://localhost:8080
```

---

# CORS Configuration

The backend currently allows requests from the React development server:

```text
http://localhost:5173
```

Allowed HTTP methods include:

```text
GET
POST
PUT
DELETE
OPTIONS
```

---

# Project Structure

```text
src/main/java/com/wallet
│
├── advice
│   └── Global exception handling
│
├── config
│   └── Spring Security configuration
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
│   └── TransferRequestDto
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
│   └── CustomUserDetailsService
│
├── service
│   └── Business logic
│
└── DigitalWalletApplication.java
```

---

# Frontend

The backend is designed to work with the project's React frontend.

The frontend communicates with the Spring Boot REST API for:

- Authentication
- Wallet balance
- Deposits
- Withdrawals
- Money transfers
- Transaction history

---

# Current Implementation

The current backend implementation includes:

- User registration
- JWT authentication
- Spring Security
- BCrypt password hashing
- Stateless authentication
- PostgreSQL persistence
- Automatic wallet creation
- Balance retrieval
- Deposits
- Withdrawals
- User-to-user transfers
- Transaction history
- Transactional wallet operations
- Pessimistic locking
- Consistent lock ordering
- Idempotent transfers
- Duplicate transfer prevention
- Request validation
- Global exception handling
- React frontend CORS configuration

---

# Future Improvements

Potential future improvements include:

- Automated unit and integration tests
- API documentation with Swagger/OpenAPI
- Pagination for transaction history
- Refresh-token support
- Role-based authorization
- Standardized API response DTOs
- API versioning
- Structured logging
- Audit logging
- Docker support
- CI/CD pipeline
- Cloud deployment
- Redis caching

---

# Status

The backend currently provides the core digital wallet functionality and is integrated with the React frontend.

The implementation focuses on secure authentication, transactional wallet operations, transfer consistency, and protection against duplicate transfer requests.