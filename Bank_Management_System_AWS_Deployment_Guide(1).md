# Bank Management System Deployment Guide (After Cloning Repository)

> Assumptions: - Ubuntu, Docker and Docker Compose are already
> installed. - Source code has been cloned. - Stack: - React (Vite) - 5
> Java 21 services - 1 .NET 8 service - MySQL 8

## 1. Clone the repository

``` bash

cd <PROJECT_ROOT>
```

Verify the structure:

``` text
PROJECT_ROOT/
├── frontend/
├── java-service-1/
├── java-service-2/
├── java-service-3/
├── java-service-4/
├── java-service-5/
├── dotnet-service/
└── docker-compose.yml
```

## 2. Configure Environment Variables

Create or update the required `.env` files.

Example:

``` env
DB_HOST=mysql
DB_PORT=3306
DB_NAME=bankdb
DB_USERNAME=bankuser
DB_PASSWORD=bankpassword
```

Update each Java and .NET service to use these variables.

## 3. Build Docker Images

From the project root:

``` bash
docker compose build
```

Or build without cache:

``` bash
docker compose build --no-cache
```

## 4. Start Containers

``` bash
docker compose up -d
```

Check status:

``` bash
docker compose ps
```

View logs:

``` bash
docker compose logs -f
```

Logs for one service:

``` bash
docker compose logs -f java-service-1
```

## 5. Verify Running Containers

``` bash
docker ps
```

## 6. Verify MySQL

``` bash
docker exec -it mysql mysql -u root -p
```

List databases:

``` sql
SHOW DATABASES;
```

## 7. Verify Backend APIs

Examples:

``` bash
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8090/health
```

Adjust ports/endpoints to your application.

## 8. Verify Frontend

Open:

    http://<EC2_PUBLIC_IP>

or

    http://<EC2_PUBLIC_IP>:3000

depending on your Nginx/Compose configuration.

## 9. Common Docker Commands

Restart:

``` bash
docker compose restart
```

Stop:

``` bash
docker compose down
```

Stop and remove volumes:

``` bash
docker compose down -v
```

Rebuild and restart:

``` bash
docker compose up -d --build
```

## 10. Update Application After Code Changes

``` bash
git pull
docker compose down
docker compose build --no-cache
docker compose up -d
```

## 11. Useful Troubleshooting

Running containers:

``` bash
docker ps
```

Stopped containers:

``` bash
docker ps -a
```

Container logs:

``` bash
docker logs <container-name>
```

Disk usage:

``` bash
docker system df
```

Clean unused resources:

``` bash
docker system prune -a
```

## 12. Production Checklist

-   Docker images build successfully.
-   All services are healthy.
-   MySQL data is persisted using Docker volumes.
-   Backend services can connect to MySQL.
-   Frontend points to the correct backend URLs.
-   Only ports 80 and 443 are publicly exposed when using Nginx.
-   HTTPS configured (recommended).
-   Back up database regularly.

## Deployment Workflow

``` text
git clone
    ↓
Configure .env
    ↓
docker compose build
    ↓
docker compose up -d
    ↓
docker compose ps
    ↓
Verify APIs
    ↓
Open Frontend
```
