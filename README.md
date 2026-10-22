# Spring Boot Product CRUD with Google Analytics 4 Integration

[![Java CI with Maven](https://github.com/hendisantika/spring-boot-google-analytics/actions/workflows/maven.yml/badge.svg)](https://github.com/hendisantika/spring-boot-google-analytics/actions/workflows/maven.yml)
[![Dependabot](https://img.shields.io/badge/dependabot-enabled-025E8C?logo=dependabot)](.github/dependabot.yml)
![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F?logo=springboot)

A comprehensive Spring Boot application that provides CRUD operations for products with PostgreSQL database support and
Google Analytics 4 event tracking.

## Features

- **Complete CRUD Operations**: Create, Read, Update, Delete products
- **Database Support**: PostgreSQL 17.5 integration with JPA/Hibernate
- **Docker Support**: Containerized application with Docker Compose
- **Google Analytics 4**: Event tracking for all product operations
- **Advanced Search**: Search products by name, description, price range
- **Pagination**: Support for paginated product listings
- **Validation**: Input validation with Bean Validation
- **Logging**: Comprehensive logging throughout the application
- **CI/CD**: GitHub Actions build & test on JDK 25 with a PostgreSQL service container
- **Automated Dependency Updates**: Grouped Dependabot PRs for Maven, GitHub Actions and Docker

## Tech Stack

- Java 25 (Eclipse Temurin)
- Spring Boot 4.1.1
- Spring Data JPA
- PostgreSQL 17.5
- Docker & Docker Compose
- Google Analytics 4 API
- Lombok
- Maven
- GitHub Actions & Dependabot

## Project Structure

```
src/
├── main/
│   ├── java/id/my/hendisantika/googleanalytics/
│   │   ├── config/
│   │   │   └── GoogleAnalyticsConfig.java
│   │   ├── controller/
│   │   │   └── ProductController.java
│   │   ├── entity/
│   │   │   └── Product.java
│   │   ├── repository/
│   │   │   └── ProductRepository.java
│   │   ├── service/
│   │   │   ├── ProductService.java
│   │   │   └── GoogleAnalyticsService.java
│   │   └── SpringBootGoogleAnalyticsApplication.java
│   └── resources/
│       └── application.properties
├── .github/
│   ├── dependabot.yml
│   └── workflows/
│       └── maven.yml
├── docker-compose.yml
├── Dockerfile
└── pom.xml
```

## Prerequisites

- Java 25 (JDK 25)
- Maven 3.9+ (or use the bundled `./mvnw`)
- Docker and Docker Compose
- Google Analytics 4 property (optional)

## Quick Start

### 1. Clone and Build

```bash
git clone https://github.com/hendisantika/spring-boot-google-analytics.git
cd spring-boot-google-analytics
./mvnw clean package -DskipTests
```

### 2. Run with Docker Compose

```bash
docker-compose up -d
```

This starts PostgreSQL 17.5 on port 5432. The `app` service in `docker-compose.yml` is commented out by default;
uncomment it to also run the application container (built from `eclipse-temurin:25-jre`) on port 8080.

> **Note:** `application.properties` defaults to `jdbc:postgresql://localhost:5433/productdb`. When using the Compose
> database on port 5432, set `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/productdb`.

### 3. Access the Application

The API will be available at: `http://localhost:8080/api/products`

## API Endpoints

### Product CRUD Operations

| Method | Endpoint                  | Description                  |
|--------|---------------------------|------------------------------|
| GET    | `/api/products`           | Get all products             |
| GET    | `/api/products/paginated` | Get products with pagination |
| GET    | `/api/products/{id}`      | Get product by ID            |
| POST   | `/api/products`           | Create new product           |
| PUT    | `/api/products/{id}`      | Update existing product      |
| DELETE | `/api/products/{id}`      | Delete product               |

### Search and Filter

| Method | Endpoint                                                  | Description                     |
|--------|-----------------------------------------------------------|---------------------------------|
| GET    | `/api/products/search?name={name}`                        | Search products by name         |
| GET    | `/api/products/search/advanced?keyword={keyword}`         | Advanced search with pagination |
| GET    | `/api/products/price-range?minPrice={min}&maxPrice={max}` | Filter by price range           |
| GET    | `/api/products/low-stock?threshold={threshold}`           | Get low stock products          |
| GET    | `/api/products/low-stock/count?threshold={threshold}`     | Count low stock products        |

### Example Product JSON

```json
{
  "name": "Sample Product",
  "description": "A sample product description",
  "price": 29.99,
  "stockQuantity": 100
}
```

## Configuration

### Database Configuration

The application uses PostgreSQL with the following default settings:

- Database: `productdb`
- Username: `yu71`
- Password: `53cret`
- Port: `5433`

### Google Analytics 4 Configuration

Set the following environment variables or application properties:

```properties
google.analytics.property-id=GA_MEASUREMENT_ID
google.analytics.credentials-path=/path/to/credentials.json
```

### Environment Variables

| Variable                     | Description                  | Default                                    |
|------------------------------|------------------------------|--------------------------------------------|
| `GA_PROPERTY_ID`             | Google Analytics Property ID | -                                          |
| `GA_CREDENTIALS_PATH`        | Path to GA4 credentials file | -                                          |
| `SPRING_DATASOURCE_URL`      | Database URL                 | jdbc:postgresql://localhost:5433/productdb |
| `SPRING_DATASOURCE_USERNAME` | Database username            | yu71                                       |
| `SPRING_DATASOURCE_PASSWORD` | Database password            | 53cret                                     |

## Google Analytics 4 Events

The application tracks the following events:

| Event Name          | Trigger                     | Parameters                 |
|---------------------|-----------------------------|----------------------------|
| `product_view`      | When a product is viewed    | product_id, product_name   |
| `product_create`    | When a product is created   | product_id, product_name   |
| `product_update`    | When a product is updated   | product_id, product_name   |
| `product_delete`    | When a product is deleted   | product_id, product_name   |
| `product_search`    | When products are searched  | search_term, results_count |
| `product_list_view` | When product list is viewed | product_count, list_type   |

## Development

### Running Locally

1. Start PostgreSQL:
   ```bash
   docker-compose up -d postgres
   ```

2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

### Running Tests

`SpringBootGoogleAnalyticsApplicationTests` loads the full Spring context, so a PostgreSQL instance must be reachable:

```bash
docker run -d --rm --name ga-pg-test \
  -e POSTGRES_DB=productdb -e POSTGRES_USER=yu71 -e POSTGRES_PASSWORD=53cret \
  -p 5433:5432 postgres:17.5

./mvnw test
```

### Testing the API

```bash
# Create a product
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Product","description":"Test Description","price":19.99,"stockQuantity":50}'

# Get all products
curl http://localhost:8080/api/products

# Search products
curl "http://localhost:8080/api/products/search?name=Test"

# Get paginated products
curl "http://localhost:8080/api/products/paginated?page=0&size=5&sortBy=name&sortDir=asc"
```

## Continuous Integration

The [Java CI with Maven](.github/workflows/maven.yml) workflow runs on every push and pull request to `main`:

- Sets up **JDK 25** (Temurin) with Maven dependency caching
- Starts a **PostgreSQL 17.5** service container (`localhost:5433`) for the Spring context test
- Runs `mvn -B package` (compile + test + package)
- Submits the Maven dependency graph to GitHub on pushes to `main` (improves Dependabot alerts)

## Dependency Management

[Dependabot](.github/dependabot.yml) keeps dependencies up to date:

| Ecosystem      | Schedule              | Grouping                                                    | Commit prefix   |
|----------------|-----------------------|-------------------------------------------------------------|-----------------|
| Maven          | Daily, 05:00 WIB      | `spring-boot`, `google`, other minor/patch updates          | `build(deps)`   |
| GitHub Actions | Daily, 05:00 WIB      | All actions in one PR                                       | `ci(deps)`      |
| Docker         | Weekly (Mon), 05:00   | Major `eclipse-temurin` bumps ignored (stay on JDK 25 LTS)  | `build(docker)` |
| Docker Compose | Weekly (Mon), 05:00   | -                                                           | `build(docker)` |

## Docker Commands

```bash
# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop services
docker-compose down

# Remove volumes (data will be lost)
docker-compose down -v
```

## Database Schema

The `products` table includes:

- `id` (Primary Key, Auto-increment)
- `name` (Required)
- `description` (Optional, Text)
- `price` (Required, Decimal)
- `stock_quantity` (Required, Integer)
- `created_at` (Timestamp)
- `updated_at` (Timestamp)

## Monitoring and Logs

- Application logs are available in `/app/logs` within the container
- Google Analytics events are logged for debugging
- Database queries can be viewed by setting `spring.jpa.show-sql=true`

## Production Considerations

1. **Security**: Implement authentication and authorization
2. **Google Analytics**: Configure proper GA4 credentials and API integration
3. **Database**: Use connection pooling and proper indexing
4. **Monitoring**: Add health checks and metrics
5. **Caching**: Implement caching for frequently accessed data
6. **Error Handling**: Add global exception handling

## License

This project is licensed under the MIT License.