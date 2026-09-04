# URL Shortener

A full-stack URL shortener web application that converts long URLs into short, shareable links and redirects users to the original URL when the short link is visited.

## Features
- Shorten any long URL into a short, unique link
- Redirect from short URL to the original long URL
- Simple and clean user interface
- REST API built with Spring Boot

## Tech Stack

**Frontend**
- React
- Vite

**Backend**
- Java
- Spring Boot
- Spring Data JPA
- MySQL

## Project Structure
```
shortner/
├── src/main/java/com/url/shortner
│   ├── controller/     # API endpoints
│   ├── service/        # Business logic
│   ├── repository/     # Database queries
│   ├── entity/         # Database models
│   ├── dto/             # Request/Response objects
│   └── exception/      # Custom exception handling
├── frontend/            # React application
└── pom.xml              # Maven configuration
```

## Getting Started

### Prerequisites
- Java 17+
- Node.js
- MySQL

### Backend Setup
```bash
cd shortner
./mvnw spring-boot:run
```
Backend will run on `http://localhost:8080`

### Frontend Setup
```bash
cd frontend
npm install
npm run dev
```
Frontend will run on `http://localhost:5173`

## How It Works
1. User pastes a long URL in the frontend
2. Backend generates a unique short code and saves it to the database
3. A short URL is returned and displayed to the user
4. When someone visits the short URL, the backend looks it up and redirects to the original URL

## Author
**Harsh Dubey**
