# RecipeApp – Coding Factory 8 Final Project

## Overview
RecipeApp is a full-stack web application developed as the final project for
**Coding Factory 8** of the **Athens University of Economics and Business (AUEB)**.

The application helps users discover recipes based on the ingredients they already
have available, while also allowing registered users to create, edit, and manage
their own recipes.

The project was designed and implemented following **Domain-Driven Design principles**
and a **layered architecture**, focusing on clean code, separation of concerns,
and real-world application structure.

---

## Technologies Used

### Backend
- **Java 21**
- **Spring Boot**
- **Spring MVC**
- **Spring Data JPA (Hibernate)**
- **Spring Security**

### Database
- **MySQL**
- Database schema generated automatically from the domain model
  (Model-First approach)

### Frontend
- **Thymeleaf** (Server-Side Rendering)
- HTML5 / CSS3
- JavaScript (for client-side filtering and UI enhancements)

### Build & Tools
- **Maven**
- **Hibernate ORM**
- **JPA Repositories**
- **Session-based authentication**

---

## Application Architecture

The application follows a **layered architecture**:

- **Controller Layer**
    - Handles HTTP requests
    - Manages routing and UI rendering (Thymeleaf)
- **Service Layer**
    - Contains business logic
    - Coordinates data flow between controllers and repositories
- **Repository Layer**
    - Handles persistence using Spring Data JPA
- **Domain Model**
    - Represents real-world entities and relationships

This separation ensures maintainability, scalability, and testability.

---

## Domain Model

### Entities
- **User**
    - Represents registered users of the application
- **Recipe**
    - Represents a cooking recipe
- **Ingredient**
    - Represents a unique ingredient
- **RecipeIngredient**
    - Join entity between Recipe and Ingredient
    - Stores quantity and measurement unit

### Relationships
- A **User** can own multiple **Recipes**
- A **Recipe** consists of multiple **Ingredients**
- Ingredients are reusable across different recipes

The database structure is automatically created based on these entities using JPA.

---

## Core Features

### Fridge – Recipe Search
- Users enter ingredients they have available
- The system compares input ingredients with stored recipes
- Recipes are ranked based on:
    - Matching ingredients
    - Missing ingredients
- Recipes with fewer missing ingredients are shown first

### Recipe Recommendations
- Perfect matches are highlighted
- Partial matches show which ingredients are missing
- No-match cases are handled with user-friendly messages

### Recipe Management (Authenticated Users)
- Create new recipes
- Edit existing recipes
- Add or remove ingredients
- Delete recipes
- View personal recipe list

### Public Access
- Guest users can:
    - Browse all recipes
    - Use the fridge search functionality

---

## Authentication & Authorization

- **Spring Security** is used for authentication and authorization
- Guest users:
    - Can search and view recipes
- Authenticated users:
    - Can create recipes
    - Can edit and delete **only their own recipes**
- Authorization rules are enforced both in:
    - Controllers
    - UI (Thymeleaf security tags)

---

## Data Handling & Performance
- Batch inserts enabled for ingredients and recipe relations
- Optimized JPA queries
- Transactions handled at the service layer
- Duplicate ingredients are avoided using normalization logic

---

## Build & Run Instructions

### 1. Create Database
```sql
CREATE DATABASE recipe_fridge;
