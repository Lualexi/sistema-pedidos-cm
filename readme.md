# Sistema de Gestión de Pedidos e Inventario

## Tecnologías Utilizadas
Java 21, Spring Boot

## Pruebas Unitarias (TDD)
./mvnw clean test
./mvnw test

## Inicializar el Proyecto (http://localhost:8080)
./mvnw spring-boot:run

## Generar las clases correctas para el test
./mvnw clean compile

## Usuario y contraseña
Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"username":"admin","password":"123456","role":"ADMIN_ALMACEN"}'