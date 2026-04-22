# Bunny.net Deployment Guide

## Pasos:

### 1. Ir a https://dash.bunny.net
- Create Account con GitHub
- New Environment → Java

### 2. Connect GitHub
- Repository: victorcelemin/transporte-backend

### 3. Configure:
- Build Command: `mvn clean package -DskipTests`
- Run Command: `java -jar target/transporte-backend-1.0.0.jar`

### 4. Environment Variables:
- DB_URL=tu_mysql_url
- DB_USER=tu_usuario
- DB_PASSWORD=tu_password

### 5. Deploy