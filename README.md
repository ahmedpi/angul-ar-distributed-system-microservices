# Angul-AR Distributed System Microservices
## Overview
Angul-AR is a distributed, microservices-based cinema booking platform.
This project demonstrates best practices in microservices architecture, including containerization, distributed tracing, OAuth2 security, and cloud readiness.
## Architecture
- Microservices
  - **Cinema Service**: Manages cinemas
  - **Movie Service**: Manages movies
  - **Booking Service**: Manages bookings
- API Security: OAuth2/OpenID Connect via Keycloak
- Distributed Tracing: OpenTelemetry + Jaeger
- Containerization: Docker, orchestrated with Docker Compose
- Database: H2 (in-memory, per service)
- Local Security Bypass: Profile-based config for local development
## Key Features
- Hexagonal architecture for each service (domain, application, adapters)
- RESTful APIs for all services
- JWT-based authentication (OAuth2 Resource Server)
- Distributed tracing with OpenTelemetry and Jaeger
- Health and metrics endpoints via Spring Boot Actuator
- Local development ready (Postman, Docker Compose, WSL2/Windows support)
- Persistent Keycloak configuration using Docker volumes
- Profile-based security bypass for local development
## Local Development Security Bypass
For local development and testing, you can bypass security using a dedicated Spring profile (local).
This allows you to test APIs from Postman or your browser without requiring a Bearer token.
### How it works
SecurityConfig is active for all profiles except local and enforces JWT/OAuth2 security.
NoSecurityConfig is active only for the local profile and permits all requests (no authentication required).
### Activating Local Profile
In docker-compose.yml for each microservice:
```
environment:
  - SPRING_PROFILES_ACTIVE=local
 ```
Or run with:
```
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
Result:
- All endpoints are open for local development and testing.
- In other profiles, full security is enforced.

## Getting Started
### Prerequisites
- Docker
- Docker Compose
- Java 21+
- Spring Boot 3.5.x (project uses Spring Boot 3.5.8)
- Maven
- Postman (for API testing)

### 1. Clone the Repository
    ```
    git clone https://github.com/ahmedpi/angul-ar-distributed-system-microservices.git
    cd angul-ar-distributed-system-microservices
    ```
### 2. Build the Services
    ```
    cd cinema-service && mvn clean package && cd ..
    cd movie-service && mvn clean package && cd ..
    cd booking-service && mvn clean package && cd ..
    ```
### 3. Start All Services with Docker Compose (with local profile for security bypass)
    ``` 
    docker-compose up --build
    ```
    (Make sure each microservice in docker-compose.yml has SPRING_PROFILES_ACTIVE=local in its environment section for local dev.)
### 4. Access the Services
- Cinema Service: http://localhost:8081/cinemas
- Movie Service: http://localhost:8082/movies
- Booking Service: http://localhost:8083/bookings
- Keycloak Admin Console: http://localhost:8080/auth/admin
- Jaeger UI: http://localhost:16686

## Testing with Postman
You can use Postman to test the APIs without authentication when the local profile is active.

## Security (OAuth2/OpenID Connect)
- Identity Provider: Keycloak (Dockerized)
- Realm: angul-ar
- Clients: One per microservice (e.g., cinema-service)
- User: Create via Keycloak Admin UI

Token Endpoint Example:
```
POST http://localhost:8080/realms/angul-ar/protocol/openid-connect/token
```
Body (x-www-form-urlencoded):
- client_id: cinema-service
- client_secret: <client-secret>
- username:
- password:
- grant_type: password

Use the access_token in API requests:
``` Authorization: Bearer <access_token> ```

## Distributed Tracing  
- OpenTelemetry Java Agent is attached to each service.
- Jaeger as the tracing backend (Dockerized) - collects and visualizes traces
- View traces in Jaeger UI at http://localhost:16686

## Health and Metrics
Each service exposes:
- Health Endpoint: /actuator/health
- Metrics Endpoint: /actuator/metrics

## Development Notes
- H2 Console: Accessible at /h2-console for each service
- Persistent Keycloak Data:
  - Docker volume keycloak_data ensures realms/clients/users persist across restarts.
- Networking:
  - For local dev, use localhost for browser/Postman and host.docker.internal or service name for container-to-container communication as needed.

## Project Structure
/cinema-service
/movie-service
/booking-service
/docker-compose.yml
/opentelemetry-javaagent.jar

## Next Steps (Planned)
- Add API Gateway (Spring Cloud Gateway or Kong)
- Implement service discovery (Consul/Eureka)
- Set up CI/CD pipelines (GitHub Actions, GitLab CI, Jenkins)
- Prepare for cloud deployment (Kubernetes, EPAM Cloud)
- Add CQRS and data aggregation

## Troubleshooting
- 401 Unauthorized:
  - Ensure the JWT iss claim matches the issuer-uri in your microservice config.
  - For local dev, use the local  profile to bypass security.
- Keycloak data lost after restart:
  - Do not use docker-compose down -v unless you want to reset all data.
- Network issues:
  - Ensure Windows firewall allows inbound connections on port 8080 and mapped microservice ports.

