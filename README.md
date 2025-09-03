# Hotel Booking Microservices (Enterprise Middleware)

**Coursework for CSC8104 - Enterprise Middleware (Newcastle University)**  
Author: Aryaman Patronia  

---

##  Project Aim
This project implements an **enterprise-level hotel booking application** using **Quarkus**, JPA, and RESTful APIs.  
The application supports hotel, customer, and booking management, along with guest bookings and a distributed travel agent service.  
It is deployed on **OpenShift (Red Hat)** and documented/tested with **Swagger annotations**.

---

##  System Design

### Core Features
- **Customer Service**  
  - Manage customer data with CRUD operations.  
  - Includes validation and repository integration.  

- **Hotel Service**  
  - Manage hotels with unique hotel name validation.  
  - Cascade deletion ensures bookings are removed if hotels are deleted.  

- **Booking Service**  
  - Create and manage reservations between customers and hotels.  
  - Cascade deletion ensures bookings are removed if customers are deleted.  
  - Validators ensure no duplicate bookings.  

- **Guest Booking Service**  
  - Allows a booking to be made for a new customer in one transaction.  
  - Managed with `UserTransaction` for consistency.  

- **Travel Agent Service**  
  - Integrates with external **Taxi** and **Flight** services using `@RegisterRestClient`.  
  - Provides a single transaction for multi-service bookings.  
  - Ensures transactional consistency by rolling back all bookings if one fails.  

---

## Tech Stack
- **Framework:** Quarkus (Java)  
- **Database:** JPA / Hibernate  
- **API:** REST (JAX-RS), Swagger for documentation  
- **Cloud Deployment:** OpenShift (RedHat)  
- **Additional:** JSON serialization with Jackson  
