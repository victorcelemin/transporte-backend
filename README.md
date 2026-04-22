# Transporte Backend API

API REST para gestión de transporte de personal. Construido con Spring Boot 3.2, Java 17, JPA/Hibernate y JWT Authentication.

## URL Base

```
https://transporte-backend-755906643293.us-central1.run.app
```

## Credenciales de Prueba

| Login    | Contraseña   | Rol       |
|----------|--------------|----------|
| admin    | admin123     | Admin    |
| operador | oper123      | Operador |

## Autenticación

### Login
```bash
curl -X POST https://transporte-backend-755906643293.us-central1.run.app/api/publico/login \
  -H "Content-Type: application/json" \
  -d '{"login":"admin","password":"admin123"}'
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer",
  "login": "admin"
}
```

### Usar Token
Incluir en todas las peticiones protegidas:
```bash
Authorization: Bearer <token>
```

## Endpoints Públicos (sin autenticación)

| Método | Endpoint                              | Descripción                    |
|-------|---------------------------------------|--------------------------------|
| GET   | `/api/health`                          | Health check                   |
| POST  | `/api/publico/login`                   | Login de usuario               |
| GET   | `/api/publico/personas/estadisticas`   | Estadísticas de personas       |
| GET   | `/api/publico/vehiculos/documentos-vencidos` | Documentos vencidos   |

## Endpoints Protegidos (requieren token)

### Personas
| Método | Endpoint                              | Descripción                    |
|-------|---------------------------------------|--------------------------------|
| GET   | `/api/personas`                       | Listar todas                   |
| GET   | `/api/personas/{id}`                   | Obtener por ID                |
| POST  | `/api/personas`                       | Crear persona                  |
| PUT   | `/api/personas/{id}`                  | Actualizar persona             |
| DELETE| `/api/personas/{id}`                  | Eliminar persona               |

### Usuarios
| Método | Endpoint                              | Descripción                    |
|-------|---------------------------------------|--------------------------------|
| GET   | `/api/usuarios`                       | Listar todos                  |
| GET   | `/api/usuarios/{id}`                  | Obtener por ID                 |
| POST  | `/api/usuarios`                       | Crear usuario                  |
| PUT   | `/api/usuarios/{id}`                  | Actualizar usuario            |
| DELETE| `/api/usuarios/{id}`                  | Eliminar usuario              |

### Vehículos
| Método | Endpoint                              | Descripción                    |
|-------|---------------------------------------|--------------------------------|
| GET   | `/api/vehiculos`                      | Listar todos                  |
| GET   | `/api/vehiculos/{id}`                 | Obtener por ID                 |
| POST  | `/api/vehiculos`                      | Crear vehículo                 |
| PUT   | `/api/vehiculos/{id}`                 | Actualizar vehículo            |
| DELETE| `/api/vehiculos/{id}`                 | Eliminar vehículo            |
| GET   | `/api/vehiculos/{id}/documentos`      | Documentos del vehículo        |

### Conductores
| Método | Endpoint                              | Descripción                    |
|-------|---------------------------------------|--------------------------------|
| GET   | `/api/vehiculos/{id}/conductor`        | Obtener conductor              |
| POST  | `/api/vehiculos/{id}/conductor`      | Asignar conductor              |
| PUT   | `/api/vehiculos/{id}/conductor`      | Actualizar conductor           |
| DELETE| `/api/vehiculos/{id}/conductor`      | Eliminar conductor             |

### Documentos
| Método | Endpoint                              | Descripción                    |
|-------|---------------------------------------|--------------------------------|
| GET   | `/api/documentos`                    | Listar todos                  |
| GET   | `/api/documentos/{id}`               | Obtener por ID                |
| POST  | `/api/documentos`                    | Crear documento               |
| PUT   | `/api/documentos/{id}`              | Actualizar documento         |
| DELETE| `/api/documentos/{id}`              | Eliminar documento           |

## Ejemplos con curl

### 1. Login
```bash
curl -X POST https://transporte-backend-755906643293.us-central1.run.app/api/publico/login \
  -H "Content-Type: application/json" \
  -d '{"login":"admin","password":"admin123"}'
```

### 2. Health Check
```bash
curl https://transporte-backend-755906643293.us-central1.run.app/api/health
```

### 3. Listar Personas (protegido)
```bash
curl https://transporte-backend-755906643293.us-central1.run.app/api/personas \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 4. Crear Persona
```bash
curl -X POST https://transporte-backend-755906643293.us-central1.run.app/api/personas \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "nombres": "Pedro",
    "apellidos": "García",
    "identificacion": "12345679",
    "tipoIdentificacion": "Cédula",
    "direccion": "Calle Nueva 456",
    "telefono": "555-9999",
    "correo": "pedro@email.com",
    "fechaNacimiento": "1995-07-20",
    "tipoPersona": "Empleado"
  }'
```

### 5. Crear Vehículo
```bash
curl -X POST https://transporte-backend-755906643293.us-central1.run.app/api/vehiculos \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "placa": "XYZ-1234",
    "marca": "Chevrolet",
    "modelo": "Spark",
    "color": "Gris",
    "anioFabricacion": 2022,
    "estado": "Activo"
  }'
```

### 6. Ver Estadísticas
```bash
curl https://transporte-backend-755906643293.us-central1.run.app/api/publico/personas/estadisticas
```

## Base de Datos

### MySQL Cloud (GCP Cloud SQL - Gratuito)
- **IP Pública**: `34.55.92.10`
- **Instancia**: `transporte-mysql`
- **Tier**: `db-f1-micro` (incluido en free tier)
- **Storage**: 10GB PD-SSD
- **Database**: `transporte_db`
- **Puerto**: 3306

### Tablas Creadas
- `persona` - Personas (conductores, pasajeros)
- `usuario` - Usuarios del sistema
- `vehiculo` - Vehículos de transporte
- `conductor` - Relación conductor-vehículo
- `documento` - Documentos de vehículos (SOAT, revisión)

## Tecnologías

| Componente       | Tecnología                        |
|----------------|----------------------------------|
| Lenguaje       | Java 17                          |
| Framework     | Spring Boot 3.2                  |
| Seguridad     | Spring Security + JWT              |
| Persistencia  | Spring Data JPA / Hibernate       |
| Base de Datos | MySQL 8.0 / H2 (desarrollo)     |
| Build         | Maven                           |
| Despliegue    | Google Cloud Run                  |

## Despliegue en Cloud Run

```bash
# Desplegar
gcloud run deploy transporte-backend --source . --region us-central1 --allow-unauthenticated

# Ver logs
gcloud run services logs read transporte-backend --region us-central1

# Configurar variables de entorno
gcloud run services update transporte-backend --region us-central1 --set-env-vars="DB_URL=jdbc:mysql://IP:3306/db,DB_USER=user,DB_PASSWORD=pass"
```

## Configuración

### Variables de Entorno
| Variable       | Descripción                         | Default                    |
|----------------|------------------------------------|----------------------------|
| `DB_URL`      | JDBC URL de MySQL                   | H2 en memoria             |
| `DB_USER`     | Usuario de base de datos             | `sa`                      |
| `DB_PASSWORD` | Contraseña de base de datos         | (vacío)                   |

### Perfiles
- `dev` - Desarrollo (H2 en memoria)
- `prod` - Producción (MySQL Cloud)

## API Documentation

### Request Headers Comunes
```
Content-Type: application/json
Authorization: Bearer <jwt_token>
```

### Códigos de Respuesta
| Código | Descripción                       |
|--------|----------------------------------|
| 200   | OK                              |
| 201   | Creado                          |
| 400   | Bad Request                     |
| 401   | Unauthorized                   |
| 403   | Forbidden                      |
| 404   | Not Found                      |
| 500   | Internal Server Error            |

## Notas

- El token JWT expira en 24 horas
- Los datos se reinician con H2 (en memoria)
- Para persistencia, usar MySQL Cloud