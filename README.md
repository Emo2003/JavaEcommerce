# E-Commerce Web Application

A Java JSP/Servlet e-commerce project with MySQL persistence, Redis support, JWT-based authentication, rate limiting, and a clean layered structure.

## Overview

This application lets users register, sign in, browse products, view product details, manage a cart, and submit reviews. Admin users can add, edit, and delete products, and manage orders.

The app is organized into controller, service, DAO, filter, model, and utility layers to keep request handling, business logic, and database access separate.

## Features

- User registration, login, logout, and account deletion
- JWT-based authentication with server-side session support
- Role-based admin access
- Product listing and product details pages
- Cart actions: add, update, remove, clear, and checkout
- Product reviews and review deletion
- Admin product and order management
- Redis-backed rate limiting
- Centralized error handling and validation


## Project Structure

```text
src/main/java/com/example/java_ecommerce/
├── controller  # Servlets for request handling
├── services    # Business logic
├── dao         # Database access
├── filters     # Auth, rate limit, and error handling
├── models      # Domain objects
└── util        # JWT, Redis, DB, and password helpers

src/main/webapp/
├── login.jsp
├── signup.jsp
├── error.jsp
└── (other JSPs as needed)
```

## Architecture

- `AuthFilter` protects authenticated and admin-only routes.
- `RateLimitFilter` limits repeated requests to reduce abuse.
- `ErrorHandlingFilter` keeps failures consistent and user-friendly.
- `JwtUtil`, `RedisUtil`, and `DBConnection` handle shared security and infrastructure concerns.
- `ProductsMainServlet` provides the authenticated landing page after login.

## Setup

### Prerequisites

- Java and Maven
- Tomcat 10.1+ (or any Jakarta Servlet 6.0-compatible container)
- MySQL 8
- Redis

### Local run

1. Start the full stack with Docker.
   ```powershell
   docker compose up -d
   ```

2. The app container connects to MySQL using `DB_HOST=mysql` and `DB_PORT=3306`.

3. If you run the app outside Docker, use the exposed host port instead:
   - `DB_HOST=localhost`
   - `DB_PORT=3307`

4. Redis follows the same rule:
   - inside Docker: `REDIS_HOST=redis`, `REDIS_PORT=6379`
   - outside Docker: `REDIS_HOST=localhost`, `REDIS_PORT=6379`

5. The database schema is created automatically from `docker/mysql/init/01-schema.sql` on first start.

6. Build the WAR only if you want to run the app outside Docker.
   ```powershell
   mvn clean package
   ```

7. Deploy `target/demo7-1.0-SNAPSHOT.war` to Tomcat if you are not using the Docker app container.

### Docker notes

- MySQL is exposed on host port `3307` to avoid conflicts with local MySQL installs.
- Redis is exposed on host port `6379`.
- The MySQL container uses a persistent volume, so data survives restarts.
- The schema bootstrap creates starter `admin` credentials with password `admin123` for local testing.
- The app container talks to the same MySQL database through the service name `mysql`; your host can still reach that same database through `localhost:3307`.

