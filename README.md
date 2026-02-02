## Employee Onboarding Application

This project is a simple Employee Onboarding web application developed as part of a job application assignment for an Appian Developer position at End2End.

The goal of the application is to demonstrate a clear and meaningful approach to the employee onboarding business process, rather than focusing on excessive functionality or cutting-edge technologies.

## Business Process Overview

The onboarding process starts with the creation of a new employee in the system.
Each employee is assigned an initial onboarding status and can progress through
predefined onboarding phases.

During the onboarding process:
- Tasks can be manually added to the employee
- Tasks represent concrete actions that must be completed by HR, IT, or managers
- The employee status reflects the overall onboarding progress

The process is intentionally kept simple and transparent to clearly demonstrate
the onboarding flow and decision points.


## Technologies Used

Java 21

Spring Boot

Spring Data JPA (Specifications)

Thymeleaf

PostgreSQL

Maven

Lombok

## Core Functionalities
## 1. Employee Management

Create a new employee

View a list of all employees

Search employees by:

Last name

Onboarding status

View detailed information for a single employee

## 2. Onboarding Process

Each employee goes through predefined onboarding statuses (e.g.:
OFFER_ACCEPTED, PRE_EMPLOYMENT, ONBOARDING, etc.).

Status transitions are controlled via a dedicated service

Only valid transitions are allowed

A “Next step” action moves the employee forward in the onboarding process

## 3. Onboarding Tasks

Manually add onboarding tasks to an employee

Tasks are categorized by type (HR, IT, LEGAL, MANAGER)

Tasks can be marked as completed

Tasks remain linked to the employee for full traceability

## 4. Cancellation (Optional Extension)

Onboarding can be cancelled using a dedicated status

Cancelled employees are not deleted

This allows future reactivation or auditing


## Design Considerations

Clear separation of concerns (Controller / Service / Repository)

Business logic placed in services, not controllers

Simple UI focused on usability

Easy extensibility for:

Automatic task generation

Reporting

Notifications

Role-based access

## Possible Future Improvements

Automatic task creation based on onboarding status

Export of onboarding data (CSV / PDF)

Improved UI styling

Authentication & authorization

Audit log of status changes

## Running the Application

Configure PostgreSQL and update application.yml

Run the Spring Boot application

##  Main Endpoints

### Employees
- `GET /employees`  
  Lists all employees with optional search by last name and status.

- `GET /employees/{id}`  
  Displays detailed view of a single employee and their onboarding tasks.

- `GET /employees/new`  
  Form for creating a new employee.

- `POST /employees`  
  Creates a new employee and initializes the onboarding process.

### Onboarding Tasks
- `POST /employees/{id}/tasks`  
  Adds a new onboarding task to an employee.

- `POST /tasks/{id}/complete`  
  Marks an onboarding task as completed.

### Onboarding Process
- `POST /employees/{id}/status/next`  
  Moves an employee to the next onboarding status (if allowed).


Author

Developed by Antonio Mužar
2026
