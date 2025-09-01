# Spring Boot Product CRUD with Google Analytics 4 Integration

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

## Tech Stack

- Java 21
- Spring Boot 3.5.5
- Spring Data JPA
- PostgreSQL 17.5
- Docker & Docker Compose
- Google Analytics 4 API
- Lombok
- Maven

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
├── docker-compose.yml
├── Dockerfile
└── pom.xml
```

## Prerequisites

- Java 21
- Maven 3.6+
- Docker and Docker Compose
- Google Analytics 4 property (optional)

## Quick Start

### 1. Clone and Build

```bash
git clone <repository-url>
cd spring-boot-google-analytics
mvn clean package -DskipTests
```

### 2. Run with Docker Compose

```bash
docker-compose up -d
```

This will start:

- PostgreSQL 17.5 on port 5432
- Spring Boot application on port 8080

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
- Username: `admin`
- Password: `password123`
- Port: `5432`

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
| `SPRING_DATASOURCE_URL`      | Database URL                 | jdbc:postgresql://localhost:5432/productdb |
| `SPRING_DATASOURCE_USERNAME` | Database username            | admin                                      |
| `SPRING_DATASOURCE_PASSWORD` | Database password            | password123                                |

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
   mvn spring-boot:run
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