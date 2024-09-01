# Splitwise-API

Welcome to the Splitwise-API, a backend service designed for managing group and individual expenses. This API is built with Java Spring Boot and supports features such as expense tracking, group management, and user balance calculations.

## 📑 Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Deployment](#deployment)

## 🌟 Features

- **User Management**: Register and login users.
- **Expense Tracking**: Create and track expenses between individuals and groups.
- **Group Management**: Create and track expenses between groups of users for shared expenses.
- **Balance Calculation**: Calculate individual and group balances to see who owes whom.
- **Containerization**: Docker support for easy deployment.

## 🛠️ Technologies

- **Java Spring Boot**: Backend framework
- **Java Spring Boot**: Backend framework
- **MySQL**: Relational database
- **Hibernate**: ORM for database interactions
- **JWT**: Authentication and authorization
- **Docker**: Containerization
- **Heroku**: Deployment

## 📂 Project Structure

```plaintext
splitwise-api/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.example.splitwise/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── entities/
│   │   │       │   ├── request/
│   │   │       │   └── response/
│   │   │       ├── exception/
│   │   │       ├── repository/
│   │   │       │   └── dao/
│   │   │       ├── service/
│   │   │       │   └── impl/
│   │   │       └── table/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── Dockerfile
├── init.sql
├── pom.xml
└── README.md
```

## 🚀 Getting Started

### Prerequisites

* **Java 17**
* **Maven 3.6+**
* **MySQL 8.0+**
* **Docker** (optional, for containerization)

### Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/yourusername/splitwise-api.git 
    cd splitwise-api
    ```

2. **Configure the database**:
    * Update the `application.properties` file in `src/main/resources/` with your MySQL credentials.
    * Run the `init.sql` script to set up the initial database schema.

3. **Build the project**:
    ```bash
    mvn clean install
    ```

### Running the Application

* **Using Maven**:
    ```bash
    mvn spring-boot:run
    ```

### Using Docker

1. **Build the Docker image**:
    ```bash
    docker build -t splitwise-api .
    ```

2. **Tag the Docker image** (optional, for pushing to Docker Hub):
    ```bash
    docker tag splitwise-api suntwalriya/splitwise-api:v1.0
    ```

3. **Push the Docker image to Docker Hub** (optional, for sharing or deployment):
    ```bash
    docker push suntwalriya/splitwise-api:v1.0
    ```

4. **Run the Docker container**:
    ```bash
    docker run -p 8080:8080 suntwalriya/splitwise-api:v1.0
    ```

   or if running the local build:
    ```bash
    docker run -p 8081:8081 splitwise-api
    ```

## 📖 API Documentation

https://shimmering-forsythia-d94.notion.site/Splitwise-API-917b143a391d4d28a9ea4ccaf4caf43a?pvs=4

### System Health Check

* **Health Check**: `GET /api/v1/healthCheck`

### User Management

* **Register**: `POST /api/v1/register`
* **Login**: `POST /api/v1/login`

### Group Management

* **Create Group**: `POST /api/v1/groups`

### Expense Management - Create and Settle Expense

* **Create Expense**: `POST /api/v1/expenses`

### Fetch Balance API

* **Fetch Balance**: `GET /api/v1/fetch/details/{userId}`


## 🌍 Deployment

### Heroku

1. **Login to Heroku**:
    ```bash
    heroku login
    ```

2. **Create a new app**:
    ```bash
    heroku create splitwise-api
    ```

3. **Deploy the app**:
    ```bash
    git push heroku main
    ```

4. **Open the app**:
    ```bash
    heroku open
    ```

### Docker

* **Push the Docker image to a registry**:
    ```bash
    docker tag splitwise-api suntwalriya/splitwise-api:v1.0
    docker push suntwalriya/splitwise-api:v1.0
    ```

