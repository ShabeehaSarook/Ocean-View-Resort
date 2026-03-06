# Screenshot Checklist for TASK_B_INTERACTIVE_SYSTEM.md

This comprehensive checklist identifies exactly which code sections to screenshot, with file names, line numbers, and what to capture.

---

## 📋 Complete Screenshot Checklist

### ✅ = Must Have | 🔹 = Recommended | ⭐ = Extra Impact

---

## Section 2: Design Patterns Implementation

### ✅ 2.1 Repository Pattern (DAO Layer)

#### Screenshot 1: DAO Interface
- **File:** `src/main/java/com/oceanview/resort/dao/UserDAO.java`
- **Lines to Capture:** 1-19 (entire interface)
- **What to Show:** 
  - Package declaration
  - Import statements
  - Interface declaration
  - All method signatures (create, findById, findByUsername, findAll, update, deleteById)
- **Save As:** `screenshots/design-patterns/01-repository-dao-interface.png`
- **Caption:** "UserDAO interface showing Repository Pattern with standard CRUD operations"

#### Screenshot 2: DAO Implementation - Create Method
- **File:** `src/main/java/com/oceanview/resort/dao/impl/UserJdbcDAO.java`
- **Lines to Capture:** Lines showing the `create()` method (approximately lines 15-40)
- **What to Show:**
  - Method signature
  - SQL query string
  - PreparedStatement usage
  - Parameter binding
  - Exception handling
  - Return statement
- **Save As:** `screenshots/design-patterns/02-repository-jdbc-create.png`
- **Caption:** "UserJdbcDAO create method demonstrating database interaction and exception translation"

#### Screenshot 3: DAO Implementation - FindByUsername Method
- **File:** `src/main/java/com/oceanview/resort/dao/impl/UserJdbcDAO.java`
- **Lines to Capture:** Lines showing `findByUsername()` method
- **What to Show:**
  - SQL SELECT query
  - ResultSet mapping
  - Optional return type
  - Exception handling
- **Save As:** `screenshots/design-patterns/03-repository-jdbc-findbyusername.png`
- **Caption:** "FindByUsername method showing ResultSet mapping and Optional usage"

---

### ✅ 2.2 Service Layer Pattern

#### Screenshot 4: RegisterService Class
- **File:** `src/main/java/com/oceanview/resort/service/RegisterService.java`
- **Lines to Capture:** 1-31 (entire file)
- **What to Show:**
  - Package and imports
  - Class declaration
  - RegisterValidator field
  - Constructor (default and DI constructor)
  - validateGuestRegistration method
  - hashPassword method
- **Save As:** `screenshots/design-patterns/04-service-layer-register.png`
- **Caption:** "RegisterService demonstrating Service Layer Pattern with dependency injection"

#### Screenshot 5: ReservationService Business Rules
- **File:** `src/main/java/com/oceanview/resort/service/ReservationService.java`
- **Lines to Capture:** Lines 27-53 (business rule methods)
- **What to Show:**
  - `ensureCheckInAllowed()` method
  - `ensureCheckOutAllowed()` method
  - `ensureGuestModifyAllowed()` method
  - `isActiveReservationStatus()` method
- **Save As:** `screenshots/design-patterns/05-service-layer-reservation-rules.png`
- **Caption:** "ReservationService business rules enforcing state transitions"

#### Screenshot 6: BillingService Calculations
- **File:** `src/main/java/com/oceanview/resort/service/BillingService.java`
- **Lines to Capture:** Lines 11-50 (calculateTotal and validate methods)
- **What to Show:**
  - `calculateTotal()` method with BigDecimal usage
  - `validateForCreateOrUpdate()` method
  - Error accumulation logic
  - ValidationException throwing
- **Save As:** `screenshots/design-patterns/06-service-layer-billing.png`
- **Caption:** "BillingService showing financial calculations with BigDecimal precision"

---

### ✅ 2.3 Front Controller Pattern

#### Screenshot 7: AuthServlet
- **File:** `src/main/java/com/oceanview/resort/controller/AuthServlet.java`
- **Lines to Capture:** Lines 1-30 (class declaration and doPost start)
- **What to Show:**
  - @WebServlet annotation with URL patterns
  - Class declaration
  - DAO field declarations
  - doGet method signature
  - doPost method signature and routing logic
- **Save As:** `screenshots/design-patterns/07-front-controller-auth.png`
- **Caption:** "AuthServlet showing Front Controller Pattern with URL routing"

#### Screenshot 8: RegisterServlet - Request Handling
- **File:** `src/main/java/com/oceanview/resort/controller/RegisterServlet.java`
- **Lines to Capture:** Lines showing doPost method (approximately lines 25-60)
- **What to Show:**
  - Parameter extraction
  - Service layer delegation
  - Exception handling
  - POST-Redirect-GET pattern
- **Save As:** `screenshots/design-patterns/08-front-controller-register.png`
- **Caption:** "RegisterServlet demonstrating request handling and service delegation"

---

### ✅ 2.4 Data Transfer Object Pattern

#### Screenshot 9: User Entity
- **File:** `src/main/java/com/oceanview/resort/model/User.java`
- **Lines to Capture:** Lines 1-30 (class declaration and fields)
- **What to Show:**
  - Package declaration
  - Class declaration
  - All private fields
  - Constructor signatures
- **Save As:** `screenshots/design-patterns/09-dto-user-entity.png`
- **Caption:** "User entity as Data Transfer Object with no business logic"

🔹 **Optional Screenshot 10: Entity Getters/Setters**
- Same file, lines 27-82 showing getter/setter methods
- **Save As:** `screenshots/design-patterns/10-dto-user-accessors.png`

---

### ✅ 2.5 Strategy Pattern (Validation)

#### Screenshot 11: RegisterValidator
- **File:** `src/main/java/com/oceanview/resort/validation/RegisterValidator.java`
- **Lines to Capture:** Entire `validateForGuestRegistration()` method
- **What to Show:**
  - Method signature
  - Error list creation
  - All validation rules (username, password, email, phone, NIC)
  - Error accumulation
  - ValidationException throwing
- **Save As:** `screenshots/design-patterns/11-strategy-register-validator.png`
- **Caption:** "RegisterValidator showing Strategy Pattern for validation rules"

#### Screenshot 12: ReservationValidator
- **File:** `src/main/java/com/oceanview/resort/validation/ReservationValidator.java`
- **Lines to Capture:** Entire `validateReservation()` method
- **What to Show:**
  - Date validation logic
  - Temporal constraint checking
  - Capacity validation
  - Status validation
- **Save As:** `screenshots/design-patterns/12-strategy-reservation-validator.png`
- **Caption:** "ReservationValidator enforcing date and capacity constraints"

---

### ✅ 2.6 Dependency Injection Pattern

#### Screenshot 13: Service Constructor Injection
- **File:** `src/main/java/com/oceanview/resort/service/ReservationService.java`
- **Lines to Capture:** Lines 8-17 (field and constructors)
- **What to Show:**
  - Private final field
  - Default constructor
  - Parameterized constructor for DI
- **Save As:** `screenshots/design-patterns/13-dependency-injection-constructor.png`
- **Caption:** "Constructor-based Dependency Injection in ReservationService"

---

### ✅ 2.7 Filter Chain Pattern

#### Screenshot 14: AuthFilter
- **File:** `src/main/java/com/oceanview/resort/filter/AuthFilter.java`
- **Lines to Capture:** doFilter method (approximately lines 12-35)
- **What to Show:**
  - @WebFilter annotation
  - doFilter method signature
  - Path checking logic
  - Session validation
  - chain.doFilter() call
  - Redirect logic
- **Save As:** `screenshots/design-patterns/14-filter-chain-auth.png`
- **Caption:** "AuthFilter implementing Filter Chain Pattern for authentication"

#### Screenshot 15: RoleFilter
- **File:** `src/main/java/com/oceanview/resort/filter/RoleFilter.java`
- **Lines to Capture:** doFilter and isRoleAllowed methods
- **What to Show:**
  - @WebFilter with URL patterns
  - doFilter method
  - isRoleAllowed method
  - Role-based authorization logic
- **Save As:** `screenshots/design-patterns/15-filter-chain-role.png`
- **Caption:** "RoleFilter enforcing role-based access control"

---

### ✅ 2.8 Exception Translation Pattern

#### Screenshot 16: DataAccessException
- **File:** `src/main/java/com/oceanview/resort/exception/DataAccessException.java`
- **Lines to Capture:** 1-15 (entire class)
- **What to Show:**
  - Class extending RuntimeException
  - Both constructors
- **Save As:** `screenshots/design-patterns/16-exception-translation-data.png`
- **Caption:** "DataAccessException for translating database exceptions"

#### Screenshot 17: ValidationException
- **File:** `src/main/java/com/oceanview/resort/exception/ValidationException.java`
- **Lines to Capture:** Entire class
- **What to Show:**
  - List<String> errors field
  - Both constructors
  - getErrors() method
  - getMessage() override
- **Save As:** `screenshots/design-patterns/17-exception-translation-validation.png`
- **Caption:** "ValidationException supporting multiple error messages"

#### Screenshot 18: Exception Translation in DAO
- **File:** `src/main/java/com/oceanview/resort/dao/impl/ReservationJdbcDAO.java`
- **Lines to Capture:** Lines showing try-catch with SQLException translation
- **What to Show:**
  - try-catch block
  - SQLException catching
  - DataAccessException throwing with message
- **Save As:** `screenshots/design-patterns/18-exception-translation-usage.png`
- **Caption:** "Exception translation in action - SQLException to DataAccessException"

---

## Section 3: Three-Tier Architecture

### ✅ 3.1 Presentation Layer

#### Screenshot 19: Servlet as Presentation Controller
- **File:** `src/main/java/com/oceanview/resort/controller/guest/GuestReservationServlet.java`
- **Lines to Capture:** doPost method showing layer delegation
- **What to Show:**
  - Request parameter extraction
  - Service layer calls
  - DAO layer calls
  - View forwarding/redirecting
- **Save As:** `screenshots/architecture/19-presentation-layer-servlet.png`
- **Caption:** "Presentation Layer: Servlet coordinating request handling"

#### Screenshot 20: JSP View Fragment
- **File:** `src/main/webapp/WEB-INF/views/fragments/nav.jspf`
- **Lines to Capture:** Role-based navigation code
- **What to Show:**
  - JSTL tags
  - Role checking logic
  - Dynamic menu rendering
- **Save As:** `screenshots/architecture/20-presentation-layer-jsp.png`
- **Caption:** "Presentation Layer: Role-based navigation in JSP"

---

### ✅ 3.2 Business Logic Layer

#### Screenshot 21: Service Coordination
- **File:** `src/main/java/com/oceanview/resort/service/ReservationService.java`
- **Lines to Capture:** Full class showing business logic
- **Save As:** `screenshots/architecture/21-business-layer-service.png`
- **Caption:** "Business Logic Layer: Service implementing domain rules"

---

### ✅ 3.3 Data Access Layer

#### Screenshot 22: Complex Query in DAO
- **File:** `src/main/java/com/oceanview/resort/dao/impl/ReservationJdbcDAO.java`
- **Lines to Capture:** `existsOverlappingReservation()` method
- **What to Show:**
  - Complex SQL query
  - Parameter binding
  - Overlap detection logic
- **Save As:** `screenshots/architecture/22-data-layer-complex-query.png`
- **Caption:** "Data Access Layer: Complex overlap detection query"

#### Screenshot 23: DBConnection Utility
- **File:** `src/main/java/com/oceanview/resort/util/DBConnection.java`
- **Lines to Capture:** Entire class
- **What to Show:**
  - Static initialization
  - Properties loading
  - getConnection() method
- **Save As:** `screenshots/architecture/23-data-layer-connection.png`
- **Caption:** "Data Access Layer: Centralized database connection management"

---

## Section 4: Validation Mechanisms

### ✅ 4.1 Client-Side Validation

#### Screenshot 24: JavaScript Validation
- **File:** `src/main/webapp/assets/js/validation.js`
- **Lines to Capture:** validateRegistrationForm function (if exists) or validation logic
- **What to Show:**
  - Form validation function
  - Field checking logic
  - Error message display
- **Save As:** `screenshots/validation/24-client-side-javascript.png`
- **Caption:** "Client-Side Validation: JavaScript form validation"

#### Screenshot 25: HTML5 Validation Attributes
- **File:** `src/main/webapp/WEB-INF/views/common/register.jsp`
- **Lines to Capture:** Input fields with validation attributes
- **What to Show:**
  - required attributes
  - pattern attributes
  - type="email", type="date" etc.
  - minlength/maxlength
- **Save As:** `screenshots/validation/25-client-side-html5.png`
- **Caption:** "Client-Side Validation: HTML5 validation attributes"

---

### ✅ 4.2 Server-Side Validation

#### Screenshot 26: Comprehensive Validator
- **File:** `src/main/java/com/oceanview/resort/validation/ReservationValidator.java`
- **Lines to Capture:** Complete validateReservation method
- **Save As:** `screenshots/validation/26-server-side-validator.png`
- **Caption:** "Server-Side Validation: Comprehensive business rule enforcement"

---

### ✅ 4.3 Database-Level Validation

#### Screenshot 27: CHECK Constraints - Reservations
- **File:** `sql/schema.sql`
- **Lines to Capture:** Reservations table CREATE statement with constraints
- **What to Show:**
  - CHECK constraint for date range
  - CHECK constraint for adults > 0
  - CHECK constraint for children >= 0
  - CHECK constraint for status enumeration
- **Save As:** `screenshots/validation/27-database-check-constraints-reservations.png`
- **Caption:** "Database Validation: CHECK constraints in reservations table"

#### Screenshot 28: CHECK Constraints - Bills
- **File:** `sql/schema.sql`
- **Lines to Capture:** Bills table CREATE statement with constraints
- **What to Show:**
  - CHECK constraints for non-negative amounts
  - CHECK constraint for payment status
- **Save As:** `screenshots/validation/28-database-check-constraints-bills.png`
- **Caption:** "Database Validation: Financial constraints in bills table"

#### Screenshot 29: UNIQUE Constraints
- **File:** `sql/schema.sql`
- **Lines to Capture:** UNIQUE key definitions across tables
- **What to Show:**
  - UNIQUE username
  - UNIQUE email
  - UNIQUE nic_passport
  - UNIQUE room_number
- **Save As:** `screenshots/validation/29-database-unique-constraints.png`
- **Caption:** "Database Validation: UNIQUE constraints preventing duplicates"

---

## Section 5: Database Implementation

### ✅ 5.2 Advanced Database Features

#### Screenshot 30: Foreign Key Constraints with Actions
- **File:** `sql/schema.sql`
- **Lines to Capture:** Foreign key definitions
- **What to Show:**
  - FK with ON DELETE CASCADE
  - FK with ON DELETE RESTRICT
  - FK with ON DELETE SET NULL
  - FK with ON UPDATE CASCADE
- **Save As:** `screenshots/database/30-foreign-key-constraints.png`
- **Caption:** "Foreign key constraints with referential actions"

#### Screenshot 31: Index Definitions
- **File:** `sql/schema.sql`
- **Lines to Capture:** KEY and INDEX definitions
- **What to Show:**
  - Primary keys
  - Composite indexes
  - Performance indexes
- **Save As:** `screenshots/database/31-index-definitions.png`
- **Caption:** "Database indexes for query optimization"

#### Screenshot 32: Users Table Schema
- **File:** `sql/schema.sql`
- **Lines to Capture:** Complete users table CREATE statement
- **What to Show:**
  - Column definitions
  - Data types
  - DEFAULT values
  - Constraints
  - Auto timestamps
- **Save As:** `screenshots/database/32-schema-users-table.png`
- **Caption:** "Users table schema with audit timestamps"

#### Screenshot 33: Reservations Table Schema
- **File:** `sql/schema.sql`
- **Lines to Capture:** Complete reservations table CREATE statement
- **Save As:** `screenshots/database/33-schema-reservations-table.png`
- **Caption:** "Reservations table with temporal and capacity constraints"

---

## Section 6: Reports for Decision-Making

### ✅ 6.1 Report API Implementation

#### Screenshot 34: ReportApiServlet - Endpoint Routing
- **File:** `src/main/java/com/oceanview/resort/api/ReportApiServlet.java`
- **Lines to Capture:** doGet method with switch-case routing
- **What to Show:**
  - @WebServlet annotation
  - doGet method
  - Path-based routing
  - Authentication check
  - Report method calls
- **Save As:** `screenshots/reports/34-report-api-routing.png`
- **Caption:** "RESTful API routing for report endpoints"

#### Screenshot 35: Report Data Generation
- **File:** `src/main/java/com/oceanview/resort/api/ReportApiServlet.java`
- **Lines to Capture:** One complete report method (e.g., usersByRoleData or revenueTrendData)
- **What to Show:**
  - Data retrieval from DAO
  - Aggregation logic
  - ChartData creation
  - Return statement
- **Save As:** `screenshots/reports/35-report-data-generation.png`
- **Caption:** "Report data generation and aggregation logic"

#### Screenshot 36: Chart.js Integration
- **File:** `src/main/webapp/assets/js/charts.js`
- **Lines to Capture:** loadChart function
- **What to Show:**
  - Fetch API call
  - JSON parsing
  - Chart.js initialization
  - Error handling
- **Save As:** `screenshots/reports/36-chartjs-integration.png`
- **Caption:** "Client-side chart rendering with Chart.js"

---

## Section 7: User Interface

### ✅ 7.1 UI Screenshots (Browser - Running Application)

#### Screenshot 37: Admin Dashboard
- **How:** Start application, login as admin, navigate to dashboard
- **What to Capture:**
  - Full browser window showing dashboard
  - Multiple charts visible
  - Navigation menu
  - User info in header
- **Save As:** `screenshots/ui/37-admin-dashboard.png`
- **Caption:** "Admin Dashboard with interactive charts and reports"

#### Screenshot 38: Staff Dashboard
- **How:** Login as staff member
- **Save As:** `screenshots/ui/38-staff-dashboard.png`
- **Caption:** "Staff Dashboard showing operational metrics"

#### Screenshot 39: Guest Dashboard
- **How:** Login as guest
- **Save As:** `screenshots/ui/39-guest-dashboard.png`
- **Caption:** "Guest Dashboard with personalized information"

#### Screenshot 40: Login Page
- **How:** Navigate to /login
- **What to Capture:**
  - Login form
  - Register link
  - Clean layout
- **Save As:** `screenshots/ui/40-login-page.png`
- **Caption:** "Login page with form validation"

#### Screenshot 41: Manage Reservations (Staff View)
- **How:** Login as staff, go to reservations
- **What to Capture:**
  - Data table with reservations
  - Action buttons
  - Status badges
  - Filter options
- **Save As:** `screenshots/ui/41-manage-reservations.png`
- **Caption:** "Staff reservation management interface"

#### Screenshot 42: Create Reservation Form
- **How:** Click "Create Reservation" button
- **What to Capture:**
  - Form with all fields
  - Date pickers
  - Dropdown selects
  - Validation messages (if any)
- **Save As:** `screenshots/ui/42-create-reservation-form.png`
- **Caption:** "Reservation creation form with validation"

#### Screenshot 43: Chart Visualization
- **How:** Dashboard with visible chart
- **What to Capture:**
  - Close-up of one chart (revenue trend or occupancy)
  - Legend
  - Data labels
- **Save As:** `screenshots/ui/43-chart-visualization.png`
- **Caption:** "Interactive chart visualization using Chart.js"

---

### ✅ 7.2 Advanced Features

#### Screenshot 44: Responsive Navigation
- **File:** `src/main/webapp/WEB-INF/views/fragments/nav.jspf`
- **Lines to Capture:** Complete navigation code
- **Save As:** `screenshots/ui/44-responsive-navigation.png`
- **Caption:** "Role-based responsive navigation implementation"

#### Screenshot 45: CSS Responsive Design
- **File:** `src/main/webapp/assets/css/app.css`
- **Lines to Capture:** Media queries and responsive styles
- **Save As:** `screenshots/ui/45-responsive-css.png`
- **Caption:** "CSS media queries for responsive design"

---

## Section 8: Web Services

#### Screenshot 46: RESTful API Endpoint
- **File:** `src/main/java/com/oceanview/resort/api/ReportApiServlet.java`
- **Lines to Capture:** Complete doGet method showing RESTful principles
- **Save As:** `screenshots/webservices/46-restful-api-endpoint.png`
- **Caption:** "RESTful API implementation following REST principles"

#### Screenshot 47: JSON Response Structure
- **File:** `src/main/java/com/oceanview/resort/api/ReportApiServlet.java`
- **Lines to Capture:** ChartData class or JSON writing method
- **Save As:** `screenshots/webservices/47-json-response.png`
- **Caption:** "JSON response structure for API endpoints"

#### Screenshot 48: Browser DevTools - Network Tab
- **How:** 
  1. Open dashboard in browser
  2. Open DevTools (F12)
  3. Go to Network tab
  4. Refresh page to see API calls
  5. Click on an API request (e.g., /api/reports/revenue)
  6. Show request and response
- **Save As:** `screenshots/webservices/48-api-network-request.png`
- **Caption:** "API call in browser Network tab showing JSON response"

---

## Section 9: Session and Cookie Management

#### Screenshot 49: Session Creation on Login
- **File:** `src/main/java/com/oceanview/resort/controller/AuthServlet.java`
- **Lines to Capture:** Login method with session creation
- **What to Show:**
  - getSession(true) call
  - setAttribute for user
  - setMaxInactiveInterval
- **Save As:** `screenshots/session/49-session-creation.png`
- **Caption:** "Session creation and user authentication"

#### Screenshot 50: Session Validation in Filter
- **File:** `src/main/java/com/oceanview/resort/filter/AuthFilter.java`
- **Lines to Capture:** getSessionUser method
- **What to Show:**
  - getSession(false) call
  - getAttribute
  - Null checking
- **Save As:** `screenshots/session/50-session-validation.png`
- **Caption:** "Session validation in authentication filter"

#### Screenshot 51: Session Invalidation on Logout
- **File:** `src/main/java/com/oceanview/resort/controller/AuthServlet.java`
- **Lines to Capture:** performLogout method
- **What to Show:**
  - session.invalidate() call
  - Redirect after logout
- **Save As:** `screenshots/session/51-session-invalidation.png`
- **Caption:** "Secure session invalidation on logout"

#### Screenshot 52: Browser DevTools - Cookies
- **How:**
  1. Login to application
  2. Open DevTools (F12)
  3. Go to Application tab (Chrome) or Storage tab (Firefox)
  4. Click on Cookies
  5. Show JSESSIONID cookie with HttpOnly flag
- **Save As:** `screenshots/session/52-session-cookie.png`
- **Caption:** "Session cookie (JSESSIONID) with HttpOnly flag in browser"

#### Screenshot 53: Session Storage in Browser
- **How:**
  1. Same as above, Application tab
  2. Show Session Storage or Application data
- **Save As:** `screenshots/session/53-session-storage.png`
- **Caption:** "Session data in browser developer tools"

---

## BONUS Screenshots (Extra Impact) ⭐

#### Screenshot 54: Web.xml Configuration
- **File:** `src/main/webapp/WEB-INF/web.xml`
- **Lines to Capture:** Session configuration
- **Save As:** `screenshots/bonus/54-web-xml-session-config.png`

#### Screenshot 55: Database Connection Properties
- **File:** `src/main/resources/db.properties`
- **Save As:** `screenshots/bonus/55-db-properties.png`

#### Screenshot 56: Password Hashing Utility
- **File:** `src/main/java/com/oceanview/resort/util/PasswordUtil.java`
- **Save As:** `screenshots/bonus/56-password-util.png`

#### Screenshot 57: Error Handling in JSP
- **File:** `src/main/webapp/WEB-INF/views/common/error.jsp`
- **Save As:** `screenshots/bonus/57-error-page.png`

#### Screenshot 58: Validation Error Display
- **How:** Submit form with invalid data, capture error messages
- **Save As:** `screenshots/bonus/58-validation-errors-display.png`

---

## 📊 SUMMARY

### Total Screenshots Needed:

| Category | Must Have (✅) | Recommended (🔹) | Bonus (⭐) | Total |
|----------|----------------|------------------|------------|-------|
| **Design Patterns** | 18 | 2 | - | 20 |
| **Architecture** | 5 | 1 | - | 6 |
| **Validation** | 6 | - | 2 | 8 |
| **Database** | 4 | - | 1 | 5 |
| **Reports** | 3 | - | - | 3 |
| **UI** | 9 | - | 2 | 11 |
| **Web Services** | 3 | - | - | 3 |
| **Session Management** | 5 | - | - | 5 |
| **TOTAL** | **53** | **3** | **5** | **61** |

### Minimum for Strong Submission:
- **All 53 "Must Have" screenshots** ✅
- **At least 20 from each major section**

### For Maximum Impact:
- **All 61 screenshots including bonus**

---

## 🎯 Priority Order (If Time Limited)

### HIGH PRIORITY (Take These First - 25 screenshots):

1. All 8 Design Pattern screenshots (#1, 4, 7, 9, 11, 14, 16, 17)
2. Architecture layers (#19, 21, 22)
3. Database constraints (#27, 28, 30, 32, 33)
4. UI screenshots (#37, 38, 39, 40, 41)
5. Reports API (#34, 35)
6. Session management (#49, 50, 51)
7. Web services (#46, 48)

### MEDIUM PRIORITY (Add These - 20 screenshots):

8. Additional design pattern examples (#2, 3, 5, 6, 8, 12, 13, 15, 18)
9. Validation screenshots (#24, 25, 26, 29)
10. More database (#31)
11. Chart visualization (#36, 43)
12. Additional UI (#42)
13. Cookie screenshot (#52)

### LOW PRIORITY (Nice to Have - 16 screenshots):

14. Remaining screenshots and all bonus items

---

## 📁 Folder Structure to Create

```
docs/
└── screenshots/
    ├── design-patterns/        (Screenshots 1-18)
    ├── architecture/           (Screenshots 19-23)
    ├── validation/            (Screenshots 24-29)
    ├── database/              (Screenshots 30-33)
    ├── reports/               (Screenshots 34-36)
    ├── ui/                    (Screenshots 37-45)
    ├── webservices/           (Screenshots 46-48)
    ├── session/               (Screenshots 49-53)
    └── bonus/                 (Screenshots 54-58)
```

---

## ✅ Quick Start Action Plan

1. **Create folders** (5 minutes)
2. **Install Polacode or CodeSnap** in VS Code (5 minutes)
3. **Take HIGH PRIORITY screenshots** (60 minutes)
4. **Start application and take UI screenshots** (30 minutes)
5. **Take MEDIUM PRIORITY screenshots** (45 minutes)
6. **Review and organize** (15 minutes)

**Total Time: ~2.5 hours for complete documentation**

---

Good luck! This checklist ensures you have comprehensive visual evidence for every claim in your Task B documentation.
