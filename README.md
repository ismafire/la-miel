# La Miel - Online Boutique

A Spring Boot e-commerce application with admin dashboard for managing products.

## Project Structure

```
src/
├── main/
│   ├── java/com/lamiel/store/
│   │   ├── AdminController.java
│   │   ├── StoreController.java
│   │   ├── RateLimitFilter.java
│   │   ├── RateLimiter.java
│   │   └── (other model classes)
│   └── resources/
│       ├── templates/
│       │   ├── index.html
│       │   └── admin-dashboard.html
│       ├── static/css/
│       │   └── style.css
│       └── application.properties
```

## Features

- Product catalog with category filtering
- Shopping cart functionality
- Admin dashboard for managing products
- Rate limiting on sensitive endpoints
- CSRF protection

## Building and Running

```bash
mvn clean package
java -jar target/la-miel-*.jar
```

The application will be available at `http://localhost:8080`

Admin dashboard: `http://localhost:8080/admin/login`

Default credentials (override with environment variables):
- Username: `admin`
- Password: `changeme`
