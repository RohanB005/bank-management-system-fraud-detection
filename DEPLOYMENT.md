# Bank Management System - Complete Production Deployment Guide

This guide provides step-by-step instructions for building, deploying, and maintaining the Bank Management System using Docker and Docker Compose on Ubuntu / AWS EC2.

---

## 1. System Architecture Overview

The system consists of **9 containerized services** connected through an isolated Docker bridge network (`bank-network`):

```text
                                 +-------------------------+
                                 |  User Browser (Port 80) |
                                 +------------+------------+
                                              |
                                              v
                                 +------------+------------+
                                 | bank-frontend (Nginx)   |
                                 +------------+------------+
                                              |
     +-----------------+----------------------+----------------------+-----------------+-----------------+
     |                 |                      |                      |                 |                 |
     v                 v                      v                      v                 v                 v
+----+----+     +------+-----+          +-----+------+         +-----+------+    +-----+------+    +-----+------+
|  Auth   |     |  Account   |          | Transaction|         |TransferFlow|    | .NET Fraud |    |    Admin   |
| (9090)  |     |   (8082)   |          |   (8086)   |         |   (8083)   |    |   (5000)   |    |   (9098)   |
+----+----+     +------+-----+          +-----+------+         +-----+------+    +-----+------+    +-----+------+
     |                 |                      |                      |                 |                 |
     +-----------------+----------------------+----------------------+-----------------+-----------------+
                                              |
                                              v
                                 +------------+------------+
                                 |   MySQL 8 Database      |
                                 |   Container (Port 3306) |
                                 +-------------------------+
```

### Services Summary

1. **`mysql`**: MySQL 8.0 Database (stores schema `bankmanagementdb`, seeded automatically via `./database/bankmanagementdb.sql`).
2. **`bank-auth-service`**: Java 21 Spring Boot service managing authentication, OTPs, and customer identities.
3. **`bank-account-service`**: Java 21 Spring Boot service managing customer bank accounts and balances.
4. **`bank-transaction-service`**: Java 21 Spring Boot service handling deposits, withdrawals, and transaction history.
5. **`transfer-flow-service`**: Java 21 Spring Boot service orchestrating fund transfers and integrating with fraud checks.
6. **`fraud-detection-service`**: .NET 8 ASP.NET Core Web API assessing transaction risk and AI explanations via Gemini API.
7. **`bank-ivr-service`**: Java 21 Spring Boot phone banking / IVR service.
8. **`bank-admin-service`**: Java 21 Spring Boot dashboard and administrative service.
9. **`frontend`**: React 19 + Vite 8 SPA served via Nginx with automated API reverse proxying on Port 80.

---

## 2. Folder Structure

```text
bank-management-system-fraud-detection/
├── .dockerignore
├── docker-compose.yml
├── DEPLOYMENT.md
├── AWS_DEPLOYMENT.md
├── database/
│   └── bankmanagementdb.sql
├── frontend/
│   ├── Dockerfile
│   ├── nginx.conf
│   ├── .env.production
│   └── src/
└── backend/
    ├── Bank-Admin/ (Dockerfile)
    ├── TransferFlow/ (Dockerfile)
    ├── bank-account-service/ (Dockerfile)
    ├── bank-auth-service/ (Dockerfile)
    ├── bank-ivr-service/ (Dockerfile)
    ├── bank-transaction-service/ (Dockerfile)
    └── fraud-detection-service/ (Dockerfile)
```

---

## 3. Prerequisites

- Operating System: Ubuntu 24.04 LTS (or 20.04/22.04 LTS)
- Docker: Engine 24.0+ installed
- Docker Compose: v2.20+ installed
- Minimum System Requirements: 2 vCPUs, 4 GB RAM (8 GB recommended for concurrent Maven builds)

---

## 4. Environment Variables

| Variable | Default Value | Description |
| :--- | :--- | :--- |
| `DB_HOST` | `mysql` | Hostname of the database container |
| `DB_PORT` | `3306` | Port of the database container |
| `DB_NAME` | `bankmanagementdb` | Target database name |
| `DB_USER` | `root` | Database username |
| `DB_PASS` | `root` | Database password |
| `JWT_SECRET` | `ThisIsMyVerySecret...` | Secret key for signing JWT tokens |
| `GEMINI_API_KEY` | *(Optional)* | Google Gemini API key for AI Fraud Explanations |

---

## 5. Dockerfiles Architecture

Every microservice uses **multi-stage builds** to maximize performance and security:
- **Java Services Stage 1**: `maven:3.9.9-eclipse-temurin-21-alpine` compiles source code and builds the executable `.jar`.
- **Java Services Stage 2**: `eclipse-temurin:21-jre-alpine` provides a minimal ~180MB runtime.
- **.NET Service Stage 1**: `mcr.microsoft.com/dotnet/sdk:8.0` publishes Release binaries.
- **.NET Service Stage 2**: `mcr.microsoft.com/dotnet/aspnet:8.0` executes `FraudDetectionService.dll`.
- **React Frontend Stage 1**: `node:20-alpine` builds Vite production assets into `/app/dist`.
- **React Frontend Stage 2**: `nginx:alpine` serves static assets and reverse proxies API routes.

---

## 6. Build & Deployment Commands

### Step 1: Clone Repository
```bash
git clone https://github.com/RohanB005/bank-management-system-fraud-detection.git
cd bank-management-system-fraud-detection
```

### Step 2: Build All Docker Images
```bash
docker compose build
```

### Step 3: Start All Services in Background
```bash
docker compose up -d
```

### Step 4: Verify Container Health
```bash
docker compose ps
```

---

## 7. Verification Steps

1. **Verify Database Initialization**:
   ```bash
   docker exec -it mysql mysql -u root -proot bankmanagementdb -e "SHOW TABLES;"
   ```

2. **Verify Backend Service Health**:
   ```bash
   curl http://localhost:9090/actuator/health
   curl http://localhost:8082/actuator/health
   curl http://localhost:8086/actuator/health
   curl http://localhost:8083/actuator/health
   curl http://localhost:5000/swagger/index.html
   ```

3. **Verify Frontend UI**:
   Open `http://<YOUR_SERVER_IP>` in your web browser.

---

## 8. Maintenance, Updates & Rollback

### Code Update Procedure
```bash
git pull
docker compose build --no-cache
docker compose up -d
```

### Rollback Procedure
```bash
git checkout <PREVIOUS_COMMIT_SHA>
docker compose up -d --build
```

### Reset & Cleanup Data
```bash
docker compose down -v
docker compose up -d
```

---

## 9. Troubleshooting & Common Fixes

- **MySQL Connection Refused**:
  Ensure MySQL container health check passes (`docker compose ps`). The backend services automatically wait for `mysql` health check before starting.
- **Port Conflict on 80 / 3306**:
  Check running local services with `sudo netstat -tulpn | grep -E '80|3306'` and stop any conflicting system services (e.g. local apache2 or mysql).
- **Out of Memory during Maven Build**:
  Ensure swap file is enabled or build images sequentially:
  ```bash
  docker compose build bank-auth-service
  docker compose build bank-account-service
  ...
  ```
