Record Shop Capstone – RESTful E-Commerce API
📌 Project Overview
===================

Record Shop is a RESTful e-commerce backend built with Java, Spring Boot, and MySQL. This capstone demonstrates backend development concepts, including user authentication, role-based authorization, database persistence, and API testing.

The application supports user registration and login with JWT authentication, category management with admin permissions, and structured DAO-based database access.

<Image border={false} src="https://files.readme.io/60d7f968cebe3336c34d5b34d3eb093c9078eb80f0e0e875e0c4ebd94ee2cbdd-Screenshot_2025-12-17_at_3.15.08PM.png" />

<Image border={false} src="https://files.readme.io/91360b4a86cf2a40f56273cf8788fbf456360e9b1708ac54850c14d8993762f9-Screenshot_2025-12-17_at_3.15.19PM.png" />

<Image border={false} src="https://files.readme.io/26151b36846e6505a12a0056e4bc93ca3e6c36a41257ff8743a29ff8203429ce-Screenshot_2025-12-17_at_3.15.27PM.png" />

<Image border={false} src="https://files.readme.io/fc62d8bf51ce07066b99e56289c6ea6da8314465d52ac96d4bb83f3e3b50cd98-Screenshot_2025-12-17_at_3.15.50PM.png" />



***

## 🛠️ Technologies Used

* Java
* Spring Boot
* Spring Security + JWT
* MySQL
* Apache DBCP (BasicDataSource)
* Insomnia (API testing)
* Maven

***

## 🔐 Key Features

* User registration and login
* Password confirmation validation
* JWT-based authentication
* Role-based authorization (ADMIN vs USER)
* CRUD operations for categories
* DAO pattern for database access
* Centralized error handling
* RESTful API design

***

## 🗄️ Database Setup

This project requires a local MySQL database.

**1️⃣ Create the database**

```sql
CREATE DATABASE recordshop;
```

**2️⃣ Update application.properties**

```properties
datasource.url=jdbc:mysql://localhost:3306/recordshop
datasource.username=root
datasource.password=your_password
```

**3️⃣ Run table creation scripts**

Run the provided SQL scripts (if included) to create tables such as:

* users
* profiles
* categories

***

## 🚀 Running the Application

1. Start MySQL
2. Open the project in IntelliJ
3. Run the Spring Boot application

Server runs on: [http://localhost:8080](http://localhost:8080)

***

## 🔍 API Endpoints

**Authentication**

| Method | Endpoint    | Description                    |
| ------ | ----------- | ------------------------------ |
| POST   | `/register` | Register a new user            |
| POST   | `/login`    | Authenticate user & return JWT |

**Categories**

| Method | Endpoint      | Authorization |
| ------ | ------------- | ------------- |
| GET    | `/categories` | Public        |
| POST   | `/categories` | ADMIN only    |

***

## 🧪 Testing

API endpoints were tested using Insomnia with automated test scripts to verify:

* Successful registration & login
* Authorization enforcement
* Correct HTTP status codes
* Error handling

***

## ⚠️ Error Handling

The application uses ResponseStatusException to return meaningful HTTP responses:

* 400 – bad request (validation errors)
* 401 – unauthorized
* 403 – forbidden
* 500 – internal server errors

***

## 💡 Lessons Learned

* Importance of proper database configuration
* Normalizing user input (e.g., usernames)
* Handling SQL and authentication errors safely
* Debugging layered backend systems
* Implementing role-based access control

***

## Bug Fixes

| Requirement                         | Where Fixed                | How                                    |
| ----------------------------------- | -------------------------- | -------------------------------------- |
| Bug #1 – Product search filtering   | `MySqlProductDao.search()` | Dynamic SQL + conditional filters      |
| Bug #2 – Product update duplication | `ProductsController PUT`   | Switched from `create()` to `update()` |
| Category admin-only writes          | `CategoriesController`     | `@PreAuthorize("hasRole('ADMIN')")`    |
| Profile auto-created on register    | `AuthenticationController` | `profileDao.create(profile)`           |
| Cart uses logged-in user            | `ShoppingCartController`   | `Principal → userDao → userId`         |



***

<Image align="center" border={false} caption="Passing all Insomnia Postman Scripts" src="https://files.readme.io/8e72936393ad00c7f8ec383a68b53112a091a0de633b12115d5e2641f417cc63-Screenshot_2025-12-17_at_2.41.17PM.png" />
