# ☁️ GUÍA DE DESPLIEGUE EN COOLIFY

## Alternativa gratuita a Railway/Render

---

## 📋 Pasos:

### 1. Crear cuenta en Coolify
Ve a: https://coolify.io

### 2. Crear nuevo proyecto
- New Project → Give it a name

### 3. Agregar base de datos MySQL
- Resources → Add → MySQL
- Name: `transporte-mysql`
- Notar la connection string

### 4. Agregar Backend
- Add → Docker
- Repository: `https://github.com/victorcelemin/transporte-backend`
- Build Pack: `Dockerfile`
- (El proyecto ya tiene Dockerfile配置)

### 5. Configurar variables
Agregar en Environment:
```
DB_URL=jdbc:mysql://HOST:3306/transporte_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USER=usuario_mysql
DB_PASSWORD=password_mysql
JWT_SECRET=una_clave_secreta_de_al_menos_256_bits
```

### 6. Deploy
- Click Deploy

---

## ✅ Verificación
```
GET https://tu-dominio.coolify.io/api/publico/personas/estadisticas
```