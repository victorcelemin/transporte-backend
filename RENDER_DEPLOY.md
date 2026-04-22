# 🚀 GUÍA DE DESPLIEGUE EN RENDER - Transporte Backend

## 📋 Checklist de Verificación

- [x] application.properties con variables de entorno
- [x] Procfile creado
- [x] CORS configurado
- [x] Puerto dinámico configurado

---

## 🔧 Configuración Realizada

### 1. application.properties
Usa variables de entorno con valores por defecto:
- `${DB_URL}` - URL de MySQL
- `${DB_USER}` - Usuario MySQL
- `${DB_PASSWORD}` - Password MySQL
- `${PORT:8080}` - Puerto dinámico
- `${JWT_SECRET}` - Clave JWT
- `${JWT_EXPIRATION}` - Expiración token

### 2. Procfile
```
web: java -jar target/transporte-backend-1.0.0.jar
```

### 3. CORS
Permite todos los orígenes, métodos y headers.

---

## 📦 PASOS PARA DESPLIEGUE

### Paso 1: Subir a GitHub

```bash
git init
git add .
git commit -m "Preparado para Render"
git branch -M main
git remote add origin https://github.com/tu-usuario/transporte-backend.git
git push -u main
```

### Paso 2: Crear Base de Datos en Render

1. Ve a [Render Dashboard](https://dashboard.render.com)
2. Click **"New +"** → **"PostgreSQL"** o **"MySQL"**
3. Configura:
   - Name: `transporte-db`
   - Database: `transporte_db`
4. Click **"Create Database"**
5. Copia la **Internal URL** (formato: `mysql://...`)

### Paso 3: Crear Web Service

1. Click **"New +"** → **"Web Service"**
2. Conecta tu repositorio GitHub
3. Configura:

| Campo | Valor |
|-------|-------|
| Name | `transporte-backend` |
| Build Command | `mvn clean install` |
| Start Command | `java -jar target/transporte-backend-1.0.0.jar` |
| Runtime | `Java 17` |

### Paso 4: Configurar Variables de Entorno

En la sección **"Environment"**, agrega:

```
DB_URL=mysql://USERNAME:PASSWORD@HOST:3306/transporte_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USER=tu_usuario
DB_PASSWORD=tu_password
JWT_SECRET=Y2FtYXNldHJhbnNwb3J0ZWJhc2VlbmNyeXB0b2tleXNlY3JldGhpcyRmMWMyZjNhZi1lMjM0LTQ0ODMtODFmYy1mNjc3ODkwMTIzNDMyZTI
JWT_EXPIRATION=86400000
```

**Nota:** DB_URL debe tener el formato completo de tu base de datos Render.

### Paso 5: Desplegar

1. Click **"Create Web Service"**
2. Espera a que termine el build (3-5 minutos)
3. Verifica en los logs que dice:

```
Tomcat started on port(s) 8080
Started TransporteApplication in X.XXX seconds
```

---

## 🧪 PRUEBAS EN POSTMAN

### URLBase
```
https://transporte-backend.onrender.com
```

### 1. Endpoint Público - Estadísticas

```http
GET {{url}}/api/publico/personas/estadisticas
```

**Headers:** (ninguno requerido)

**Response:**
```json
[
  { "tipoPersona": "C", "total": 0 },
  { "tipoPersona": "A", "total": 0 }
]
```

### 2. Crear Persona Admin (protegido)

```http
POST {{url}}/api/personas
Content-Type: application/json
Authorization: Bearer <token_jwt>
X-API-Key: <api_key>
```

**Body:**
```json
{
  "identificacion": "123456789",
  "tipoIdentificacion": "CC",
  "nombres": "Admin",
  "apellidos": "User",
  "correo": "admin@empresa.com",
  "tipoPersona": "A"
}
```

### 3. Obtener Token JWT

Como no hay login, genera el token manualmente:

En Java:
```java
import com.transporte.security.JwtUtil;
String token = jwtUtil.generateToken("login_usuario");
```

O usa una librería JWT:
```javascript
// JavaScript ejemplo
const jwt = require('jsonwebtoken');
const token = jwt.sign(
  { sub: 'login_usuario' },
  'JWT_SECRET_DEL_CONFIG',
  { expiresIn: '24h' }
);
```

### 4. Headers de Autenticación

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
X-API-Key: apiKey_generada_al_crear_usuario
```

---

## ❓ SOLUCIÓN DE PROBLEMAS

### Error: "Connection refused"
- Revisa que MySQL esté corriendo en Render
- Verifica DB_URL con formato correcto
- Asegúrate de que `createDatabaseIfNotExist=true`

### Error: "Port problem"
- El puerto ya está en uso
- Revisa que el Procfile use `${PORT}`
- El default es 8080

### Error: "CORS"
- CORS ya configurado
- Si persiste, verifica que los headers estén bien

### Error: "JWT"
- Verifica JWT_SECRET en variables de entorno
- Debe tener al menos 256 bits

---

## 📊 Endpoints Disponibles

| Método | Endpoint | Auth | Descripción |
|--------|----------|-----|-------------|
| GET | `/api/publico/personas/estadisticas` | ❌ | Stats |
| GET | `/api/publico/vehiculos/{placa}` | ❌ | Por placa |
| GET | `/api/publico/conductores/habilitados` | ❌ | Habilitados |
| POST | `/api/personas` | ✅ | Crear |
| GET | `/api/personas` | ✅ | Listar |
| PUT | `/api/personas/{id}` | ✅ | Actualizar |
| POST | `/api/vehiculos` | ✅ | Crear |
| POST | `/api/vehiculos/conductores` | ✅ | Asociar |
| PUT | `/api/vehiculos/conductores/{id}/estado` | ✅ | Estado |
| POST | `/api/documentos` | ✅ | Subir PDF |

---

## 📞 Logs en Render

Ver deploy:
```bash
# En dashboard Render → tu servicio → Logs
```

Buscar errores:
```
error
exception
failed
```

---

## ✅ Verificación Final

1. Accede a: `https://tu-servicio.onrender.com/api/publico/personas/estadisticas`
2. Debe responder JSON con estadísticas
3. ¡Listo! 🎉