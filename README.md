# Ecommerce API

A complete RESTful ecommerce backend built with **Spring Boot 4**, **Java 17**, **MySQL**, **Spring Security**, and **JWT authentication**. The API supports product browsing, category management, user authentication, shopping cart workflows, profile management, and checkout/order creation.

## Features

- User registration and login
- JWT-based authentication
- Role-based authorization with `ROLE_USER` and `ROLE_ADMIN`
- Product browsing, filtering, and admin product management
- Category browsing and admin category management
- Authenticated shopping cart management
- User profile retrieval and updates
- Checkout flow that creates orders from cart contents
- MySQL database scripts with sample ecommerce data
- OpenAPI documentation for exploring and testing endpoints

## Tech Stack

- Java 17
- Spring Boot 4
- Spring Web MVC
- Spring Security
- Spring Data JPA
- Spring Validation
- MySQL
- JWT / JJWT
- Springdoc OpenAPI
- Maven

## Project Structure

```text
├── database/                 # MySQL schema and seed scripts
├── src/main/java/org/yearup/
│   ├── controllers/          # REST controllers
│   ├── models/               # Domain models and DTOs
│   ├── repository/           # Data access layer
│   ├── security/             # Security and JWT configuration
│   ├── service/              # Business logic
│   └── ECommerceApplication.java
├── src/main/resources/       # Application resources and banners
├── openapi.yaml              # OpenAPI API specification
├── pom.xml                   # Maven configuration
```


## Authentication

The API uses JWT bearer tokens for protected endpoints.

### Register

```http
POST /register
Content-Type: application/json
```

```json
{
  "username": "johndoe",
  "password": "p@ssw0rd",
  "confirmPassword": "p@ssw0rd",
  "role": "ROLE_USER"
}
```

### Login

```http
POST /login
Content-Type: application/json
```

```json
{
  "username": "johndoe",
  "password": "p@ssw0rd"
}
```

Successful login returns a JWT token. Include it on protected requests:

```http
Authorization: Bearer your.jwt.token
```

## API Endpoints

### Authentication

| Method | Endpoint | Description | Auth |
| --- | --- | --- | --- |
| `POST` | `/register` | Create a new user account | Public |
| `POST` | `/login` | Authenticate and receive a JWT | Public |

### Products

| Method | Endpoint | Description | Auth |
| --- | --- | --- | --- |
| `GET` | `/products` | List or search products | Public |
| `GET` | `/products/{id}` | Get a product by ID | Public |
| `POST` | `/products` | Create a product | Admin |
| `PUT` | `/products/{id}` | Update a product | Admin |
| `DELETE` | `/products/{id}` | Delete a product | Admin |

Product search supports optional query parameters:

```text
/products?cat=1&minPrice=10&maxPrice=500&subCategory=Black
```

### Categories

| Method | Endpoint | Description | Auth |
| --- | --- | --- | --- |
| `GET` | `/categories` | List all categories | Public |
| `GET` | `/categories/{id}` | Get a category by ID | Public |
| `GET` | `/categories/{categoryId}/products` | List products in a category | Public |
| `POST` | `/categories` | Create a category | Admin |
| `PUT` | `/categories/{id}` | Update a category | Admin |
| `DELETE` | `/categories/{id}` | Delete a category | Admin |

### Shopping Cart

| Method | Endpoint | Description | Auth |
| --- | --- | --- | --- |
| `GET` | `/cart` | Get the authenticated user's cart | User |
| `POST` | `/cart/products/{productId}` | Add a product to the cart | User |
| `PUT` | `/cart/products/{productId}` | Update cart item quantity | User |
| `DELETE` | `/cart` | Clear the cart | User |

### Profile

| Method | Endpoint | Description | Auth |
| --- | --- | --- | --- |
| `GET` | `/profile` | Get the authenticated user's profile | User |
| `PUT` | `/profile` | Update the authenticated user's profile | User |

### Orders

| Method | Endpoint | Description | Auth |
| --- | --- | --- | --- |
| `POST` | `/orders` | Checkout and create an order from the cart | User |

## API Documentation

The repository includes an `openapi.yaml` specification. With the application running, Springdoc OpenAPI can be used to view and test the API through Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

## Roles and Permissions

| Role | Permissions |
| --- | --- |
| `ROLE_USER` | Manage personal profile, cart, and checkout |
| `ROLE_ADMIN` | Manage products and categories in addition to standard user access |