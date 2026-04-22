# 🚚 TRANSPORTE BACKEND - Documentación Completa del Proyecto

## 1. Visión General del Proyecto

### 1.1 Descripción

Este proyecto es un **sistema backend de gestión de transporte** desarrollado en Java 17 con Spring Boot 3.2. Proporciona una API REST completa para administrar:

- **Personas**: conductores y personal administrativo
- **Usuarios**: cuentas de acceso al sistema
- **Vehículos**: fleet de transporte
- **Conductores**: asociación de personas a vehículos
- **Documentos**: archivos PDF asociados a vehículos (SOAT, técnico mecánica, etc.)

### 1.2 Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|--------|----------|
| Java | 17+ | Lenguaje de programación |
| Spring Boot | 3.2.0 | Framework principal |
| Spring Data JPA | - | Acceso a datos |
| Spring Security | - | Seguridad |
| MySQL | 8.0 | Base de datos |
| JWT (jjwt) | 0.12.3 | Tokens |
| Lombok | - |减少 código |
| Maven | - | Build tool |

### 1.3 Requisitos del Sistema

- Java Development Kit (JDK) 17 o superior
- MySQL 8.0 o superior
- Maven 3.8 o superior
- 4GB RAM mínimo
- 2GB espacio en disco

---

## 2. Arquitectura del Sistema

### 2.1 Patrón de Capas

El proyecto sigue una **arquitectura de capas (Layered Architecture)**:

```
┌─────────────────────────────────────────────┐
│           CONTROLLER LAYER              │
│  (Recibe requests, devuelve respuestas)    │
├─────────────────────────────────────────────┤
│           SERVICE LAYER                 │
│  (Lógica de negocio)                    │
├─────────────────────────────────────────────┤
│           REPOSITORY LAYER               │
│  (Acceso a base de datos)                │
├─────────────────────────────────────────────┤
│           ENTITY LAYER                  │
│  (Modelos de datos)                     │
└─────────────────────────────────────────────┘
```

### 2.2 Estructura de Paquetes

```
com.transporte/
├── Transport eApplication.java      # Clase principal
├── config/                        # Configuración
│   ├── SecurityConfig.java        # Seguridad Spring
│   └── GlobalExceptionHandler.java # Manejo errores
├── controller/                    # Endpoints REST
│   ├── PersonaController.java
│   ├── UsuarioController.java
│   ├── VehiculoController.java
│   ├── DocumentoController.java
│   └── PublicoController.java
├── service/                      # Lógica de negocio
│   ├── PersonaService.java
│   ├── UsuarioService.java
│   ├── VehiculoService.java
│   └── DocumentoService.java
├── repository/                   # Acceso a datos
│   ├── PersonaRepository.java
│   ├── UsuarioRepository.java
│   ├── VehiculoRepository.java
│   ├── ConductorRepository.java
│   └── DocumentoRepository.java
├── entity/                       # Entidades DB
│   ├── Persona.java
│   ├── Usuario.java
│   ├── Vehiculo.java
│   ├── Conductor.java
│   └── Documento.java
├── dto/                         # Objetos transferencia
│   ├── PersonaRequestDTO.java
│   ├── PersonaResponseDTO.java
│   ├── UsuarioResponseDTO.java
│   ├── VehiculoRequestDTO.java
│   ├── VehiculoResponseDTO.java
│   ├── ConductorRequestDTO.java
│   ├── ConductorResponseDTO.java
│   ├── DocumentoRequestDTO.java
│   ├── DocumentoResponseDTO.java
│   └── (otros DTOs)
├── security/                    # Seguridad
│   ├── JwtUtil.java          # Utilidades JWT
│   ├── JwtAuthenticationFilter.java
│   └── JwtAuthenticationProvider.java
└── util/                      # Utilidades
    └── GeneradorUtil.java    # Generadores password/apiKey
```

---

## 3. Modelo de Datos

### 3.1 Entidad: Persona

Representa a las personas del sistema (conductores o administrativos).

```java
@Entity
@Table(name = "persona")
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String identificacion;    // CC número
    
    @Pattern(regexp = "^CC$")
    private String tipoIdentificacion;  // Solo CC
    
    private String nombres;
    private String apellidos;
    
    @Column(unique = true)
    private String correo;
    
    @Pattern(regexp = "^[CA]$")
    private String tipoPersona;  // C=Conductor, A=Administrativo
}
```

**Campo tipoPersona:**
- `C` = Conductor (maneja vehículos)
- `A` = Administrativo (tiene usuario en el sistema)

### 3.2 Entidad: Usuario

Cuentas de acceso al sistema. Solo las personas tipo "A" pueden tener usuario.

```java
@Entity
@Table(name = "usuario")
@IdClass(Usuario.UsuarioId.class)
public class Usuario {
    @Id
    private String login;       // PK: jcperez + identificacion
    
    @Id
    private Long idPersona;   // FK a Persona
    
    @Column(nullable = false)
    private String password;  // BCrypt encriptado
    
    @Column(unique = true)
    private String apiKey;   // API Key único
}
```

**Generación automática:**
- **Login**: primera letra nombre + primera letra apellido + identificación
  - Ej: "Juan Carlos Pérez" + "123456789" → "jcp123456789"
- **Password**: 16 caracteres aleatorios
- **APIKey**: 32 bytes en Base64URL

### 3.3 Entidad: Vehículo

```java
@Entity
@Table(name = "vehiculo")
public class Vehiculo {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(unique = true)
    private String placa;    // Ej: "ABC123"
    
    private String marca;     // Ej: "Toyota"
    private String modelo;    // Ej: "Hilux"
    private Integer anioFabricacion;
}
```

### 3.4 Entidad: Conductor

Relación muchos-a-muchos entre Persona y Vehículo.

```java
@Entity
@Table(name = "conductor")
public class Conductor {
    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne
    private Persona persona;
    
    @ManyToOne
    private Vehiculo vehiculo;
    
    private LocalDate fechaAsociacion;
    
    @Pattern(regexp = "^(PO|EA|RO)$")
    private String estado;
}
```

**Estados del conductor:**
- `PO` = Pendiente Operativo (habilitado)
- `EA` = En Asignación
- `RO` = Retirado Operativo

### 3.5 Entidad: Documento

Documentos PDF asociados a vehículos.

```java
@Entity
@Table(name = "documento")
public class Documento {
    @Id
    private Long id;
    
    private String tipoDocumento;     // SOAT, TÉCNICA, etc
    private String numeroDocumento;
    private LocalDate fechaVencimiento;
    
    @Lob
    private byte[] archivo;        // BLOB - PDF en Base64
}
```

### 3.6 Diagrama de Relaciones

```
┌──────────────┐       ┌──────────────┐
│   Persona   │       │   Usuario   │
│  (id, tipo) │���─ 1:1 ─│ (login,   │
└─────────────┘       │  password) │
       ▲             └────────────┘
       │
    1:N (conductor)
       │
       ▼
┌──────────────┐       ┌──────────────┐
│  Conductor  │── N:1 ─│  Vehiculo   │
│  (estado)  │       │ (placa)    │
└────────────┘       └──────────────┘
                           │
                          1:N
                           ▼
                    ┌──────────────┐
                    │  Documento  │
                    │ (BLOB PDF) │
                    └──────────────┘
```

---

## 4. Endpoints de la API

### 4.1 Endpoints PÚBLICOS (sin autenticación)

Estos endpoints permiten consultas públicas sin necesidad de token.

| # | Método | Endpoint | Descripción | Parámetros |
|-----|--------|----------|------------|------------|
| 1 | GET | `/api/publico/vehiculos/documentos-vencidos` | Lista vehículos con docs vencidos | - |
| 2 | GET | `/api/publico/conductores/habilitados` | Conductores estado "PO" | - |
| 3 | GET | `/api/publico/vehiculos/{placa}` | Consulta vehículo por placa | Path: placa |
| 4 | GET | `/api/publico/vehiculos/documentos-por-vencer` | Docs próximos a vencer | `dias` (query, default 30) |
| 5 | GET | `/api/publico/personas/estadisticas` | Total personas por tipo | - |

**Ejemplo de respuesta - estadisticas:**
```json
[
  { "tipoPersona": "C", "total": 15 },
  { "tipoPersona": "A", "total": 3 }
]
```

### 4.2 Endpoints PROTEGIDOS (requieren JWT + APIKey)

#### Personas

| # | Método | Endpoint | Descripción |
|-----|--------|----------|-------------|
| 1 | POST | `/api/personas` | Crear persona |
| 2 | GET | `/api/personas` | Listar todas |
| 3 | GET | `/api/personas/{id}` | Obtener por ID |
| 4 | PUT | `/api/personas/{id}` | Actualizar |

**Request - Crear Persona:**
```json
{
  "identificacion": "123456789",
  "tipoIdentificacion": "CC",
  "nombres": "Juan Carlos",
  "apellidos": "Pérez Gómez",
  "correo": "juan.perez@empresa.com",
  "tipoPersona": "A"
}
```

**Nota:** Si tipoPersona = "A", se crea automáticamente un usuario.

#### Usuarios

| # | Método | Endpoint | Descripción |
|-----|--------|----------|-------------|
| 1 | PUT | `/api/usuarios/{login}/password` | Cambiar password |
| 2 | GET | `/api/usuarios/{login}/apikey` | Regenerar APIKey |

**Request - Cambiar Password:**
```json
{
  "nuevaPassword": "NuevaPass123!"
}
```

#### Vehículos

| # | Método | Endpoint | Descripción |
|-----|--------|----------|-------------|
| 1 | POST | `/api/vehiculos` | Crear vehículo |
| 2 | GET | `/api/vehiculos` | Listar vehículos |
| 3 | GET | `/api/vehiculos/{id}` | Obtener por ID |
| 4 | GET | `/api/vehiculos/placa/{placa}` | Obtener por placa |

**Request - Crear Vehículo:**
```json
{
  "placa": "ABC123",
  "marca": "Toyota",
  "modelo": "Hilux",
  "anioFabricacion": 2022
}
```

#### Conductores

| # | Método | Endpoint | Descripción |
|-----|--------|----------|-------------|
| 1 | POST | `/api/vehiculos/conductores` | Asociar conductor |
| 2 | PUT | `/api/vehiculos/conductores/{id}/estado` | Cambiar estado |
| 3 | GET | `/api/vehiculos/conductores/habilitados` | Listar habilitados |

**Request - Asociar Conductor:**
```json
{
  "idPersona": 2,
  "idVehiculo": 1
}
```

**Request - Cambiar Estado:**
```json
{
  "estado": "PO"
}
```

#### Documentos

| # | Método | Endpoint | Descripción |
|-----|--------|----------|-------------|
| 1 | POST | `/api/documentos` | Subir documentos |
| 2 | GET | `/api/documentos/{id}` | Obtener documento |

**Request - Subir Documento:**
```json
{
  "idVehiculo": 1,
  "documentos": [
    {
      "tipoDocumento": "SOAT",
      "numeroDocumento": "SOAT-2024-001",
      "fechaVencimiento": "2025-12-31",
      "archivoBase64": "JVBERi0xLjQK..."
    }
  ]
}
```

---

## 5. Seguridad y Autenticación

### 5.1 Esquema de Seguridad

El sistema usa un esquema de **doble autenticación**:

1. **JWT (JSON Web Token)** - Header `Authorization`
2. **API Key** - Header `X-API-Key`

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
X-API-Key: dGhpc2lzYXZlcnlsb25nYXBp... 
```

### 5.2 Cómo Funciona

```
┌─────────────┐     ┌──────────────┐     ┌─────────────────┐
│   Cliente   │────▶│ JWT Filter   │────▶│ Validar Token    │
└─────────────┘     └──────────────┘     └─────────────────┘
                           │                        │
                           ▼                        │
                    ┌──────────────┐              │ SI
                    │APIKey Filter │──────────────┘
                    └──────────────┘
                           │
                           ▼
                    ┌──────────────┐
                    │  Controlador │
                    └──────────────┘
```

### 5.3 Generar Token JWT

El proyecto no incluye endpoint de login. Para desarrollo:

```java
// JwtUtil.generateToken("login")
String token = jwtUtil.generateToken("jcp123456789");
// Output: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqY3AxMjM0NTY3ODki...
```

### 5.4 Reglas de Seguridad

| Endpoint | Requiere Auth |
|----------|---------------|
| `/api/publico/*` | ❌ No |
| `/api/personas/*` | ✅ Sí |
| `/api/usuarios/*` | ✅ Sí |
| `/api/vehiculos/*` | ✅ Sí |
| `/api/documentos/*` | ✅ Sí |

### 5.5 Configuración de SecurityConfig

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/publico/**").permitAll()
    .requestMatchers("/api/personas/**").authenticated()
    .requestMatchers("/api/usuarios/**").authenticated()
    .requestMatchers("/api/vehiculos/**").authenticated()
    .requestMatchers("/api/documentos/**").authenticated()
    .anyRequest().authenticated()
)
```

---

## 6. Configuración

### 6.1 application.properties

```properties
# ============================================
# APLICACIÓN
# ============================================
spring.application.name=transporte-backend
server.port=8080

# ============================================
# BASE DE DATOS
# ============================================
spring.datasource.url=jdbc:mysql://localhost:3306/transporte_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ============================================
# JPA / HIBERNATE
# ============================================
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# ============================================
# JWT
# ============================================
jwt.secret=Y2FtYXNldHJhbnNwb3J0ZWJhc2VlbmNyeXB0b2tleXNlY3JldGhpcyRmMWMyZjNhZi1lMjM0LTQ0ODMtODFmYy1mNjc3ODkwMTIzNDMyZTI
jwt.expiration=86400000

# ============================================
# LOGGING
# ============================================
logging.level.org.springframework.security=INFO
logging.level.com.transporte=DEBUG
```

### 6.2 Parámetros MySQL

| Parámetro | Valor por defecto | Descripción |
|----------|---------------|-------------|
| `spring.datasource.url` | jdbc:mysql://localhost:3306/transporte_db | URL de conexión |
| `spring.datasource.username` | root | Usuario MySQL |
| `spring.datasource.password` | root | Password MySQL |

**Importante:** Modificar según tu configuración local.

### 6.3 Generación de Tablas

Las tablas se crean automáticamente con JPA (`ddl-auto=update`).

No necesitas ejecutar scripts SQL. JPA crea:
- Tabla `persona`
- Tabla `usuario` (con clave compuesta)
- Tabla `vehiculo`
- Tabla `conductor`
- Tabla `documento`

---

## 7. Ejecución del Proyecto

### 7.1 Requisitos Previos

```bash
# Verificar Java
java -version
# Debe mostrar: java 17 o superior

# Verificar Maven
mvn -version
# Debe mostrar: Maven 3.8 o superior

# Verificar MySQL
mysql --version
# Debe mostrar: MySQL 8.0 o superior
```

### 7.2 Compilar

```bash
cd /ruta/del/proyecto
mvn clean install
```

**Salida esperada:**
```
[INFO] BUILD SUCCESS
[INFO] --------------------------------------------
[INFO] BUILD SUCCESS
[INFO] --------------------------------------------
```

### 7.3 Ejecutar

```bash
mvn spring-boot:run
```

**Salida esperada:**
```
Tomcat started on port: 8080
Started TransporteApplication in X.XXX seconds
```

### 7.4 Verificar

```bash
# Endpoint público
curl http://localhost:8080/api/publico/personas/estadisticas

# Debe devolver:
# [{"tipoPersona":"C","total":0},{"tipoPersona":"A","total":0}]
```

---

## 8. Ejemplos Completos de Uso

### 8.1 Flujo Completo: Crear Admin, Conductor, Vehículo y Documento

#### Paso 1: Crear Persona Administrativa

```bash
curl -X POST http://localhost:8080/api/personas \
  -H "Content-Type: application/json" \
  -d '{
    "identificacion": "123456789",
    "tipoIdentificacion": "CC",
    "nombres": "Juan Carlos",
    "apellidos": "Pérez Gómez",
    "correo": "juan@empresa.com",
    "tipoPersona": "A"
  }'
```

**Respuesta:**
```json
{
  "id": 1,
  "identificacion": "123456789",
  "tipoIdentificacion": "CC",
  "nombres": "Juan Carlos",
  "apellidos": "Pérez Gómez",
  "correo": "juan@empresa.com",
  "tipoPersona": "A",
  "login": "jcp123456789"
}
```

Se creó automáticamente el usuario con:
- **Login:** `jcp123456789`
- **Password:** generada automáticamente (16 caracteres)
- **APIKey:** generada automáticamente

#### Paso 2: Crear Conductor

```bash
curl -X POST http://localhost:8080/api/personas \
  -H "Content-Type: application/json" \
  -d '{
    "identificacion": "987654321",
    "tipoIdentificacion": "CC",
    "nombres": "Pedro",
    "apellidos": "López",
    "correo": "pedro@empresa.com",
    "tipoPersona": "C"
  }'
```

**Respuesta:**
```json
{
  "id": 2,
  "identificacion": "987654321",
  "tipoIdentificacion": "CC",
  "nombres": "Pedro",
  "apellidos": "López",
  "correo": "pedro@empresa.com",
  "tipoPersona": "C",
  "login": null
}
```

#### Paso 3: Crear Vehículo

```bash
curl -X POST http://localhost:8080/api/vehiculos \
  -H "Content-Type: application/json" \
  -d '{
    "placa": "ABC123",
    "marca": "Toyota",
    "modelo": "Hilux",
    "anioFabricacion": 2022
  }'
```

**Respuesta:**
```json
{
  "id": 1,
  "placa": "ABC123",
  "marca": "Toyota",
  "modelo": "Hilux",
  "anioFabricacion": 2022,
  "conductores": [],
  "documentos": []
}
```

#### Paso 4: Asociar Conductor a Vehículo

```bash
curl -X POST http://localhost:8080/api/vehiculos/conductores \
  -H "Content-Type: application/json" \
  -d '{
    "idPersona": 2,
    "idVehiculo": 1
  }'
```

**Respuesta:**
```json
{
  "id": 1,
  "idPersona": 2,
  "nombresPersona": "Pedro",
  "apellidosPersona": "López",
  "idVehiculo": 1,
  "placaVehiculo": "ABC123",
  "fechaAsociacion": "2024-01-15",
  "estado": "PO"
}
```

#### Paso 5: Subir Documento (PDF)

```bash
# Convertir PDF a Base64 primero:
# Linux: base64 -w 0 archivo.pdf
# Windows: [Convertir online a Base64]

curl -X POST http://localhost:8080/api/documentos \
  -H "Content-Type: application/json" \
  -d '{
    "idVehiculo": 1,
    "documentos": [
      {
        "tipoDocumento": "SOAT",
        "numeroDocumento": "SOAT-2024-001",
        "fechaVencimiento": "2025-12-31",
        "archivoBase64": "JVBERi0x..."
      }
    ]
  }'
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "tipoDocumento": "SOAT",
    "numeroDocumento": "SOAT-2024-001",
    "fechaVencimiento": "2025-12-31",
    "idVehiculo": 1,
    "placaVehiculo": "ABC123",
    "archivoBase64": "JVBERi0x..."
  }
]
```

### 8.2 Consultas Públicas

#### Ver vehículos con documentos vencidos

```bash
curl http://localhost:8080/api/publico/vehiculos/documentos-vencidos
```

#### Ver conductores habilitados

```bash
curl http://localhost:8080/api/publico/conductores/habilitados
```

#### Consultar vehículo por placa

```bash
curl http://localhost:8080/api/publico/vehiculos/ABC123
```

#### Ver vehículos con documentos por vencer (próximos 30 días)

```bash
curl "http://localhost:8080/api/publico/vehiculos/documentos-por-vencer?dias=30"
```

#### Ver estadísticas de personas

```bash
curl http://localhost:8080/api/publico/personas/estadisticas
```

---

## 9. Manejo de Errores

### 9.1 GlobalExceptionHandler

El proyecto incluye un manejador global de excepciones que retorna errores estandarizados.

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Persona no encontrada"
}
```

### 9.2 Códigos de Estado HTTP

| Código | Significado | Uso típico |
|--------|------------|------------|
| 200 | OK | GET exitoso, PUT exitoso |
| 201 | Created | POST exitoso |
| 400 | Bad Request | Validación fallida |
| 401 | Unauthorized | Token inválido |
| 404 | Not Found | Recurso no existe |
| 500 | Internal Server Error | Error inesperado |

### 9.3 Errores Comunes

| Error | Causa | Solución |
|-------|------|---------|
| 400: Validation Failed | Datos inválidos | Revisar JSON-request |
| 400: Ya existe... | Registro duplicado | Usar identificador diferente |
| 404: Entidad no encontrada | ID inexistente | Verificar ID |
| 401: Unauthorized | Token o APIKey inválidos | Incluir headers correctos |

---

## 10. Dependencias del POM

```xml
<!-- Spring Boot -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- MySQL -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

---

## 11. Estructura de Archivos del Proyecto

```
transporte-backend/
├── pom.xml                                    # Maven config
├── README.md                                 # Este documento
├── SCRIPTS_POSTMAN.txt                       # Queries Postman
└── src/
    ├── main/
    │   ├── java/com/transporte/
    │   │   ├── TransporteApplication.java  # Clase principal
    │   │   ├── config/
    │   │   │   ├── SecurityConfig.java
    │   │   │   └── GlobalExceptionHandler.java
    │   │   ├── controller/
    │   │   │   ├── PersonaController.java
    │   │   │   ├── UsuarioController.java
    │   │   │   ├── VehiculoController.java
    │   │   │   ├── DocumentoController.java
    │   │   │   └── PublicoController.java
    │   │   ├── service/
    │   │   │   ├── PersonaService.java
    │   │   │   ├── UsuarioService.java
    │   │   │   ├── VehiculoService.java
    │   │   │   └── DocumentoService.java
    │   │   ├── repository/
    │   │   │   ├── PersonaRepository.java
    │   │   │   ├── UsuarioRepository.java
    │   │   │   ├── VehiculoRepository.java
    │   │   │   ├── ConductorRepository.java
    │   │   │   └── DocumentoRepository.java
    │   │   ├── entity/
    │   │   │   ├── Persona.java
    │   │   │   ├── Usuario.java
    │   │   │   ├── Vehiculo.java
    │   │   │   ├── Conductor.java
    │   │   │   └── Documento.java
    │   │   ├── dto/
    │   │   │   ├── PersonaRequestDTO.java
    │   │   │   ├── PersonaResponseDTO.java
    │   │   │   ├── UsuarioResponseDTO.java
    │   │   │   ├── VehiculoRequestDTO.java
    │   │   │   ├── VehiculoResponseDTO.java
    │   │   │   ├── ConductorRequestDTO.java
    │   │   │   ├── ConductorResponseDTO.java
    │   │   │   ├── DocumentoRequestDTO.java
    │   │   │   ├── DocumentoResponseDTO.java
    │   │   │   ├── PasswordChangeDTO.java
    │   │   │   ├── ConductorEstadoDTO.java
    │   │   │   ├── DocumentoListaRequestDTO.java
    │   │   │   ├── EstadisticaPersonaDTO.java
    │   │   │   └── AuthResponseDTO.java
    │   │   ├── security/
    │   │   │   ├── JwtUtil.java
    │   │   │   ├── JwtAuthenticationFilter.java
    │   │   │   └── JwtAuthenticationProvider.java
    │   │   └── util/
    │   │       └── GeneradorUtil.java
    │   └── resources/
    │       └── application.properties
    └── test/
        └── (pruebas unitarias - no incluidas)
```

---

## 12. Buenas Prácticas Utilizadas

### 12.1 En el Código

- ✅ **Separación de capas**: Controller → Service → Repository
- ✅ **DTOs**: No se exponen entidades directamente
- ✅ **Validaciones**: Anotaciones @Valid y @Pattern
- ✅ **Excepciones**: GlobalExceptionHandler
- ✅ **Lombok**: Reduce código repetitivo
- ✅ **Inyección dependencias**: Constructor (final + @RequiredArgsConstructor)

### 12.2 Seguridad

- ✅ **Passwords encriptados**: BCrypt
- ✅ **Tokens JWT**: Validados en cada request
- ✅ **APIKeys**: Validadas junto con JWT
- ✅ **Endpoints públicos**: Separados de los protegidos

### 12.3 Base de Datos

- ✅ **Relaciones**: Foreign keys 管理
- ✅ **Clave compuesta**: Usuario.login + Usuario.idPersona
- ✅ **Índices**: unique, foreign keys
- ✅ **JPA**: Abstrae SQL

---

## 13. Limitaciones y Trabajo Futuro

### 13.1 Limitaciones Actuales

- ❌ No incluye endpoint de Login (generar JWT manualmente)
- ❌ No incluye paginación
- ❌ No incluye filtros avanzados
- ❌ No incluye logging a archivo
- ❌ Documentación en inglés

### 13.2 Mejoras Sugeridas

- [ ] Agregar endpoint `/api/auth/login`
- [ ] Implementar paginación con Page
- [ ] Agregar filtros por fecha, estado
- [ ] Logging con Logback
- [ ] Documentación OpenAPI/Swagger
- [ ] Tests unitarios con JUnit
- [ ] Docker-compose para MySQL

---

## 14. Preguntas Frecuentes (FAQ)

### P: ¿Cómo obtengo el token JWT?
R: Para desarrollo, genera uno con `JwtUtil.generateToken("login")`. Para producción, implementa un endpoint de login.

### P: ¿Dónde veo el password del usuario creado?
R: El password se genera automáticamente pero no se almacena. Para recuperar, usa el endpoint de cambio de password.

### P: ¿Cómo cambio el password?
R: `PUT /api/usuarios/{login}/password` con el nuevo password.

### P: ¿Cómo regenero la APIKey?
R: `GET /api/usuarios/{login}/apikey` devuelve una nueva APIKey.

### P: ¿Puedo tener más de un conductor por vehículo?
R: Sí, es relación many-to-many. Un vehículo puede tener muchos conductores.

### P: ¿Puedo tener más de un vehículo por conductor?
R: Sí, un conductor puede conducir múltiples vehículos en diferente fechas.

### P: ¿Dónde se almacenan los PDF?
R: En la base de datos como BLOB, codificados en Base64 en el JSON.

### P: ¿Qué pasa si elimino una persona conductor?
R: Se eliminan también sus registros en conductor (CASCADE).

---

## 15. Contacto y Soporte

Para dudas o problemas:
1. Revisar los logs con `logging.level.com.transporte=DEBUG`
2. Verificar conexión MySQL
3. Verificar usuario/password MySQL en application.properties

---

## 16. Licencia

Este proyecto es de uso educativo y demostración.

---

**Documento creado:** Enero 2024  
**Versión:** 1.0.0  
**Proyecto:** transporte-backend v1.0.0