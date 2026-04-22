# ☁️ Guia de Despliegue en Domenicos

## alternativagratis a Render (sin tarjeta)

1. Ir a: **https://domenicos.com**
2. Registrarse con GitHub
3. New Project → Connect GitHub
4. Seleccionar: `victorcelemin/transporte-backend`

## Config:
- Build Command: `mvn clean package -DskipTests`
- Start Command: `java -jar target/transporte-backend-1.0.0.jar`
- Port: `8080`

## Variables de Entorno:
- DB_URL: tu_mysql_url
- DB_USER: usuario
- DB_PASSWORD: password

## MySQL:
Domenicos ofrece MySQL gratuito también.