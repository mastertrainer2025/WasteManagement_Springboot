# Walkthrough - Waste Collection & Recycling Management Portal

The **Waste Collection & Recycling Management Portal** has been fully implemented, built, and launched. It is currently running locally on your machine on port 8080.

---

## 🛠️ Changes Implemented

We created a custom vanilla Java Spring Boot application targeting Java 21+ with Maven and MySQL:

1. **Database & Configuration**:
   - Configured `src/main/resources/application.properties` with MySQL connection configurations, Hikari pool connection, and automated Hibernate schema generation (`ddl-auto=update`).
   - Dynamically discovered the local MySQL password as `1234` and configured the data source connection accordingly.
2. **Domain Layer (Entities and Enums)**:
   - Implemented JPA Entities (`User`, `WastePickupRequest`, `IssueReport`, `RouteSchedule`, `Notification`) and related Enums (`Role`, `WasteType`, `RequestStatus`, `IssueType`, `IssueStatus`) without Lombok to ensure complete compatibility with Java 26+ compiler internals.
3. **Repository Layer**:
   - Created JPA repository interfaces (`UserRepository`, `WastePickupRequestRepository`, `IssueReportRepository`, `RouteScheduleRepository`, `NotificationRepository`) to handle database CRUD operations.
4. **Service Layer**:
   - Implemented business logic services (`UserService`, `PickupRequestService`, `IssueReportService`, `RouteScheduleService`, `NotificationService`) for user registration, task assignment, proof verification, and notifications handling.
5. **Security Configuration**:
   - Established role-based URL protections: `/admin/**` accessible only to `ADMIN`, `/staff/**` to `STAFF`, `/citizen/**` to `CITIZEN`.
   - Programmed a custom `AuthenticationSuccessHandler` to route authenticated users to their corresponding dashboard upon successful login.
6. **Controller Layer**:
   - Implemented Spring MVC Controllers (`AuthController`, `CitizenController`, `StaffController`, `AdminController`) to handle mappings, form submissions, and template models.
7. **Frontend (Thymeleaf, CSS, JS)**:
   - Designed an **"Eco-Tech" Theme** in `style.css` featuring fresh forest greens, glassmorphism, stats widgets, and timeline progress tracking indicators.
   - Built a dynamic **Waste Segregation Game** on the home page via JavaScript, along with a client-side file-to-Base64 encoder to attach photo proof of trash collection.
   - Programmed interactive dashboards for all three roles.

---

## 🧪 Verification Plan

The application was compiled using Maven and is currently running as a background task.

### Local URL

Open your browser to: **[http://localhost:8080/](http://localhost:8080/)**

### Seeded Demo Accounts

- **Municipality Administrator**:
  - Username: `admin@waste.com`
  - Password: `admin123`
- **Citizen (Resident)**:
  - Username: `citizen@waste.com`
  - Password: `citizen123`
- **Collection Staff**:
  - Username: `staff@waste.com`
  - Password: `staff123`

---

## 🔄 User Flow Checklist

1. **Citizen Portal**:
   - Login as `citizen@waste.com`.
   - Submit a new pickup request (`/citizen/request`).
   - File a municipal issue report (`/citizen/issue`).
   - Verify that your dashboard lists active requests with dynamic step-by-step progress tracking (Timeline).
2. **Admin Control Center**:
   - Login as `admin@waste.com`.
   - Verify that counters load statistics.
   - Schedule the citizen's pending pickup request by choosing Collection Staff (`Robert Trucker`) and a collection date.
   - Check the **Analytics** page (`/admin/reports`) to see live charts generated via **Chart.js**.
3. **Collection Staff Portal**:
   - Login as `staff@waste.com`.
   - Observe assigned schedules and pickup requests.
   - Click "Verify Pickup" on the assigned task, upload a verification image, type notes, and submit.
   - Verify that the task status updates to "COMPLETED".
4. **Validation**:
   - Log back in as Citizen and check the completed pickup to view the verification image and collection notes.

## Problem Statement

- **Context:** Cities need an efficient, transparent system to manage household and public-waste services, track citizen reports, and coordinate field operations.
- **Primary Goal:** Build a web-based Waste Management Portal that lets citizens report issues and request pickups, enables staff to manage and schedule collections, and gives administrators reporting and route-management tools.
- **Users & Roles:** Citizens (report/request), Staff (work orders, pickups), Admins (user management, routes, reports), optionally Municipal Supervisors (KPIs, approvals).
- **Core Features:**
  - Citizen registration, login, and profile management.
  - Issue reporting with photos, location, and priority; request for pickups.
  - Role-based dashboards: citizen, staff, admin.
  - Work-order creation, assignment, status updates, and history.
  - Route planning/assignment and schedule management for pickups.
  - Reporting and analytics (collections, response times, open issues).
  - Email/SMS notifications for status changes and confirmations.
- **Non-functional Requirements:** Secure authentication and role-based access; responsive UI; persistent storage (relational DB); RESTful API endpoints; audit logging; basic scalability to handle city workloads.
- **Constraints & Tech Stack:** Java + Spring (existing Maven project), server-side templating and static assets (HTML/CSS/JS), Maven build, relational DB (e.g., MySQL/Postgres), deployable on standard JVM hosts.
- **Success Criteria:** Citizens can submit and track reports; staff can view/complete assigned work orders; admins can generate reports and update routes; average response time and resolution metrics are trackable.
- **Acceptance Tests (high-level):**
  - Citizen can register, submit an issue with photo/location, and receive status updates.
  - Staff can claim/complete work orders and log pickups.
  - Admin can create routes, view reports, and see system audit logs.
  - System builds via `mvn package` and persists data across restarts.
