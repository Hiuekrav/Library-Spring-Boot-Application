# Library - Spring Boot Application
## Overview
This project is a Library Management System implemented using the Spring Boot framework. The application follows a container-component architecture and utilizes the Spring IoC container. It models a simple library business scenario where end-users (in other words Clients) can exclusively allocate certain resources (Books) within specified time intervals. The application is designed to handle various operations such as CRUD (Create, Read, Update, Delete) for resources and users, managing book rentals, and handling user activations and deactivations.

## Key Features
- **CRUD Operations:** Full CRUD operations for Books, Users, and Rents.
- **User Management:** Different user roles (Admin, Librarian, Reader) with specific functionalities.
- **Rental Management:** Manage book rentals, including creating, updating and ending rentals.
- **Validation:** Comprehensive validation of input data to ensure data integrity.
- **Exception Handling:** Custom exception handling with appropriate HTTP status codes.
- **Transaction Management:** Ensures data consistency during operations like book rentals.
- **Swagger Integration:** Easy API testing and documentation using Swagger UI.
- **Reflection in ObjectRepository:** Utilizes Java reflection for save operation (includes create and update operations).
- **Race Condition Resilience:** Implements transactional processing to handle concurrent requests safely.
- **Shared Repository Objects:** Repository instances are shared across REST requests for efficient resource management.

## Project Structure
The project is divided into several modules and layers:
### Modules
1. **REST Module:** Contains controllers, services, repositories, exception handling, model and mappers.
2. **DTO Module:** Contains data transfer objects (DTOs) for create, update, and output operations.

## Layers
1. **Controller Layer:** Handles HTTP requests and responses.
    - ```BookController```: Manages book-related endpoints.
    - ```RentController```: Manages rental-related endpoints.
    - ```UserController```: Manages user-related endpoints. 
2. **Service Layer:** Implements business logic and interacts with repositories.
    - ```BookService```: Manages book operations.
    - ```RentService```: Manages rental operations.
    - ```UserService```: Manages user operations.
    - ```ObjectService```: Initialize Mongo database connection.
3. **Repository Layer:** Handles data access and database operations.
    - ```BookRepository```: Manages book data.
    - ```RentRepository```: Manages rental data.
    - ```UserRepository```: Manages user data.
    - ```ObjectRepository```: Generic repository for common operations.
4. **Exception Layer:** Custom exception handling and resolvers.
5. **DTO Layer:** Data transfer objects for API communication.

## Business Logic Details
### Reflection in ObjectRepository
The ObjectRepository uses Java reflection to provide generic CU operations. This allows the repository to handle any entity type dynamically, reducing code duplication and improving maintainability. The **save** method, which is using reflection can handle both create and update operations by inspecting the entity's state.

### Transactional Processing
The application ensures data consistency and resilience to race conditions through transactional processing. For instance, when creating a rental, the system locks the book and user resources to prevent concurrent modifications.

### Shared Repository Objects
Repository instances are shared across REST requests, reducing the overhead of creating new instances for each request. This design choice improves performance and scalability.

### Business Rules
- **User Roles:** Users are categorized into Admins, Librarians, and Readers. The user hierarchy will be needed in the future, when endpoints will be secured and specific permissions and functionalities introduced.
- **Rental Constraints:** A book can only be rented if it is available and the user is active. Rentals can be scheduled for future dates.
- **Archival System:** Books and rentals can be archived, making them inactive but preserving their data for historical records.
- **Validation:** All input data is rigorously validated to ensure compliance with business rules, such as checking for duplicate emails or invalid rental periods.

## Setup and Testing the Project
### Prerequisites
- Docker
- Java 21
- Maven

### Installation
1. Clone this repository.
2. Set up MongoDB replica set by running services directly from docker-compose using IDE or by executing ```docker-compose up -d``` command.
3. Build the project: ```mvn clean install```
4. Run the application: use ```mvn spring-boot:run``` command from REST directory in your terminal (or run main method from RestApplication class in your IDE).
5. Access **Swagger UI** (recommended): Open your browser and navigate to http://localhost:8080/swagger-ui/index.html to access the Swagger UI for API documentation and testing. Alternatively you can use Postman or cURL for testing endpoints.

## API Endpoints
### BookController
- ```GET /api/books/{id}```: Get a book by ID.
- ```POST /api/books/create```: Create a new book.
- ```GET /api/books```: Find a book by title.
- ```GET /api/books/all```: Get all books.
- ```PUT /api/books/{id}```: Update a book.
- ```DELETE /api/books/{id}```: Delete a book.
- ```POST /api/books/{id}/archive```: Archive a book.
- ```POST /api/books/{id}/activate```: Activate a book.

### RentController
- ```POST /api/rents```: Create a new rental.
- ```POST /api/rents/now```: Create a rental with immediate start time.
- ```GET /api/rents/all```: Get all rentals.
- ```GET /api/rents/{id}```: Get a rental by ID.
- ```GET /api/rents/reader/{id}/all```: Get all rentals by reader ID.
- ```GET /api/rents/reader/{id}/active```: Get active rentals by reader ID.
- ```GET /api/rents/reader/{id}/archive```: Get archived rentals by reader ID.
- ```GET /api/rents/reader/{id}/future```: Get future rentals by reader ID.
- ```GET /api/rents/book/{id}/all```: Get all rentals by book ID.
- ```GET /api/rents/book/{id}/active```: Get active rentals by book ID.
- ```GET /api/rents/book/{id}/archive```: Get archived rentals by book ID.
- ```GET /api/rents/book/{id}/future```: Get future rentals by book ID.
- ```PUT /api/rents/{id}```: Update a rental.
- ```POST /api/rents/{id}/end```: End a rental.
- ```DELETE /api/rents/{id}```: Delete a rental.

### UserController
- ```POST /api/users/create-admin```: Create an admin user.
- ```POST /api/users/create-librarian```: Create a librarian user.
- ```POST /api/users/create-reader```: Create a reader user.
- ```GET /api/users/{id}```: Get a user by ID.
- ```GET /api/users```: Find a user by email.
- ```GET /api/users/all```: Get all users.
- ```PUT /api/users/{id}```: Update a user.
- ```POST /api/users/{id}/activate```: Activate a user.
- ```POST /api/users/{id}/deactivate```: Deactivate a user.

## Testing
The application includes test classes for BookController, RentController, and UserController. You can run the tests using Maven:
```
mvn test
```
or using your IDE.

## Authors

### Wiktoria Bilecka
### Grzegorz Janasek
