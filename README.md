# ZooCare – zoo animal health management system
Project description is also available in Russian: [Russian version](README.ru.md).

## Overview
ZooCare is a pet project web application for centralized, automated management of zoo animal health records. Its purpose is to support timely veterinary care and proper animal welfare.

## Features
+ User authentication and role-based access control (administrator, zookeeper, veterinarian).
+ Manage animals, employees, enclosures, medications, suppliers, examinations, medical procedures, and health observations.
+ Filtering and sorting by multiple criteria.
+ Secure REST API with JWT authentication.

## Tech Stack
+ **Languages:** Java 17, SQL.
+ **Frameworks:** Spring (Boot, Data, Security), Hibernate.
+ **Database:** PostgreSQL.
+ **Build Tool:** Apache Maven.
+ **Security:** JSON Web Token (JWT).
+ **API & Tools:** REST, HTTP, Swagger, Postman.
+ **Version Control:** Git, GitHub.

Developed in IntelliJ IDEA Community Edition.

## Database
The app uses a relational database model. The database contains 10 tables. The logical model of the database is shown below using ERD (Martin’s notation).
![ER diagram](https://github.com/NadezhdaLikh/zoocare/blob/master/ER-model-martin.jpg)

## User roles and permissions
**Administrator** manages employees and assigns roles.

**Veterinarian:**
+ manages medications, suppliers, stock, examinations, procedures, usage records.
+ validates and comments on zookeeper observations.

_Only the creator of an observation, examination, or procedure can edit it._

**Zookeeper:** creates and updates own animal observations (re-validation required after edits).

**All users** can view employees, animals, enclosures, medications, suppliers, stock, and medical records.

## Registration & authentication
+ Admin registers zookeepers and veterinarians **manually**.
+ System generates a password for a new user and sends it to him via email. User's login = user’s email.
+ Admin **does not** require registration.

## Deployment
### 1. Clone the repository
```
git clone https://github.com/NadezhdaLikh/ZooCare.git
cd zoocare
```

### 2. Configure application parameters
Copy application.yml.example from backend to application.yml and update with your credentials:
+ database: name, username, password;
+ SMTP: host, port, username, password.

### 3. Add admin's credentials and personal info
**Before** the first run, open `backend/src/main/java/ru/codeline/configuration/AdminCredentials.java` and set admin's data. On first launch, the record is automatically added to the database.

### 4. Run the project
```
mvn spring-boot:run
```

## API Documentation
Swagger UI will be available at: http://localhost:8080/swagger-ui/index.html.
