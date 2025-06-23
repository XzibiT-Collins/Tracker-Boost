# **Project Tracker** 🚀
*A comprehensive RESTful API service for tracking projects, developers, and tasks with comprehensive audit logging*

![Java](https://img.shields.io/badge/Java-24-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0.33-blue)
![MongoDB](https://img.shields.io/badge/MongoDB-7.0-green)
![Docker](https://img.shields.io/badge/Docker-✓-lightblue)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-orange)

---

## **📋 Table of Contents**
- [Architecture Overview](#-architecture-overview)
- [Tech Stack](#-tech-stack)
- [Features](#-features)
- [API Endpoints](#-api-endpoints)
- [Getting Started](#-getting-started)
- [Configuration](#-configuration)
- [API Documentation](#-api-documentation)


---

## **🏗 Architecture Overview**

This application follows a layered architecture pattern with dual-database strategy for optimal performance and data separation.

### **System Architecture**
```mermaid
graph TB
    %% Client Layer
    Client[REST API Clients<br/>Web, Mobile, External Systems]
    
    %% Spring Boot Application Container
    subgraph SpringBoot["🚀 Spring Boot Application Container"]
        %% Controller Layer
        subgraph Controllers["🎯 Controller Layer"]
            PC[ProjectController]
            TC[TaskController]
            DC[DeveloperController]
            AC[AuditLogController]
        end
        
        %% Service Layer
        subgraph Services["⚙️ Service Layer"]
            PS[ProjectServiceImpl]
            TS[TaskServiceImpl]
            DS[DeveloperServiceImpl]
            AS[AuditLogServiceImpl]
        end
        
        %% Repository Layer
        subgraph Repositories["🗃️ Repository Layer"]
            PR[ProjectRepository]
            TR[TaskRepository]
            DR[DeveloperRepository]
            AR[AuditLogRepository]
        end
        
        %% Model Layer
        subgraph Models["📋 Model Layer"]
            Entities[Project, Task, Developer Entities]
            AuditEntity[AuditLog Entity]
            DTOs[Request/Response DTOs]
            Enums[Sorting & Status Enums]
        end
        
        %% Exception Handling
        subgraph Exceptions["⚠️ Exception Handling"]
            GEH[GlobalExceptionHandler]
            CE[Custom Exceptions]
        end
    end
    
    %% Database Layer
    MySQL[(🗄️ MySQL Database<br/>Projects, Tasks, Developers<br/>Spring Data JPA + Hibernate)]
    MongoDB[(🍃 MongoDB Database<br/>Audit Logs<br/>Spring Data MongoDB)]
    
    %% Connections
    Client --> Controllers
    
    %% Controller to Service connections
    PC --> PS
    TC --> TS
    DC --> DS
    AC --> AS
    
    %% Service to Repository connections
    PS --> PR
    TS --> TR
    DS --> DR
    AS --> AR
    
    %% Repository to Model connections
    PR --> Entities
    TR --> Entities
    DR --> Entities
    AR --> AuditEntity
    
    %% Repository to Database connections
    PR --> MySQL
    TR --> MySQL
    DR --> MySQL
    AR --> MongoDB
    
    %% DTOs flow
    Controllers --> DTOs
    Services --> DTOs
    
    %% Exception handling
    Controllers --> Exceptions
    Services --> Exceptions
    
    %% Styling
    classDef clientStyle fill:#3498db,stroke:#2980b9,stroke-width:2px,color:#fff
    classDef controllerStyle fill:#e74c3c,stroke:#c0392b,stroke-width:2px,color:#fff
    classDef serviceStyle fill:#f39c12,stroke:#e67e22,stroke-width:2px,color:#fff
    classDef repositoryStyle fill:#27ae60,stroke:#229954,stroke-width:2px,color:#fff
    classDef modelStyle fill:#9b59b6,stroke:#8e44ad,stroke-width:2px,color:#fff
    classDef databaseStyle fill:#34495e,stroke:#2c3e50,stroke-width:2px,color:#fff
    classDef mongoStyle fill:#16a085,stroke:#138d75,stroke-width:2px,color:#fff
    classDef exceptionStyle fill:#95a5a6,stroke:#7f8c8d,stroke-width:2px,color:#fff
    classDef containerStyle fill:#ecf0f1,stroke:#bdc3c7,stroke-width:2px
    
    class Client clientStyle
    class PC,TC,DC,AC controllerStyle
    class PS,TS,DS,AS serviceStyle
    class PR,TR,DR,AR repositoryStyle
    class Entities,AuditEntity,DTOs,Enums modelStyle
    class MySQL databaseStyle
    class MongoDB mongoStyle
    class GEH,CE exceptionStyle
    class SpringBoot containerStyle
```

---

## **🛠 Tech Stack**

| Component                   | Technology                    | Purpose                           |
|-----------------------------|-------------------------------|-----------------------------------|
| **Runtime**                 | Java JDK 24 (Alpine)          | Application runtime environment   |
| **Framework**               | Spring Boot 3.4.5             | Core application framework        |
| **ORM**                     | Spring Data JPA + Hibernate   | Relational database operations    |
| **NoSQL Integration**       | Spring Data MongoDB           | Document database operations      |
| **Relational Database**     | MySQL 8.0.33                 | Primary data persistence          |
| **Document Database**       | MongoDB 7.0                   | Audit logs and analytics          |
| **Caching**                 | Spring Boot In-Memory Cache   | Performance optimization          |
| **API Documentation**       | Swagger/OpenAPI 3             | Interactive API documentation     |
| **Containerization**        | Docker + Docker Compose       | Deployment and orchestration      |
| **Build Tool**              | Maven                         | Dependency management & building  |

---

## **✨ Features**

### **Core Functionality**
- 🏗 **Project Management**: Create, update, delete, and track projects with status monitoring
- 👥 **Developer Management**: Manage developers and task assignments
- 📋 **Task Management**: Create and track tasks with due dates
- 📊 **Audit Logging**: Comprehensive activity tracking stored in MongoDB
- 🔍 **Advanced Filtering**: Sort and filter data across multiple criteria
- ⚡ **Performance Optimized**: In-memory caching for frequently accessed data

### **Data Management**
- **Polyglot Persistence**: MySQL for transactional data, MongoDB for audit logs
- **Exception Handling**: Global error handling with custom exceptions
- **Data Validation**: Request/Response DTOs with validation
- **Sorting & Filtering**: Flexible data retrieval options

---

## **🔌 API Endpoints**

### **📁 Project Endpoints**
| Method   | Endpoint                        | Description                    | Parameters       |
|----------|---------------------------------|--------------------------------|------------------|
| `POST`   | `/api/v1/projects/create`       | Create new project             | Request body     |
| `PUT`    | `/api/v1/projects/update/{id}`  | Update existing project        | `id` (path)      |
| `DELETE` | `/api/v1/projects/delete/{id}`  | Delete project                 | `id` (path)      |
| `GET`    | `/api/v1/projects`              | Get all projects               | `sortBy` (query) |
| `GET`    | `/api/v1/projects/withoutTasks` | Get all projects without tasks | `sortBy` (query) |
| `GET`    | `/api/v1/projects/{id}`         | Get project by ID              | `id` (path)      |

**Sorting Options:**
- `SORT_BY_ID` (Default)
- `SORT_BY_NAME`
- `SORT_BY_DEADLINE`
- `SORT_BY_STATUS`

### **👨‍💻 Developer Endpoints**
| Method   | Endpoint                            | Description                         | Parameters       |
|----------|-------------------------------------|-------------------------------------|------------------|
| `POST`   | `/api/v1/developers/create`         | Create new users                | Request body     |
| `PUT`    | `/api/v1/developers/update/{id}`    | Update existing users           | `id` (path)      |
| `DELETE` | `/api/v1/developers/delete/{id}`    | Delete users                    | `id` (path)      |
| `GET`    | `/api/v1/developers`                | Get all developers                  | `sortBy` (query) |
| `GET`    | `/api/v1/developers/{id}`           | Get users by ID                 | `id` (path)      |
| `GET`    | `/api/v1/developers/top5Developers` | Get top 5 users with most tasks | `none`           |

**Sorting Options:**
- `SORT_BY_ID` (Default)
- `SORT_BY_NAME`

### **📋 Task Endpoints**
| Method   | Endpoint                     | Description              | Parameters       |
|----------|------------------------------|--------------------------|------------------|
| `POST`   | `/api/v1/tasks/create`       | Create new task          | Request body     |
| `PUT`    | `/api/v1/tasks/update/{id}`  | Update existing task     | `id` (path)      |
| `DELETE` | `/api/v1/tasks/delete/{id}`  | Delete task              | `id` (path)      |
| `GET`    | `/api/v1/tasks`              | Get all tasks            | `sortBy` (query) |
| `GET`    | `/api/v1/tasks/overdueTasks` | Get overdue tasks        | `sortBy` (query) |
| `GET`    | `/api/v1/tasks/{id}`         | Get task by ID           | `id` (path)      |
| `GET`    | `/api/v1/tasks/statusCount`  | Get task count by status | `none`           |

**Sorting Options:**
- `SORT_BY_TITLE` (Default)
- `SORT_BY_DUE_DATE`
- `SORT_BY_STATUS`

### **📊 Audit Log Endpoints**
| Method | Endpoint | Description | Parameters |
|--------|----------|-------------|------------|
| `GET` | `/api/v1/audit-logs` | Get audit logs | `sortBy` (query) |

**Sorting Options:**
- `SORT_BY_TIMESTAMP` (Default)
- `SORT_BY_ID`
- `SORT_BY_ACTION_TYPE`
- `SORT_BY_ENTITY_TYPE`
- `SORT_BY_ACTOR_NAME`
---

## **🚀 Getting Started**

### **Prerequisites**
- Docker and Docker Compose installed
- Java 24 (if running locally)
- Maven 3.9.9 (if building locally)

### **Quick Start with Docker**

1. **Clone the repository**
   ```bash
   git clone https://github.com/XzibiT-Collins/Project-Tracker.git
   cd project-tracker
   ```

2. **Build and run containers**
   ```bash
   docker compose up --build
   ```

3. **Access the application**
    - API Base URL: `http://localhost:8080`
    - Swagger UI: `http://localhost:8080/swagger-ui/index.html`

### **Individual Docker Commands**

```bash
# Build Docker image
docker compose build

# Start containers
docker compose up

# Stop containers
docker compose down
```

---

## **⚙️ Configuration**

### **Environment Variables**

| Variable | Description | Default Value |
|----------|-------------|---------------|
| `MYSQL_DATABASE_NAME` | MySQL database name | `project_db`  |
| `MYSQL_USERNAME` | MySQL username | `root`        |
| `MYSQL_ROOT_PASSWORD` | MySQL root password | `password`    |
| `MONGO_DATABASE_NAME` | MongoDB database name | `audit_db`    |
| `MONGO_DB_USERNAME` | MongoDB username | `root`        |
| `MONGO_DB_PASSWORD` | MongoDB password | `password`    |


### **Example .env File**
```markdown
#example.env

MYSQL_DATABASE_NAME= #name of database
MYSQL_ROOT_PASSWORD= #database password
MYSQL_USERNAME= #database username

MONGO_DB_USERNAME= #database username
MONGO_DB_PASSWORD= #database password
MONGO_DATABASE_NAME= #name of database
```


### **Database Configuration**

The application uses a dual-database approach:
- **MySQL**: Stores core business entities (Projects, Tasks, Developers)
- **MongoDB**: Stores audit logs

---

## **📚 API Documentation**

### **Interactive Documentation**
Access the Swagger UI for interactive API testing:
```
http://localhost:8080/swagger-ui/index.html
```

### **API Specification**
OpenAPI 3.0 specification available at:
```
http://localhost:8080/v3/api-docs
```

![API Documentation](api-docs/api-doc.png)

---

## **🔧 Development**

### **Local Development Setup**

1. **Install dependencies**
   ```bash
   mvn clean install
   ```

2. **Build target file**
   ```bash
   mvn clean package
   ```
3. **Run .jar file**
    ```bash
   cd target
   java -jar tracker-0.0.1-SNAPSHOT.jar
   ```


### **Project Structure**
```
src/
├── main/
│   ├── java/com/project/tracker/
│   │   ├── controllers/               # Controllers
│   │   ├── dto/                       # Response and Request DTO's
│   │   │   ├── requestDto/
│   │   │   └── responseDto/
│   │   ├── exceptions/
│   │   │   ├── customExceptions/      # Custom exceptions
│   │   │   └── globalExceptions/      # global exception handling
│   │   ├── models/                    # Models
│   │   ├── repositories/              # Repositories
│   │   ├── services/                  # Service Layer interfaces and Impl
│   │   │   └── serviceInterfaces/
│   │   └── sortingEnums/              # Enums for sorting and status
│   │   └── TrackerApplication.java
│   └── resources/
│       └── application.yml            # Configuration
└── test/                              # Unit and Integration Tests
```
---