<div align="center">
  <img src="https://res.cloudinary.com/dpabol1z3/image/upload/v1730087313/xgry4fffxin2tcgsf8na.png" width="200" height="200" alt="Logo del Proyecto"/>

# Comunicación Asíncrona de Dos Microservicios

  <p align="center">
    <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java" alt="Java 17"/>
    <img src="https://img.shields.io/badge/Spring%20Boot-Latest-green?style=for-the-badge&logo=spring" alt="Spring Boot"/>
    <img src="https://img.shields.io/badge/MySQL-Latest-blue?style=for-the-badge&logo=mysql" alt="MySQL"/>
  </p>
</div>


## 📋 Descripción

Este proyecto implementa una arquitectura de microservicios utilizando Java 17 y Spring Boot. La estructura del proyecto sigue un patrón organizado con las siguientes capas:

- 📁 API
- ⚙️ Config
- 🎮 Controller
- 📊 DTO
- ❗ Exception
- 📑 Model
- 📦 Repository
- 🔒 Security
- 🛠️ Service
- ## 📦 Dependencias Principales

El proyecto utiliza las siguientes dependencias clave:

### Spring Boot Stack
- **Spring Boot Starter Data JPA**: Para la persistencia de datos
- **Spring Boot Starter Validation**: Para validación de datos
- **Spring Boot Starter Web**: Para crear aplicaciones web
- **Spring Boot DevTools**: Para desarrollo más ágil
- **Spring Security Crypto**: Para funcionalidades de seguridad

### Base de Datos
- **MySQL Connector**: Driver para conexión con MySQL
- **Flyway Core**: Para control de versiones de la base de datos

### Utilidades
- **ModelMapper (3.1.0)**: Para mapeo de objetos
- **Lombok**: Para reducir código boilerplate
- **RxJava (3.1.8)**: Para programación reactiva
- **DataFaker (1.8.1)**: Para generación de datos de prueba

### Documentación
- **SpringDoc OpenAPI UI (2.5.0)**: Para documentación de API con Swagger

### Testing
- **Spring Boot Starter Test**: Para pruebas unitarias e integración

## 🛠️ Versiones
- Java 17
- Spring Boot 3.3.5





## 🏗️ Arquitectura del Sistema

<div align="center">
  <img src="https://res.cloudinary.com/dpabol1z3/image/upload/v1730086819/d2l5gkwoxjvmvnyst2su.jpg" alt="Diagrama de Arquitectura" width="800"/>
</div>



## 🔧 Variables de Entorno


Para ejecutar este proyecto, necesitarás configurar las siguientes variables de entorno:

```env
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
```

## 🐳 Despliegue con Docker

Para ejecutar la aplicación usando Docker, sigue estos pasos:

1. Asegúrate de tener Docker y Docker Compose instalados
2. Ejecuta el siguiente comando en la raíz del proyecto:

```bash
docker-compose up --build
