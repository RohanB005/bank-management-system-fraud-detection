# AWS EC2 Ubuntu Deployment Guide

This document covers step-by-step instructions for deploying, updating, and monitoring the Bank Management System on an AWS EC2 instance running Ubuntu 24.04 LTS.

---

## 1. AWS EC2 Prerequisites & Security Group Setup

### Recommended EC2 Instance Type
- **Minimum**: `t3.medium` (2 vCPUs, 4 GB RAM)
- **Recommended**: `t3.large` (2 vCPUs, 8 GB RAM)
- **Storage**: 20 GB+ gp3 EBS Volume

### AWS EC2 Security Group Inbound Rules

| Port | Protocol | Source | Purpose |
| :--- | :--- | :--- | :--- |
| `22` | TCP | `0.0.0.0/0` (or Your IP) | SSH Remote Access |
| `80` | TCP | `0.0.0.0/0` | Web Application (React UI & Nginx Reverse Proxy) |
| `443` | TCP | `0.0.0.0/0` | HTTPS (if SSL Certificate is enabled) |

---

## 2. Server Initial Setup & Dependencies

Connect to your EC2 instance via SSH:
```bash
ssh -i "your-key.pem" ubuntu@<YOUR_EC2_PUBLIC_IP>
```

Update system dependencies and install Docker & Docker Compose:
```bash
sudo apt-get update && sudo apt-get upgrade -y
sudo apt-get install -y ca-certificates curl gnupg lsb-release

# Install Docker
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# Enable Docker without sudo
sudo usermod -aG docker $USER
newgrp docker
```

---

## 3. Deployment Steps on EC2

### Step 1: Clone Repository
```bash
git clone https://github.com/RohanB005/bank-management-system-fraud-detection.git
cd bank-management-system-fraud-detection
```

### Step 2: Build All Service Images
```bash
docker compose build
```

### Step 3: Launch System Containers
```bash
docker compose up -d
```

### Step 4: Verify Deployment Status
```bash
docker compose ps
```

---

## 4. Monitoring & Logs Management

### View All Logs in Real-time
```bash
docker compose logs -f
```

### View Specific Microservice Logs
```bash
# View Auth Service Logs
docker compose logs -f bank-auth-service

# View Fraud Service Logs
docker compose logs -f fraud-detection-service

# View Frontend Logs
docker compose logs -f frontend
```

### Check Container Resource Usage (CPU & Memory)
```bash
docker stats
```

---

## 5. Operations & Service Management

### Restarting All Services
```bash
docker compose restart
```

### Stopping Services
```bash
docker compose stop
```

### Stopping Services & Cleaning Containers
```bash
docker compose down
```

---

## 6. Updating Code & Re-deploying on EC2

To deploy new commits or code updates:

```bash
cd bank-management-system-fraud-detection

# Pull latest changes from git
git pull origin main

# Rebuild images without cache
docker compose build --no-cache

# Restart updated containers
docker compose up -d
```

---

## 7. Database Backups & Management

### Create MySQL Database Backup Dump
```bash
docker exec mysql mysqldump -u root -proot bankmanagementdb > ~/bankmanagementdb_backup_$(date +%Y%m%d_%H%M%S).sql
```

### Restore Database Dump
```bash
docker exec -i mysql mysql -u root -proot bankmanagementdb < ~/bankmanagementdb_backup.sql
```
