# Digital Wallet - Spring Boot

A secure and scalable Digital Wallet application built using Spring Boot. The project provides wallet management, user authentication, money transfer, transaction handling, and RESTful APIs for digital payment operations.

## Features

- User Registration & Login
- Wallet Creation and Management
- Balance Inquiry
- Add Money to Wallet
- Transfer Money Between Users
- Transaction History
- Secure REST APIs
- Exception Handling
- Validation Support
- Layered Architecture
- Database Integration with JPA/Hibernate

---

## Tech Stack

### Backend
- Java
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate

### Database
- MySQL

### Build Tool
- Maven

### API Testing
- Postman

---

## Project Structure

```bash
digital-wallet-springboot/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── wallet/
│   │   │           ├── controller/
│   │   │           ├── service/
│   │   │           ├── repository/
│   │   │           ├── model/
│   │   │           ├── dto/
│   │   │           ├── exception/
│   │   │           └── config/
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql
│   │
│   └── test/
│
├── pom.xml
└── README.md
```

---

## Getting Started

### Prerequisites

Make sure the following are installed:

- Java 17+ (or project compatible version)
- Maven
- MySQL

---

## Installation

### 1. Clone Repository

```bash
git clone https://github.com/sunny-kumar-rana/digital-wallet-springboot.git
```

### 2. Navigate to Project

```bash
cd digital-wallet-springboot
```

### 3. Configure Database

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/digital_wallet
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 4. Build Project

```bash
mvn clean install
```

### 5. Run Application

```bash
mvn spring-boot:run
```

Application starts on:

```bash
http://localhost:8080
```

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users/register` | Register User |
| POST | `/api/users/login` | User Login |
| GET | `/api/wallet/{id}` | Get Wallet Details |
| POST | `/api/wallet/add-money` | Add Money |
| POST | `/api/wallet/transfer` | Transfer Money |
| GET | `/api/transactions/{userId}` | Transaction History |

---

## Sample JSON Request

### Register User

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

### Transfer Money

```json
{
  "senderId": 1,
  "receiverId": 2,
  "amount": 500
}
```

---

## Security

- Password encryption
- Input validation
- Exception handling
- Layered service architecture
- Secure API design practices

---

## Future Improvements

- JWT Authentication
- Role-Based Access Control
- Docker Support
- Swagger/OpenAPI Documentation
- Unit & Integration Testing
- Email Notifications
- Transaction Limits
- Redis Caching
- Microservices Architecture

---

## Testing

Run tests using:

```bash
mvn test
```

---

## Deployment

Generate JAR:

```bash
mvn clean package
```

Run JAR:

```bash
java -jar target/digital-wallet-springboot.jar
```

---

## Contributing

1. Fork repository
2. Create feature branch
3. Commit changes
4. Push branch
5. Open Pull Request

---

## Author

Sunny Kumar Rana

GitHub:
https://github.com/sunny-kumar-rana
