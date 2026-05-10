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

1. Start MySQL and Redis.
   ```powershell
   docker compose up -d
   ```

2. Create the database schema in MySQL.

3. Update connection settings in the utility classes used by the app, especially `DBConnection`, `RedisUtil`, and `JwtUtil`.

4. Build the WAR.
   ```powershell
   mvn clean package
   ```

5. Deploy `target/demo7-1.0-SNAPSHOT.war` to Tomcat and start the server.

