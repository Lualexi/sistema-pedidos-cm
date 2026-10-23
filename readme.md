# Sistema de Gestión de Pedidos e Inventario
## Tecnologías Utilizadas
* **Java 21**
* **Spring Boot 4.x / 3.x**
* **Spring Data JPA & Hibernate**
* **Spring Security con JWT**
* **MySQL Database**
* **Lombok & JUnit 5 / Mockito**


## 1. Compilar y limpiar el proyecto
Genera las clases correctas y limpia binarios antiguos:
./mvnw clean compile

## 2. Ejecutar la aplicación
Inicializa el servidor backend en el puerto local:
./mvnw spring-boot:run
Acceso: http://localhost:8080

## 3. Ejecutar Pruebas Unitarias
Ejecuta la suite de pruebas unitarias con JUnit y Mockito:
./mvnw clean test

### Endpoints y Registro Inicial
### Registro de Administrador de Almacén (ADMIN_ALMACEN):
Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"username":"admin","password":"123456","role":"ADMIN_ALMACEN"}'

### Autenticación y Obtención de Token (Login):
Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"username":"admin","password":"123456"}'
