# Implementation Plan - Waste Collection & Recycling Management Portal

The goal is to build a full-featured, secure, and beautiful **Waste Collection & Recycling Management Portal** using Java Spring Boot, Maven, MySQL, Thymeleaf, and Bootstrap 5. The application will support three distinct user roles: Citizens, Collection Staff, and Administrators.

## User Review Required

> [!IMPORTANT]
> **Database Credentials & Schema Initialisation**
> - The application will use Hibernate's `ddl-auto=update` to generate the MySQL schema automatically on first boot.
> - We will create a `DataInitializer` class to automatically seed the database with default roles, users, routes, requests, and notifications to allow immediate testing.
> - Default Database: `waste_management_db`. Username: `root`. Password: (empty by default). You can override this in `src/main/resources/application.properties`. Please confirm if you have a specific database password we should set by default.

## Proposed System Design & Database Schema

We will use standard JPA entities to model the system.

### User Entity
- `id` (Long, PK)
- `email` (String, Unique) - Used as the username for login
- `password` (String) - BCrypt hashed
- `name` (String)
- `phone` (String)
- `address` (String)
- `role` (Role Enum: `CITIZEN`, `STAFF`, `ADMIN`)
- `enabled` (boolean)

### WastePickupRequest Entity
- `id` (Long, PK)
- `citizen` (User, FK)
- `wasteType` (WasteType Enum: `ORGANIC`, `RECYCLABLE`, `HAZARDOUS`, `BULKY`)
- `pickupAddress` (String)
- `requestedDate` (LocalDateTime)
- `scheduledDate` (LocalDate)
- `assignedStaff` (User, FK, Nullable)
- `status` (RequestStatus Enum: `PENDING`, `ASSIGNED`, `COMPLETED`, `CANCELLED`)
- `notes` (String)
- `collectionProofNotes` (String)
- `collectionProofImage` (String, Base64 or placeholder URL)

### IssueReport Entity
- `id` (Long, PK)
- `citizen` (User, FK, Nullable for anonymous or citizen reports)
- `issueType` (IssueType Enum: `UNCOLLECTED_WASTE`, `BLOCKED_ROAD`, `OVERFILLED_BIN`, `OTHER`)
- `description` (String)
- `location` (String)
- `status` (IssueStatus Enum: `REPORTED`, `INVESTIGATING`, `RESOLVED`)
- `reportedDate` (LocalDateTime)
- `assignedStaff` (User, FK, Nullable)
- `resolutionNotes` (String)

### RouteSchedule Entity
- `id` (Long, PK)
- `routeName` (String)
- `wasteType` (WasteType Enum)
- `scheduledDay` (String, e.g., "Monday", "Wednesday")
- `assignedStaff` (User, FK)
- `active` (boolean)

### Notification Entity
- `id` (Long, PK)
- `title` (String)
- `content` (String)
- `recipient` (User, FK, Nullable - if Null, it is a public announcement/awareness tip)
- `createdDate` (LocalDateTime)
- `read` (boolean)

---

## Proposed Changes

We will generate the project structure using Spring Initializr, configure Security, and implement controllers, repositories, services, and views.

### 1. Backend Project Foundation & Configuration

#### [NEW] [pom.xml](file:///c:/Users/hari2/OneDrive/Desktop/Antigravity_Java_Project/pom.xml)
- Declares dependencies: `spring-boot-starter-web`, `spring-boot-starter-thymeleaf`, `spring-boot-starter-data-jpa`, `spring-boot-starter-security`, `thymeleaf-extras-springsecurity6`, `mysql-connector-j`, `lombok`, and `spring-boot-devtools`.

#### [NEW] [application.properties](file:///c:/Users/hari2/OneDrive/Desktop/Antigravity_Java_Project/src/main/resources/application.properties)
- Configures database connection string `jdbc:mysql://localhost:3306/waste_management_db?createDatabaseIfNotExist=true`.
- Sets Hibernate DDL-to-DDL auto generation to `update`.
- Configures Thymeleaf cache settings (disabled in dev) and logging level.

#### [NEW] [SecurityConfig.java](file:///c:/Users/hari2/OneDrive/Desktop/Antigravity_Java_Project/src/main/java/com/waste/portal/config/SecurityConfig.java)
- Configures Spring Security 6.x.
- Roles mapping: `/admin/**` accessible only to `ROLE_ADMIN`, `/staff/**` to `ROLE_STAFF`, `/citizen/**` to `ROLE_CITIZEN`.
- Custom login page (`/login`), login success redirect handler (redirects users based on their role).
- Log out behavior.

### 2. Core Entities & Enums

#### [NEW] [Enums.java](file:///c:/Users/hari2/OneDrive/Desktop/Antigravity_Java_Project/src/main/java/com/waste/portal/model/Role.java)
- Defines: `Role` (CITIZEN, STAFF, ADMIN), `WasteType` (ORGANIC, RECYCLABLE, HAZARDOUS, BULKY), `RequestStatus` (PENDING, ASSIGNED, COMPLETED, CANCELLED), `IssueType` (UNCOLLECTED_WASTE, BLOCKED_ROAD, OVERFILLED_BIN, OTHER), `IssueStatus` (REPORTED, INVESTIGATING, RESOLVED).

#### [NEW] [Entities](file:///c:/Users/hari2/OneDrive/Desktop/Antigravity_Java_Project/src/main/java/com/waste/portal/model/)
- Contains JPA Entity mapping files: `User.java`, `WastePickupRequest.java`, `IssueReport.java`, `RouteSchedule.java`, `Notification.java`.

### 3. Repositories

#### [NEW] [Repositories](file:///c:/Users/hari2/OneDrive/Desktop/Antigravity_Java_Project/src/main/java/com/waste/portal/repository/)
- Contains Spring Data JPA interfaces for each entity: `UserRepository.java`, `WastePickupRequestRepository.java`, `IssueReportRepository.java`, `RouteScheduleRepository.java`, `NotificationRepository.java`.

### 4. Services

#### [NEW] [Services](file:///c:/Users/hari2/OneDrive/Desktop/Antigravity_Java_Project/src/main/java/com/waste/portal/service/)
- Business logic handlers:
  - `UserService.java`: Register, authenticate, manage users.
  - `PickupRequestService.java`: Create pickup request, retrieve citizen requests, assign staff, complete requests with notes/proof.
  - `IssueService.java`: Report issue, list all, assign staff, resolve issue.
  - `RouteService.java`: Manage schedules and assignments.
  - `NotificationService.java`: Push notifications to individuals or all citizens.

### 5. Controllers

#### [NEW] [Controllers](file:///c:/Users/hari2/OneDrive/Desktop/Antigravity_Java_Project/src/main/java/com/waste/portal/controller/)
- `HomeController.java`: Landing/home page, public awareness section.
- `AuthController.java`: Registration and login mappings.
- `CitizenController.java`: Citizen request submission, complaint filing, request/complaint status dashboards.
- `StaffController.java`: Staff schedules, updating pickup status, reporting route issues, uploading notes/proof.
- `AdminController.java`: System dashboard, user management, route assignment, operations monitoring, analytical report generating.

### 6. Templates (Thymeleaf & Bootstrap 5)

We will use modern, custom styling built on top of Bootstrap 5.3 with Forest Green / Eco theme palettes. Chart.js will be used for analytics.

#### [NEW] [Templates Folder](file:///c:/Users/hari2/OneDrive/Desktop/Antigravity_Java_Project/src/main/resources/templates/)
- `layout/main.html`: Base layout containing navigation bar (dynamic based on role) and footer.
- `home.html`: Landing page showing modern features and an awareness carousel for waste segregation.
- `login.html` & `register.html`: Glassmorphic authentication forms.
- `citizen/dashboard.html`: Citizen portal (history lists with status tracking steps, file issue, request pickup).
- `citizen/request-pickup.html` & `citizen/report-issue.html`: Forms for requesting pickups and reporting issues.
- `staff/dashboard.html`: Staff hub showing assigned tasks and schedules.
- `staff/update-request.html`: Form to submit proof of collection (image placeholder upload + notes).
- `admin/dashboard.html`: Comprehensive dashboard showing operational statistics, active staff, unresolved issues.
- `admin/users.html`: User administration list and forms.
- `admin/routes.html`: Assign schedules to collection staff.
- `admin/reports.html`: Charts displaying analytics (waste types collected, resolution times, complaint trends).

---

## Verification Plan

### Automated Verification
- Verify the build compiles and runs: `mvnw clean compile` (or running using our maven wrapper).
- Start the server using `mvnw spring-boot:run` and verify that database tables are created automatically and the initial data is seeded.

### Manual Verification
We will verify the user flows by logging in with seeded test accounts:
1. **Admin Verification**: Log in as `admin@waste.com` / `admin123`.
   - Access admin panel.
   - View seeded users, routes, and statistics.
   - Create a new collection route and assign it to staff.
2. **Citizen Verification**: Log in as `citizen@waste.com` / `citizen123`.
   - Submit a new waste pickup request.
   - Report an overfilled bin issue.
   - Check the status timeline of the pickup request.
3. **Staff Verification**: Log in as `staff@waste.com` / `staff123`.
   - View assigned collection routes.
   - View the pickup request submitted by the citizen.
   - Complete the request by adding notes & upload mock proof.
4. **Logout & Auth Restrictions**:
   - Ensure citizens cannot access `/admin/**`.
   - Ensure non-logged-in users are redirected to `/login`.
