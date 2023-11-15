
# Synchrony Users Application

The Synchrony Users Application is a Spring Boot RESTful API designed to manage user profiles and associated images. It provides endpoints for registration, retrieving user profile details, uploading images, and deleting images.
## Features

- Register User: Register a user using basic authentication
- Get User Profile Details: Retrieve user profile details.
- Upload Image: Upload an image with a title ans associate to the profile.
- Delete Image: Delete an image by providing the delete hash.


## Installation

Clone the repository and build the project using your preferred IDE or build tool.

```bash
git clone https://github.com/srujanakalluru/Synchrony-User-Service.git
```
    
## Run Locally

Run the application and access the Swagger documentation at 
```http://localhost:8080/swagger-ui/```

### Configuration
Update `application.properties` as needed

## Technologies Used

- Spring Boot: Framework for creating standalone Spring-based Applications.
- Spring Security: Authentication and access control framework.
- Spring Data JPA: Provides repository support for the Java Persistence API.
- H2 Database: In-memory database for development and testing.
- Spring AOP: Aspect-oriented programming for cross-cutting concerns.
- Spring Validation: Validation support for Spring-based applications.
- Apache HttpClient: HTTP client library.
- Ehcache: Distributed caching
- Spring Kafka: Integration with the Apache Kafka (Coonfluent) message broker.
- Springfox: Swagger/OpenAPI integration for API documentation.
- Sonar: Sonar integration for static code analysis
- JaCoCo: Code coverage analysis
- JUnit and PowerMock: Testing frameworks for unit and integration testing.

## Authors

- [@srujana-kalluru](https://github.com/srujana-kalluru)
