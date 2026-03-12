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

## Aggregator Pattern in Booking Service
The Booking Service implements the Aggregator Microservices Pattern to provide a unified view of booking data enriched with related cinema and movie information.
- Endpoint:
    ```
    GET /aggregated-bookings (via API Gateway: http://localhost:8085/aggregated-bookings)
    ```
- How it works:
The booking-service exposes an /aggregated-bookings endpoint that:
  - Retrieves all bookings from its own database.
  - For each booking, fetches the corresponding cinema and movie details from the cinema-service and movie-service using service discovery and REST calls.
  - Aggregates the data into a single response object per booking, containing booking, cinema, and movie information.
- Example Aggregated Response:
    ```
    [
      {
        "bookingId": 1,
        "cinema": {
          "id": 1,
          "name": "cinema4",
          "location": "Piazza4"
        },
        "movie": {
          "id": 1,
          "title": "fikr siferd",
          "genre": "romantic",
          "duration": 120
        },
        "seatNumber": 1,
        "userEmail": "abc@abc.com"
      }
    ]
    ```
## CQRS Pattern
All main services (cinema, movie, booking) implement the CQRS (Command Query Responsibility Segregation) pattern at the API/controller level:
 - Command controllers handle write operations (POST, PUT, DELETE).
 - Query controllers handle read operations (GET), including aggregation endpoints.

## API Gateway
- Spring Cloud Gateway is used as the single entry point for all client requests.
- It routes incoming requests to the appropriate microservice using Eureka service discovery.
- All microservices are accessible via the gateway, simplifying client configuration and enabling cross-cutting features (security, rate limiting, etc.) in one place.
  
**Gateway routes:**
- /cinemas/** → Cinema Service
- /movies/** → Movie Service
- /bookings/** → Booking Service
  
**Access the gateway at:**
  http://localhost:8085

**Example requests:**
- http://localhost:8085/cinemas
- http://localhost:8085/movies
- http://localhost:8085/bookings

## Service Discovery with Eureka
- Eureka server is included as a service in docker-compose.yml and runs on port 8761.
- Each microservice is configured as a Eureka client and registers itself automatically with the Eureka server at startup.
- Inter-service communication uses service names (e.g., http://cinema-service/...) instead of hardcoded host:port, enabling dynamic discovery and load balancing.

  Example:
  In booking-service, you can call the cinema service using:
  ```
  webClientBuilder.build().get()
    .uri("http://cinema-service/cinemas/{cinemaId}/seats/{seatNumber}/available", ...)
    .retrieve()
    .bodyToMono(Boolean.class)
    .block();
  ```
- The Eureka dashboard is available at http://localhost:8761, where you can see all registered services and their status.
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

## Starting Docker Daemon on WSL2/Ubuntu

If you are running on WSL2 or Ubuntu and Docker Desktop is **not** installed, start the Docker daemon manually before using Docker commands:

```
sudo service docker start
```
After starting, you can run _docker ps_ or _docker-compose up_ as usual.

## Running the Application
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
If you get error "KeyError: 'ContainerConfig' in your docker-compose up -d output, it usually means that Docker Compose is trying to recreate containers from images that are either corrupted, missing, or not properly built
To fix, run:
```
docker-compose down --rmi all --volumes --remove-orphans
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

## Keycloak Setup Guide (Required for Non-Local Profile)
Note: Keycloak setup is required when running microservices with any profile other than local (e.g., prod, dev).
In the local profile, security is bypassed and Keycloak is not needed for API access.

1. Keycloak Container Configuration:
   - Keycloak is included in the Docker Compose setup and runs as a container.
   - The recommended configuration uses the service name keycloak for inter-container communication.
   - Persistent data is stored in the keycloak_data Docker volume to ensure realms, clients, and users are not lost across restarts.

  Example Docker Compose Keycloak service:
  ```
  keycloak:
    image: quay.io/keycloak/keycloak:24.0.1
    environment:
      - KEYCLOAK_ADMIN=admin
      - KEYCLOAK_ADMIN_PASSWORD=admin
    command: start-dev --hostname-strict=false --hostname-strict-https=false --hostname=keycloak
    ports:
      - "8080:8080"
    volumes:
      - keycloak_data:/opt/keycloak/data
  ```
2. **Initial Keycloak Setup**

   2.1. **Access Keycloak Admin Console:**
     - Open http://localhost:8080/admin in your browser.
     - Login with the admin credentials (admin / admin by default).

   2.2. **Create a Realm:**
     - Click the dropdown at the top left and select "Create Realm".
     - Enter angul-ar as the realm name and click "Create".

   2.3. **Create Clients (one per microservice):**
     - Go to "Clients" in the left sidebar.
     - Click "Create Client".
     - Set Client ID (e.g., cinema-service).
     - Select OpenID Connect as the protocol.
     - Enable Client authentication (toggle ON).
     - Click "Save".
     - Go to the "Credentials" tab and copy the client_secret.

   2.4. **Configure Client Settings:**
     - In the "Settings" tab, set "Valid Redirect URIs" to * or your actual callback URLs.
     - Enable "Direct Access Grants" for password-based token requests.

   2.5. **Create Users:**
    - Go to "Users" in the left sidebar.
    - Click "Add user".
    - Fill in the username and other details, then click "Create".
    - Go to the "Credentials" tab, set a password, and set "Temporary" to OFF.
    - Click "Set Password".
    - Optionally, set "Email Verified" to ON. 

   2.6. **Disable Unnecessary Required Actions (for development):**
    - Go to "Authentication" > "Required Actions".
    - Disable actions like "Configure OTP", "Update Password", etc., unless needed.

3. Microservice Configuration
 Each microservice should have the following in its application.yml:
  ```
  spring:
    security:
      oauth2:
        resourceserver:
          jwt:
            issuer-uri: http://keycloak:8080/realms/angul-ar
  ```
4. Troubleshooting
   - If you get 401 Unauthorized, ensure the JWT iss claim matches the issuer-uri in your microservice config.
   - If tokens are not accepted, check that Keycloak is started with --hostname=keycloak and that your microservices use issuer-uri: http://keycloak:8080/realms/angul-ar.
   - For persistent Keycloak data, avoid running docker-compose down -v unless you want to reset all Keycloak configuration.

## Token Endpoint Example:
To obtain a token for API requests, use:
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

## Logging
 - All services use SLF4J with Logback for logging.
 - A custom `logback-spring.xml` is provided for each service.
 - **Trace ID** from distributed tracing (OpenTelemetry) is included in every log line for easy correlation across services.
 - Example log output:
   ```
   2026-03-05 16:36:24.457 [http-nio-8080-exec-4] INFO c.a.b.application.BookingService - trace_id=ef71c86959b5b5fcdd12d028ec736e2a span_id=ab2ed612e2ec25c3 Booking created successfully: 1
   ```

## Development Notes
  - H2 Console: Accessible at /h2-console for each service
  - Persistent Keycloak Data:
    - Docker volume keycloak_data ensures realms/clients/users persist across restarts.
  - Networking:
    - For local dev, use **localhost** for browser/Postman and **host.docker.internal** or **service name** for container-to-container communication as needed.

## Testing
### Unit and Integration Tests
- Cinema and Booking microservices include both unit and integration tests (TODO: add tests to movie service)
- Unit tests cover business logic in isolation using Mockito.
- Integration tests use the real Spring context and in-memory H2 database, and mock external service calls where appropriate.
- To run all tests locally:
  ```
  cd cinema-service && mvn test && cd ..
  cd movie-service && mvn test && cd ..
  cd booking-service && mvn test && cd ..
  ```
- Test reports are generated in the `target/surefire-reports/` directory of each service.

## CI/CD Pipeline
- The project includes a single GitLab CI/CD pipeline (`.gitlab-ci.yml`) for all services, with the following stages:
    - **build**: Compiles and packages all microservices.
    - **test**: Runs unit and integration tests for all microservices.
    - **docker**: Builds Docker images for all services.
    - **deploy**: (Currently a placeholder) Intended to deploy the services to a remote server via SSH.
      - The deploy stage contains dummy settings and will fail unless you update it with your actual server credentials and deployment script.
      - To enable deployment, replace `youruser@your-server-ip` and `/home/youruser/project/deploy.sh` in `.gitlab-ci.yml` with your real server details and ensure SSH keys are configured in GitLab CI/CD variables.
- Build artifacts (JAR files) are passed between stages using GitLab artifacts.
- The pipeline ensures that only tested and built images are deployed.

## TODO
- [ ] Configure the deploy stage in `.gitlab-ci.yml` with real server credentials and deployment script.
- [ ] Add unit and integration tests for
  the movie service.
- [ ] (Optional) Add test coverage reporting and/or pipeline status badges.

## Project Structure
/cinema-service
/movie-service
/booking-service
/docker-compose.yml
/opentelemetry-javaagent.jar

## What to Do Next (Action Plan)
  - Prepare Kubernetes manifests/Helm charts for all services.
  - Deploy to EPAM Cloud (or another cloud) and document the process.
  - Switch to platform-provided service discovery for cloud deployment.
  - (Optional) Split CI/CD pipelines per service.
  - Add test coverage, logging, and API documentation.
  - Update the README with cloud deployment, CQRS, and service discovery details.

## Troubleshooting
- 401 Unauthorized:
  - Ensure the JWT iss claim matches the issuer-uri in your microservice config.
  - For local dev, use the local  profile to bypass security.
- Keycloak data lost after restart:
  - **Do not** use **docker-compose down -v** unless you want to reset all data.
- Network issues:
  - Ensure Windows firewall allows inbound connections on port 8080 and mapped microservice ports.

