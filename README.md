# SnapLink URL shortener

Spring Boot API with a React (Vite) UI. Short links redirect on the backend and every visit is counted.

## Prerequisites

- Java 17+
- Node.js 18+
- MySQL 8 with a database named `url_shortener`

## Configuration

Do **not** put database passwords in git. Set them in the environment:

```bash
MYSQL_URL=jdbc:mysql://localhost:3306/url_shortener
MYSQL_USER=root
MYSQL_PASSWORD=your_password
```

Optional:

```bash
APP_PUBLIC_BASE_URL=http://localhost:8081
```

The API listens on port **8081** so it matches the Vite proxy.

If a password was previously committed in `application.properties`, change that MySQL password. It should be treated as leaked.

## Run

Terminal 1 — API:

```bash
./mvnw spring-boot:run
```

On Windows you can use `mvnw.cmd spring-boot:run`.

Terminal 2 — UI:

```bash
cd frontend
npm install
npm run dev
```

Open http://localhost:5173. Short links are `http://localhost:8081/{code}` and also work through the Vite proxy.

## Tests

Tests use an in-memory H2 database (no MySQL required):

```bash
./mvnw test
```
