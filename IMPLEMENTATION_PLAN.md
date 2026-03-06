# Ocean View Resort - Implementation Plan

## 1. Goal
Build a role-based Resort Reservation and Billing system using:
- Backend: Java, Jakarta Servlet/JSP, JDBC
- Database: MySQL
- Frontend: JSP, HTML5/CSS3, JavaScript, Chart.js
- Roles: Admin, Staff, Guest

This plan is based on `project plan.txt` and converts it into an executable delivery roadmap.

## 2. Scope (MVP)
- Authentication and authorization for Admin, Staff, Guest
- Guest registration and login
- Room type and room management (Admin)
- Reservation management (Admin/Staff full, Guest own records)
- Billing generation and viewing
- Dashboards with summary charts (Chart.js via JSON servlet endpoints)
- Validation, error handling, and basic audit fields

Out of scope for MVP:
- Payment gateway integration
- Email/SMS notifications
- Multi-property support

## 3. Proposed Architecture
- Presentation layer: JSP views under `WEB-INF/views`
- Controller layer: Servlets mapped by feature and role
- Service layer: business rules and transaction boundaries
- DAO layer: JDBC + prepared statements
- Data layer: MySQL schema + seed data
- Security layer: AuthFilter + RoleFilter + HTTP session

Pattern: `Servlet -> Service -> DAO -> MySQL`

## 4. Target Project Structure
Use the structure in `project plan.txt` with package root:
`com.oceanview.resort`

Primary folders:
- `controller`, `controller.admin`, `controller.staff`, `controller.guest`
- `api`, `filter`, `service`, `dao`, `model`, `validation`, `util`, `exception`, `listener`
- JSP views under `src/main/webapp/WEB-INF/views/*`
- SQL scripts under `sql/schema.sql` and `sql/seed.sql`

## 5. Role Matrix
| Feature | Admin | Staff | Guest |
|---|---|---|---|
| Login/Logout | Yes | Yes | Yes |
| Register | No | No | Yes |
| Manage Users | Full CRUD | No | No |
| Manage Room Types | Full CRUD | Read | Read |
| Manage Rooms | Full CRUD | Read | Read |
| Reservations | Full CRUD | Full CRUD | CRUD own only |
| Billing | Full CRUD | Create/View | View own only |
| Reports/Dashboard Charts | Full | Operational | Personal |

## 6. Database Plan (MySQL)
Core tables:
- `users` (id, username, password_hash, role, status, created_at, updated_at)
- `guest_profiles` (id, user_id, full_name, email, phone, nic_passport, address)
- `room_types` (id, name, base_price, capacity, description)
- `rooms` (id, room_number, room_type_id, status, floor)
- `reservations` (id, guest_id, room_id, check_in, check_out, adults, children, status, booked_by, created_at)
- `bills` (id, reservation_id, subtotal, tax, discount, total, issued_by, issued_at, payment_status)

Constraints:
- FK constraints on all relationships
- Unique indexes on `users.username` and `rooms.room_number`
- Reservation overlap prevention at service/DAO query level

## 7. Endpoint and View Plan
Common:
- `/login`, `/logout`, `/register`, `/help`

Admin:
- `/admin/dashboard`, `/admin/users`, `/admin/rooms`, `/admin/roomtypes`

Staff:
- `/staff/dashboard`, `/staff/reservations/*`, `/staff/billing/*`

Guest:
- `/guest/dashboard`, `/guest/reservations/*`, `/guest/bills/*`

API (JSON):
- `/api/reports/occupancy`
- `/api/reports/revenue`
- `/api/reports/reservation-trend`

## 8. Implementation Phases

### Phase 0 - Foundation Setup
Deliverables:
- Update `pom.xml` to Jakarta Servlet/JSP + JSTL + MySQL JDBC + JUnit 5
- Add Maven profiles/properties for Java version
- Replace legacy `web.xml` with Jakarta EE 6.0+ compatible descriptor
- Add `db.properties` and `AppContextListener`

Acceptance:
- Application starts on Tomcat 10+
- DB connection test endpoint/log passes

### Phase 1 - Database and Domain Layer
Deliverables:
- `sql/schema.sql` and `sql/seed.sql`
- Model classes: User, GuestProfile, RoomType, Room, Reservation, Bill
- DAO interfaces/implementations with JDBC prepared statements

Acceptance:
- Schema creates without error
- DAO smoke tests pass for basic CRUD

### Phase 2 - Security and Access Control
Deliverables:
- `AuthServlet`, `RegisterServlet`, `PasswordUtil`
- `AuthFilter` and `RoleFilter`
- Session handling and logout invalidation

Acceptance:
- Unauthorized access is blocked
- Role-based route protection verified

### Phase 3 - Admin Modules
Deliverables:
- Admin dashboard servlet + JSP with charts
- User management CRUD
- Room type and room CRUD

Acceptance:
- Admin can manage users/rooms/room types end-to-end

### Phase 4 - Staff Modules
Deliverables:
- Staff dashboard
- Reservation CRUD for all guests
- Bill generation/update

Acceptance:
- Staff can create/edit/check-in/check-out reservations and generate bills

### Phase 5 - Guest Modules
Deliverables:
- Guest dashboard
- Own reservation CRUD
- Bill view for own reservations

Acceptance:
- Guest can only access own data (verified by ID ownership checks)

### Phase 6 - Reporting and Charts
Deliverables:
- `ReportApiServlet` JSON endpoints
- Chart.js integration in dashboards

Acceptance:
- Charts render with live DB data

### Phase 7 - Quality Hardening
Deliverables:
- Validation layer (`RegisterValidator`, `ReservationValidator`)
- Exception handling and friendly error pages
- Unit tests for service and validation packages
- Basic manual test checklist by role

Acceptance:
- Core unit tests pass
- No critical security or data-integrity defects in checklist

## 9. UI Plan (JSP/CSS/JS)
- Shared JSP fragments: `header.jspf`, `nav.jspf`, `footer.jspf`
- Role-specific navigation menus
- Form validation in both client-side JS and server-side validators
- Responsive layout with consistent component classes
- Chart pages load data asynchronously from `/api/reports/*`

## 10. Security and Data Integrity Rules
- Passwords hashed with BCrypt (never plain text)
- Prepared statements only (no string-concatenated SQL)
- Session timeout + secure logout
- Ownership checks for Guest data access
- Soft delete or status flags for business records where needed

## 11. Testing Plan
- Unit tests:
  - `ReservationServiceTest`
  - `BillingServiceTest`
  - `RegisterValidatorTest`
- Integration checks:
  - Login/role redirect flow
  - Reservation overlap prevention
  - Bill total calculations
- Manual UAT script for Admin/Staff/Guest

## 12. Delivery Sequence and Milestones
1. Foundation + DB scripts
2. Auth + filters
3. Admin modules
4. Staff modules
5. Guest modules
6. Reports/charts
7. Testing and bug fixing

Milestone status:
- Foundation + DB scripts: Completed
- Auth + filters: Completed
- Admin modules: Completed
- Staff modules: Completed
- Guest modules: Completed
- Reports/charts: Completed
- Testing and bug fixing: In progress (automated tests green; manual UAT pending final execution/sign-off)

## 13. Definition of Done
- All role-based features implemented as per scope
- Schema and seed scripts reproducible
- App deploys on Tomcat 10+ via WAR
- Critical test scenarios pass
- Documentation includes setup and run instructions

Tracking:
- DoD status checklist is maintained in `docs/DOD_STATUS.md`.

## 14. Next Step After Approval
After you approve this plan, implementation starts in this order:
1. Upgrade Maven/Jakarta configuration
2. Create DB scripts and JDBC utility
3. Build auth and role filters
4. Deliver role modules incrementally with test checkpoints
