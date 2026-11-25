# Sistema de Gestión de Pedidos e Inventario - Backend

Este repositorio contiene la API RESTful y la lógica de negocio del **Sistema de Gestión de Pedidos e Inventario**.

## 🛠️ Tecnologías Utilizadas

* **Java 21**
* **Spring Boot 4.x / 3.x**
* **Spring Data JPA & Hibernate**
* **Spring Security con JWT**
* **MySQL Database**
* **Lombok & JUnit 5 / Mockito**

##  Requisitos Previos

Antes de clonar y ejecutar este proyecto, asegúrate de tener instalado en tu máquina:
1. **Java JDK 21** (o superior).
2. **MySQL Server** (Versión 8.0 o superior) y un cliente como MySQL Workbench o DBeaver.
3. *(Opcional)* Maven, aunque el proyecto ya incluye el wrapper (`mvnw`).

##  Configuración de la Base de Datos (Importante para nuevas laptops)

El código fuente no incluye la base de datos. Para que el proyecto funcione en una computadora nueva, sigue estos pasos:

1. Abre tu cliente de MySQL (Workbench, DBeaver, etc.).
2. Crea una base de datos vacía. Por ejemplo:
   ```sql
   CREATE DATABASE gestion_pedidos;
3. Abre el archivo src/main/resources/application.properties del proyecto.

4. Modifica las credenciales para que apunten a tu base de datos local. El archivo ya viene configurado así por defecto, pero debes asegurarte de que coincida con tu instalación de MySQL:

    spring.datasource.url=jdbc:mysql://localhost:3306/gestion_pedidos?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true
    spring.datasource.username=root
    spring.datasource.password=root
        (Nota: Gracias a la propiedad createDatabaseIfNotExist=true y a Hibernate con ddl-auto=update, si la base de datos no existe, Spring Boot la creará automáticamente y generará las tablas al iniciar).

Compilación y Ejecución
Nota para usuarios de Windows: Los comandos a continuación usan ./mvnw. Si estás en la consola de Windows (CMD o PowerShell) y te da error, usa mvnw.cmd en su lugar (ej: mvnw.cmd spring-boot:run).

1. Compilar y limpiar el proyecto
Genera las clases correctas y limpia binarios antiguos:
./mvnw clean compile

2. Ejecutar la aplicación
Inicializa el servidor backend en el puerto local:
./mvnw spring-boot:run

Acceso local: http://localhost:8080

3. Ejecutar Pruebas Unitarias
Ejecuta la suite de pruebas unitarias con JUnit y Mockito:
./mvnw clean test