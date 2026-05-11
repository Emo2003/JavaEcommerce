
# 🛒 E-Commerce Web Application

A production-ready Java enterprise e-commerce platform built with **JSP/Servlet** technology, featuring **MySQL** persistence, **Redis** caching and rate limiting, **JWT-based authentication**, and a clean **layered architecture**. Designed for scalability, security, and maintainability.

---

## 🚀 Key Features

### 👤 User Management
- User registration, login, logout, and account deletion
- JWT-based stateless authentication with secure HTTP-only cookies
- Role-based access control (Admin / Customer)

### 🛍️ Product & Shopping
- Browse products with pagination and search
- Detailed product view with specifications
- Shopping cart: add, update quantity, remove items, and checkout

### ⭐ Reviews System
- Authenticated users can submit product reviews
- Delete own reviews
- View aggregated ratings per product

### 🛡️ Security & Middleware
- `AuthFilter`: Protects authenticated and admin-only routes
- `RateLimitFilter`: Redis-backed request throttling (e.g., 100 req/min)
- `ErrorHandlingFilter`: Centralized exception handling with user-friendly error pages

### 👑 Admin Capabilities
- Product management (create, edit, delete)
- Order management and status updates
- Admin-only dashboard access

---

## 🏗️ Architecture Overview

The application follows **MVC** pattern with strict layer separation:

```
┌─────────────────────────────────────────────────────┐
│                   JSP Views (UI)                    │
└─────────────────────────────────────────────────────┘
                           │
┌─────────────────────────────────────────────────────┐
│   Controller Layer (Servlets)                       │
│   - Request routing, input validation               │
└─────────────────────────────────────────────────────┘
                           │
┌─────────────────────────────────────────────────────┐
│   Service Layer (Business Logic)                    │
│   - Transactions, orchestration, business rules     │
└─────────────────────────────────────────────────────┘
                           │
┌─────────────────────────────────────────────────────┐
│   DAO Layer (Data Access)                           │
│   - CRUD operations, SQL queries                    │
└─────────────────────────────────────────────────────┘
                           │
┌─────────────────────────────────────────────────────┐
│   Infrastructure (Filters + Utilities)              │
│   - Auth, rate limiting, JWT, DB pool, Redis client │
└─────────────────────────────────────────────────────┘
```

### 🔧 Technology Stack

| Layer          | Technology                                      |
|----------------|-------------------------------------------------|
| Frontend       | JSP, JSTL, HTML5, CSS3                          |
| Backend        | Java 17+, Jakarta Servlet 6.0                   |
| Authentication | JWT (JSON Web Tokens) + Session Management      |
| Database       | MySQL 8 (with JDBC connection pool)             |
| Caching/Rate   | Redis (Jedis client)                            |
| Build Tool     | Maven                                           |
| Container      | Apache Tomcat 10.1+ or Docker                   |
| Testing        | JUnit 5, Mockito                                |

---

## 📂 Project Structure

```
src/main/java/com/example/java_ecommerce/
├── controller/          # Servlets (request handlers)
│   ├── AuthServlet.java
│   ├── ProductServlet.java
│   ├── CartServlet.java
│   └── AdminServlet.java
|    └── OrderServlet.java
├── services/            # Business logic layer
├── dao/                 # Data Access Objects
├── filters/             # Request filters (auth, rate limit, error)
├── models/              # POJOs (User, Product, Order, Review)
└── util/                # Helpers (JWT, Redis, DBConnection, Password)

src/main/webapp/
├── WEB-INF/             # Protected JSPs and web.xml
├── login.jsp
├── signup.jsp
├── products.jsp
├── cart.jsp
├── error.jsp

---

## 🐳 Quick Start with Docker

### Prerequisites
- Docker Desktop
- (Optional) Maven for local WAR builds

### 1️⃣ Start the full stack
```bash
docker compose up -d
```

This launches:
- **MySQL 8** on port `3307` (to avoid host conflicts)
- **Redis** on port `6379`
- **Tomcat 10** with the pre-built WAR

### 4️⃣ Stop the environment
```bash
docker compose down
# Add -v to remove volumes (resets database)
```

---

## 🛠️ Manual Setup (without Docker)

### Requirements
- Java 17+ | Maven 3.8+ | Tomcat 10.1+ | MySQL 8 | Redis 7+

### Steps

1. **Clone & Build**
   ```bash
   mvn clean package
   ```

2. **Setup MySQL Database**
   ```sql
   CREATE DATABASE ecommerce_db;
   -- Run schema from docker/mysql/init/01-schema.sql
   ```

3. **Configure Connection** (update `context.xml` or environment variables)
   ```properties
   DB_HOST=localhost
   DB_PORT=3306
   DB_NAME=ecommerce_db
   DB_USER=root
   DB_PASSWORD=yourpassword
   REDIS_HOST=localhost
   REDIS_PORT=6379
   ```

4. **Deploy to Tomcat**
   ```bash
   cp target/demo7-1.0-SNAPSHOT.war $TOMCAT_HOME/webapps/
   ```

5. **Start Tomcat & Redis**
   ```bash
   $TOMCAT_HOME/bin/startup.sh
   redis-server
   ```

6. **Visit** `http://localhost:8080/demo7/`

---

## 🔐 Environment Configuration

| Variable      | Inside Docker | Outside Docker |
|---------------|---------------|----------------|
| `DB_HOST`     | `mysql`       | `localhost`    |
| `DB_PORT`     | `3306`        | `3306` (or `3307` if mapped) |
| `REDIS_HOST`  | `redis`       | `localhost`    |
| `REDIS_PORT`  | `6379`        | `6379`         |

> 💡 Docker Compose automatically injects the correct service names. For local development, override them in your IDE or Tomcat context.

---

## 📦 Database Schema (auto-created)

The initial schema (`01-schema.sql`) creates:
- `users` (id, email, password_hash, role)
- `products` (id, name, description, price)
- `carts` & `cart_items`
- `orders` & `order_items`
- `reviews` (user_id, product_id, rating, comment)

## 🌐 API Endpoints

### Auth
POST   /login
POST   /register
POST   /logout

### Products
GET    /products
GET    /product?id=1
POST   /admin/product/add
POST   /admin/product/delete

### Cart
POST   /cart/add
POST   /cart/update
POST   /cart/remove
GET    /cart

### Orders
POST   /order/checkout
GET    /orders


## 🎯 User Flow

1. User registers or logs in
2. Browses products
3. Adds items to cart
4. Proceeds to checkout
5. Order is stored in database
6. Admin can manage products and orders

## 🔐 Security Highlights

- JWT authentication stored in HTTP-only cookies
- Password hashing (BCrypt or similar)
- Role-based authorization (Admin / User)
- Rate limiting using Redis
- Protected routes via AuthFilter
