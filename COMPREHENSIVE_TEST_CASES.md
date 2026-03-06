# Ocean View Resort - Comprehensive Test Cases

## Test Case Overview
This document contains **40+ test cases** covering all major functionalities of the Ocean View Resort Management System.

---

## 1. Authentication & User Management Test Cases

| Test ID | Test Scenario | Test Steps | Test Data | Expected Result | Test Type | Priority |
|---------|---------------|------------|-----------|-----------------|-----------|----------|
| TC-AUTH-001 | Valid user login with ADMIN role | 1. Navigate to login page<br>2. Enter valid admin credentials<br>3. Click Login | Username: admin<br>Password: Admin@123 | Login successful, redirect to /admin/home | Functional | High |
| TC-AUTH-002 | Valid user login with STAFF role | 1. Navigate to login page<br>2. Enter valid staff credentials<br>3. Click Login | Username: staff1<br>Password: Staff@123 | Login successful, redirect to /staff/home | Functional | High |
| TC-AUTH-003 | Valid user login with GUEST role | 1. Navigate to login page<br>2. Enter valid guest credentials<br>3. Click Login | Username: john_guest<br>Password: Guest@123 | Login successful, redirect to /guest/home | Functional | High |
| TC-AUTH-004 | Invalid username login attempt | 1. Navigate to login page<br>2. Enter invalid username<br>3. Click Login | Username: invaliduser<br>Password: Test@123 | Login failed, error message displayed | Negative | High |
| TC-AUTH-005 | Invalid password login attempt | 1. Navigate to login page<br>2. Enter valid username with wrong password<br>3. Click Login | Username: admin<br>Password: wrongpass | Login failed, error message displayed | Negative | High |
| TC-AUTH-006 | Empty credentials login attempt | 1. Navigate to login page<br>2. Leave fields empty<br>3. Click Login | Username: (empty)<br>Password: (empty) | Validation error, fields required | Negative | Medium |
| TC-AUTH-007 | User logout functionality | 1. Login as any user<br>2. Click Logout button | Valid user session | Session cleared, redirect to login page | Functional | High |
| TC-AUTH-008 | Access control - Guest accessing Admin page | 1. Login as GUEST<br>2. Try to access /admin/users URL directly | Guest credentials | Access denied, redirect to login or error page | Security | High |

---

## 2. User Registration Test Cases

| Test ID | Test Scenario | Test Steps | Test Data | Expected Result | Test Type | Priority |
|---------|---------------|------------|-----------|-----------------|-----------|----------|
| TC-REG-001 | Valid guest registration with all required fields | 1. Navigate to /register<br>2. Fill all required fields<br>3. Submit form | Username: newguest1<br>Password: Guest@2026<br>Confirm: Guest@2026<br>Full Name: John Doe<br>Email: john@test.com<br>Phone: +1234567890<br>NIC: AB123456 | Registration successful, account created with GUEST role | Functional | High |
| TC-REG-002 | Registration with duplicate username | 1. Navigate to /register<br>2. Enter existing username<br>3. Submit form | Username: admin (existing)<br>Other fields: valid data | Registration failed, "Username already exists" error | Negative | High |
| TC-REG-003 | Registration with duplicate email | 1. Navigate to /register<br>2. Enter existing email<br>3. Submit form | Email: existing@test.com<br>Other fields: valid data | Registration failed, "Email already exists" error | Negative | High |
| TC-REG-004 | Registration with weak password | 1. Navigate to /register<br>2. Enter password without uppercase/digit<br>3. Submit form | Password: weakpassword<br>Other fields: valid data | Validation error, "Password must include uppercase, lowercase, and a digit" | Negative | High |
| TC-REG-005 | Registration with password mismatch | 1. Navigate to /register<br>2. Enter different passwords in confirm field<br>3. Submit form | Password: Guest@2026<br>Confirm: Guest@2027 | Validation error, "Password and confirmation do not match" | Negative | Medium |
| TC-REG-006 | Registration with invalid email format | 1. Navigate to /register<br>2. Enter invalid email<br>3. Submit form | Email: invalidemail.com<br>Other fields: valid data | Validation error, "Email format is invalid" | Negative | Medium |
| TC-REG-007 | Registration with short username | 1. Navigate to /register<br>2. Enter username less than 4 characters<br>3. Submit form | Username: abc<br>Other fields: valid data | Validation error, "Username must be 4-50 chars" | Negative | Low |
| TC-REG-008 | Registration with invalid phone format | 1. Navigate to /register<br>2. Enter invalid phone number<br>3. Submit form | Phone: abc123<br>Other fields: valid data | Validation error, "Phone format is invalid" | Negative | Medium |

---

## 3. Room Type Management Test Cases (Admin)

| Test ID | Test Scenario | Test Steps | Test Data | Expected Result | Test Type | Priority |
|---------|---------------|------------|-----------|-----------------|-----------|----------|
| TC-RT-001 | Create new room type with valid data | 1. Login as ADMIN<br>2. Navigate to /admin/roomtypes<br>3. Click Add Room Type<br>4. Fill form and submit | Name: Presidential Suite<br>Base Price: 500.00<br>Capacity: 4<br>Description: Luxury suite | Room type created successfully, appears in list | Functional | High |
| TC-RT-002 | Create room type with negative price | 1. Login as ADMIN<br>2. Navigate to /admin/roomtypes<br>3. Enter negative base price<br>4. Submit | Base Price: -100.00<br>Other fields: valid | Validation error, "Base price cannot be negative" | Negative | High |
| TC-RT-003 | Create room type exceeding max price | 1. Login as ADMIN<br>2. Navigate to /admin/roomtypes<br>3. Enter price > $100,000<br>4. Submit | Base Price: 150000.00<br>Other fields: valid | Validation error, "Base price cannot exceed $100,000" | Negative | Medium |
| TC-RT-004 | Create room type with invalid capacity | 1. Login as ADMIN<br>2. Navigate to /admin/roomtypes<br>3. Enter capacity = 0<br>4. Submit | Capacity: 0<br>Other fields: valid | Validation error, "Capacity must be between 1 and 50" | Negative | High |
| TC-RT-005 | Update existing room type | 1. Login as ADMIN<br>2. Select existing room type<br>3. Modify fields<br>4. Submit update | Name: Deluxe Ocean View<br>Base Price: 350.00 | Room type updated successfully | Functional | High |

---

## 4. Room Management Test Cases (Admin)

| Test ID | Test Scenario | Test Steps | Test Data | Expected Result | Test Type | Priority |
|---------|---------------|------------|-----------|-----------------|-----------|----------|
| TC-ROOM-001 | Create new room with valid data | 1. Login as ADMIN<br>2. Navigate to /admin/rooms<br>3. Click Add Room<br>4. Fill form and submit | Room Number: 101<br>Room Type: Deluxe<br>Status: AVAILABLE<br>Floor: 1 | Room created successfully, status = AVAILABLE | Functional | High |
| TC-ROOM-002 | Create room with duplicate room number | 1. Login as ADMIN<br>2. Enter existing room number<br>3. Submit | Room Number: 101 (existing)<br>Other fields: valid | Validation error, "Room number already exists" | Negative | High |
| TC-ROOM-003 | Create room with invalid room number | 1. Login as ADMIN<br>2. Enter special characters in room number<br>3. Submit | Room Number: 101@#$<br>Other fields: valid | Validation error, "Room number must contain only letters, numbers, or hyphens" | Negative | Medium |
| TC-ROOM-004 | Create room with invalid floor | 1. Login as ADMIN<br>2. Enter floor < -5 or > 200<br>3. Submit | Floor: 250<br>Other fields: valid | Validation error, "Floor must be between -5 and 200" | Negative | Medium |
| TC-ROOM-005 | Update room status to MAINTENANCE | 1. Login as ADMIN<br>2. Select room<br>3. Change status to MAINTENANCE<br>4. Submit | Status: MAINTENANCE | Room status updated, unavailable for booking | Functional | High |
| TC-ROOM-006 | Update room status to OCCUPIED | 1. Login as ADMIN<br>2. Select available room<br>3. Change status to OCCUPIED<br>4. Submit | Status: OCCUPIED | Room status updated successfully | Functional | Medium |

---

## 5. Reservation Management Test Cases (Staff/Guest)

| Test ID | Test Scenario | Test Steps | Test Data | Expected Result | Test Type | Priority |
|---------|---------------|------------|-----------|-----------------|-----------|----------|
| TC-RES-001 | Create valid reservation by Staff | 1. Login as STAFF<br>2. Navigate to /staff/reservations<br>3. Select guest, room, dates<br>4. Submit | Guest: John Doe<br>Room: 101<br>Check-in: Tomorrow<br>Check-out: +3 days<br>Adults: 2<br>Children: 1 | Reservation created, status = PENDING or CONFIRMED | Functional | High |
| TC-RES-002 | Create reservation by Guest (self-booking) | 1. Login as GUEST<br>2. Navigate to /guest/reservations<br>3. Select room and dates<br>4. Submit | Room: 102<br>Check-in: Tomorrow<br>Check-out: +2 days<br>Adults: 2 | Reservation created for logged-in guest | Functional | High |
| TC-RES-003 | Create reservation with check-out before check-in | 1. Login as STAFF<br>2. Enter check-out date before check-in<br>3. Submit | Check-in: 2026-03-10<br>Check-out: 2026-03-08 | Validation error, "Check-out date must be after check-in date" | Negative | High |
| TC-RES-004 | Create reservation with same check-in and check-out | 1. Login as STAFF<br>2. Enter same date for both<br>3. Submit | Check-in: 2026-03-10<br>Check-out: 2026-03-10 | Validation error, "Check-out date must be after check-in date" | Negative | High |
| TC-RES-005 | Create reservation with zero adults | 1. Login as STAFF<br>2. Set adults count to 0<br>3. Submit | Adults: 0<br>Other fields: valid | Validation error, "Adults must be at least 1" | Negative | High |
| TC-RES-006 | Create reservation with negative children | 1. Login as STAFF<br>2. Set children count to -1<br>3. Submit | Children: -1<br>Other fields: valid | Validation error, "Children cannot be negative" | Negative | Medium |
| TC-RES-007 | Create overlapping reservation for same room | 1. Login as STAFF<br>2. Book room with existing reservation<br>3. Use overlapping dates<br>4. Submit | Room: 101 (already booked)<br>Dates: Overlapping dates | Validation error, "Room not available for selected dates" | Negative | High |
| TC-RES-008 | Update reservation status to CONFIRMED | 1. Login as STAFF<br>2. Select PENDING reservation<br>3. Change status to CONFIRMED<br>4. Submit | Status: CONFIRMED | Reservation status updated successfully | Functional | High |
| TC-RES-009 | Update reservation status to CHECKED_IN | 1. Login as STAFF<br>2. Select CONFIRMED reservation<br>3. Change status to CHECKED_IN<br>4. Submit | Status: CHECKED_IN | Reservation status updated, room status may change to OCCUPIED | Functional | High |
| TC-RES-010 | Cancel existing reservation | 1. Login as STAFF or GUEST<br>2. Select active reservation<br>3. Change status to CANCELLED<br>4. Submit | Status: CANCELLED | Reservation cancelled, room becomes available | Functional | High |

---

## 6. Billing Management Test Cases (Staff)

| Test ID | Test Scenario | Test Steps | Test Data | Expected Result | Test Type | Priority |
|---------|---------------|------------|-----------|-----------------|-----------|----------|
| TC-BILL-001 | Generate bill for checked-out reservation | 1. Login as STAFF<br>2. Navigate to /staff/billing<br>3. Select reservation (CHECKED_OUT)<br>4. Enter billing details<br>5. Submit | Reservation ID: 1<br>Subtotal: 300.00<br>Tax: 30.00<br>Discount: 10.00<br>Payment Status: UNPAID | Bill created, Total = 320.00 (300+30-10) | Functional | High |
| TC-BILL-002 | Create bill with negative subtotal | 1. Login as STAFF<br>2. Enter negative subtotal<br>3. Submit | Subtotal: -100.00<br>Tax: 0<br>Discount: 0 | Validation error, "Subtotal, tax, and discount cannot be negative" | Negative | High |
| TC-BILL-003 | Create bill with discount exceeding subtotal + tax | 1. Login as STAFF<br>2. Enter discount > (subtotal + tax)<br>3. Submit | Subtotal: 100.00<br>Tax: 10.00<br>Discount: 120.00 | Validation error, "Discount is too high; total cannot be negative" | Negative | High |
| TC-BILL-004 | Create bill with invalid payment status | 1. Login as STAFF<br>2. Enter invalid payment status<br>3. Submit | Payment Status: COMPLETED (invalid) | Validation error, "Invalid payment status selected" | Negative | Medium |
| TC-BILL-005 | Update bill payment status to PAID | 1. Login as STAFF<br>2. Select UNPAID bill<br>3. Change status to PAID<br>4. Submit | Payment Status: PAID | Bill updated, payment status = PAID | Functional | High |
| TC-BILL-006 | View bill details by Guest | 1. Login as GUEST<br>2. Navigate to /guest/bills<br>3. View own bills | Guest user session | Guest can view only their own bills | Functional | Medium |

---

## 7. Dashboard & Reporting Test Cases

| Test ID | Test Scenario | Test Steps | Test Data | Expected Result | Test Type | Priority |
|---------|---------------|------------|-----------|-----------------|-----------|----------|
| TC-DASH-001 | Admin dashboard loads with statistics | 1. Login as ADMIN<br>2. Navigate to /admin/dashboard | Admin credentials | Dashboard displays: total users, rooms, reservations, revenue charts | Functional | High |
| TC-DASH-002 | Staff dashboard loads with today's reservations | 1. Login as STAFF<br>2. Navigate to /staff/dashboard | Staff credentials | Dashboard shows check-ins, check-outs, pending tasks | Functional | High |
| TC-DASH-003 | Guest dashboard loads with active reservations | 1. Login as GUEST<br>2. Navigate to /guest/dashboard | Guest credentials | Dashboard displays guest's active and upcoming reservations | Functional | High |
| TC-DASH-004 | Admin views all users list | 1. Login as ADMIN<br>2. Navigate to /admin/users | Admin credentials | Complete list of users with roles displayed | Functional | Medium |
| TC-DASH-005 | Generate revenue report via API | 1. Login as ADMIN/STAFF<br>2. Call /api/reports/revenue endpoint | Date range parameters | JSON response with revenue data by period | Functional | Medium |

---

## 8. Security & Authorization Test Cases

| Test ID | Test Scenario | Test Steps | Test Data | Expected Result | Test Type | Priority |
|---------|---------------|------------|-----------|-----------------|-----------|----------|
| TC-SEC-001 | Unauthenticated user accessing protected page | 1. Do not login<br>2. Try to access /admin/home directly | No session | Redirect to /login page | Security | High |
| TC-SEC-002 | GUEST user accessing Staff functionality | 1. Login as GUEST<br>2. Try to access /staff/billing URL | Guest credentials | Access denied, redirect or 403 error | Security | High |
| TC-SEC-003 | STAFF user accessing Admin functionality | 1. Login as STAFF<br>2. Try to access /admin/users URL | Staff credentials | Access denied, redirect or 403 error | Security | High |
| TC-SEC-004 | Session timeout after inactivity | 1. Login as any user<br>2. Wait for session timeout (30 min)<br>3. Try to access protected page | Valid credentials, expired session | Redirect to login, session expired message | Security | Medium |
| TC-SEC-005 | Password encryption verification | 1. Register new user<br>2. Check database users table | Password: Test@123 | Password stored as bcrypt hash, not plaintext | Security | High |

---

## 9. Data Validation & Integrity Test Cases

| Test ID | Test Scenario | Test Steps | Test Data | Expected Result | Test Type | Priority |
|---------|---------------|------------|-----------|-----------------|-----------|----------|
| TC-VAL-001 | SQL injection attempt in login | 1. Navigate to login<br>2. Enter SQL injection payload<br>3. Submit | Username: ' OR '1'='1<br>Password: anything | Login fails, no SQL injection occurs | Security | High |
| TC-VAL-002 | XSS attempt in user input fields | 1. Register or create resource<br>2. Enter script tags in text fields<br>3. Submit | Input: &lt;script&gt;alert('XSS')&lt;/script&gt; | Input sanitized or rejected, no script execution | Security | High |
| TC-VAL-003 | Database foreign key constraint validation | 1. Login as ADMIN<br>2. Try to delete room type with assigned rooms | Room Type ID: 1 (has rooms) | Delete fails, "Cannot delete, rooms exist" error | Negative | Medium |
| TC-VAL-004 | Concurrent reservation booking for same room | 1. Two users try to book same room<br>2. Same dates<br>3. Submit simultaneously | Room: 101<br>Dates: Same dates<br>Users: User1, User2 | Only one reservation succeeds, other fails with availability error | Negative | High |

---

## Test Summary

| Category | Total Test Cases | High Priority | Medium Priority | Low Priority |
|----------|------------------|---------------|-----------------|--------------|
| Authentication & User Management | 8 | 7 | 1 | 0 |
| User Registration | 8 | 5 | 3 | 1 |
| Room Type Management | 5 | 3 | 2 | 0 |
| Room Management | 6 | 4 | 2 | 0 |
| Reservation Management | 10 | 7 | 1 | 0 |
| Billing Management | 6 | 4 | 2 | 0 |
| Dashboard & Reporting | 5 | 3 | 2 | 0 |
| Security & Authorization | 5 | 4 | 1 | 0 |
| Data Validation & Integrity | 4 | 3 | 1 | 0 |
| **TOTAL** | **57** | **40** | **15** | **1** |

---

## Test Environment Requirements

| Component | Specification |
|-----------|---------------|
| **Application Server** | Apache Tomcat 10+ |
| **Database** | MySQL 8+ with oceanview_resort schema |
| **Java Version** | JDK 17 or JDK 21 |
| **Browser** | Chrome 90+, Firefox 88+, Edge 90+ |
| **Test Data** | Seed data loaded from seed.sql |
| **Network** | localhost:8080 or test server URL |

---

## Test Execution Guidelines

1. **Setup**: Ensure database is reset with schema.sql and seed.sql before each test cycle
2. **Order**: Execute test cases in order within each category
3. **Cleanup**: Clean test data after each test case to avoid interference
4. **Logging**: Record actual results, screenshots, and logs for failed tests
5. **Regression**: Re-run all high-priority tests after any code changes
6. **Automation**: Consider automating functional test cases using Selenium or similar tools

---

## Defect Severity Classification

| Severity | Description | Example |
|----------|-------------|---------|
| **Critical** | System crash, data loss, security breach | SQL injection successful, system inaccessible |
| **High** | Major feature not working, data corruption | Cannot create reservations, billing calculation wrong |
| **Medium** | Feature works with workaround, minor data issues | Validation message unclear, UI alignment issue |
| **Low** | Cosmetic issues, minor inconvenience | Button color incorrect, tooltip missing |

---

## Test Case Traceability Matrix

| Requirement | Related Test Cases |
|-------------|-------------------|
| User Authentication | TC-AUTH-001 to TC-AUTH-008 |
| Guest Registration | TC-REG-001 to TC-REG-008 |
| Room Management | TC-RT-001 to TC-RT-005, TC-ROOM-001 to TC-ROOM-006 |
| Reservation Booking | TC-RES-001 to TC-RES-010 |
| Billing & Payments | TC-BILL-001 to TC-BILL-006 |
| Role-Based Access Control | TC-SEC-001 to TC-SEC-003 |
| Data Security | TC-SEC-005, TC-VAL-001, TC-VAL-002 |
| Business Logic Validation | TC-VAL-003, TC-VAL-004 |

---

## Notes

- All test cases assume the system is deployed at `http://localhost:8080/Ocean.View_Resort/`
- Test data should be prepared according to seed.sql or created during test execution
- Failed test cases should be logged with screenshots and error messages
- Security test cases (TC-SEC-*, TC-VAL-*) are critical and must pass before production deployment
- Performance testing is not covered in this document but should be performed separately

---

## 10. User Interface (UI) Reference Guide

This section provides a comprehensive overview of all user interfaces in the Ocean View Resort system, organized by role and functionality.

### 10.1 Common Interfaces (Public Access)

| Interface Name | URL Path | Purpose | Key Components | User Actions | Screenshot Notes |
|----------------|----------|---------|----------------|--------------|------------------|
| **Landing Page** | `/index.jsp` | System entry point and welcome page | • Welcome message<br>• DB health link<br>• Login button<br>• Register button | • Navigate to Login<br>• Navigate to Register<br>• Check DB health | Capture full page with buttons visible |
| **Login Page** | `/login` | User authentication | • Username input field<br>• Password input field<br>• Show/Hide password toggle<br>• Remember username checkbox<br>• Login button<br>• Clear button<br>• Register link | • Enter credentials<br>• Toggle password visibility<br>• Check remember username<br>• Submit login<br>• Clear form<br>• Navigate to register | Capture empty form and validation states |
| **Registration Page** | `/register` | New guest account creation | • Username field<br>• Password field<br>• Confirm password field<br>• Full name field<br>• Email field<br>• Phone field<br>• NIC/Passport field<br>• Address field<br>• Register button<br>• Clear button | • Fill registration form<br>• Validate inputs<br>• Submit registration<br>• Clear form | Capture form with all fields and validation messages |
| **Error Page** | `/WEB-INF/views/common/error.jsp` | Display error messages | • Error title<br>• Error message<br>• Back button | • View error details<br>• Navigate back | Capture with sample error message |

---

### 10.2 Admin Interfaces

| Interface Name | URL Path | Purpose | Key Components | User Actions | Screenshot Notes |
|----------------|----------|---------|----------------|--------------|------------------|
| **Admin Home** | `/admin/home` | Admin landing page and navigation hub | • Welcome message with username<br>• System Management card<br>• Dashboard link<br>• User Management link<br>• Room Management card<br>• Rooms link<br>• Room Types link<br>• Quick Overview card | • Navigate to Dashboard<br>• Navigate to User Management<br>• Navigate to Room Management<br>• Navigate to Room Types | Capture full page with all cards visible |
| **Admin Dashboard** | `/admin/dashboard` | System analytics and statistics | • Page header<br>• 4 Stat cards:<br>&nbsp;&nbsp;- Total Users<br>&nbsp;&nbsp;- Total Rooms<br>&nbsp;&nbsp;- Total Reservations<br>&nbsp;&nbsp;- Total Revenue (highlighted)<br>• Charts:<br>&nbsp;&nbsp;- Users by Role (Bar chart)<br>&nbsp;&nbsp;- Rooms by Status (Doughnut)<br>&nbsp;&nbsp;- Revenue Trend (Line chart)<br>&nbsp;&nbsp;- Reservation Trend (Line chart) | • View statistics<br>• Analyze charts<br>• Monitor system health | Capture with all stats and charts loaded |
| **Manage Users** | `/admin/users` | User account management (CRUD) | • Add New User form:<br>&nbsp;&nbsp;- Username input<br>&nbsp;&nbsp;- Password input<br>&nbsp;&nbsp;- Role dropdown (ADMIN/STAFF/GUEST)<br>&nbsp;&nbsp;- Status dropdown (ACTIVE/INACTIVE/LOCKED)<br>&nbsp;&nbsp;- Create button<br>• Edit User form (conditional)<br>• Filter toolbar with search<br>• Users table:<br>&nbsp;&nbsp;- ID, Username, Role, Status columns<br>&nbsp;&nbsp;- Edit button<br>&nbsp;&nbsp;- Deactivate button<br>• Success/Error notices | • Create new user<br>• Edit existing user<br>• Deactivate user<br>• Filter users<br>• Clear filter | Capture table with sample data |
| **Manage Room Types** | `/admin/roomtypes` | Room type configuration (CRUD) | • Add New Room Type form:<br>&nbsp;&nbsp;- Name input (max 100 chars)<br>&nbsp;&nbsp;- Base Price input ($0-$100,000)<br>&nbsp;&nbsp;- Capacity input (1-50 guests)<br>&nbsp;&nbsp;- Description input (optional, max 500 chars)<br>&nbsp;&nbsp;- Create button<br>• Edit Room Type form (conditional)<br>• Filter toolbar<br>• Room Types table:<br>&nbsp;&nbsp;- ID, Name, Base Price, Capacity, Description<br>&nbsp;&nbsp;- Edit/Delete buttons | • Create room type<br>• Edit room type<br>• Delete room type<br>• Filter room types<br>• View pricing and capacity | Capture form and table |
| **Manage Rooms** | `/admin/rooms` | Room inventory management (CRUD) | • Add New Room form:<br>&nbsp;&nbsp;- Room Number input (pattern: [A-Za-z0-9-]{1,20})<br>&nbsp;&nbsp;- Room Type dropdown<br>&nbsp;&nbsp;- Status dropdown (AVAILABLE/OCCUPIED/MAINTENANCE/INACTIVE)<br>&nbsp;&nbsp;- Floor input (-5 to 200)<br>&nbsp;&nbsp;- Create button<br>• Edit Room form (conditional)<br>• Filter toolbar<br>• Rooms table:<br>&nbsp;&nbsp;- ID, Room Number, Room Type, Status, Floor<br>&nbsp;&nbsp;- Status badges (color-coded)<br>&nbsp;&nbsp;- Edit/Inactivate buttons | • Create room<br>• Edit room details<br>• Change room status<br>• Inactivate room<br>• Filter by status/floor | Capture table with different status badges |

---

### 10.3 Staff Interfaces

| Interface Name | URL Path | Purpose | Key Components | User Actions | Screenshot Notes |
|----------------|----------|---------|----------------|--------------|------------------|
| **Staff Home** | `/staff/home` | Staff landing page | • Welcome banner with username<br>• Staff Responsibilities info card<br>• Quick Actions cards:<br>&nbsp;&nbsp;- 📊 Dashboard<br>&nbsp;&nbsp;- 📅 Reservations<br>&nbsp;&nbsp;- 💳 Billing | • Navigate to Dashboard<br>• Navigate to Reservations<br>• Navigate to Billing | Capture with all quick action cards |
| **Staff Dashboard** | `/staff/dashboard` | Operations monitoring | • Page header<br>• 5 Stat cards:<br>&nbsp;&nbsp;- Total Reservations<br>&nbsp;&nbsp;- Checked In<br>&nbsp;&nbsp;- Checked Out<br>&nbsp;&nbsp;- Total Bills<br>&nbsp;&nbsp;- Total Billed (highlighted)<br>• Charts:<br>&nbsp;&nbsp;- Reservation Status (Bar chart)<br>&nbsp;&nbsp;- Billing Status (Doughnut chart) | • Monitor reservations<br>• Track check-ins/outs<br>• View billing stats<br>• Analyze charts | Capture with loaded data |
| **Staff Reservations** | `/staff/reservations` | Reservation management | • Create New Reservation form:<br>&nbsp;&nbsp;- Guest dropdown<br>&nbsp;&nbsp;- Room dropdown<br>&nbsp;&nbsp;- Check In date<br>&nbsp;&nbsp;- Check Out date<br>&nbsp;&nbsp;- Adults number (min 1)<br>&nbsp;&nbsp;- Children number (min 0)<br>&nbsp;&nbsp;- Status dropdown (CONFIRMED/PENDING/CANCELLED)<br>&nbsp;&nbsp;- Create button<br>• Edit Reservation form (conditional)<br>• Filter toolbar<br>• Reservations table:<br>&nbsp;&nbsp;- ID, Guest, Room, Check In, Check Out, Adults, Children, Status<br>&nbsp;&nbsp;- Action buttons: ✏️ Edit, ❌ Cancel, ✅ Check In, 🏁 Check Out<br>&nbsp;&nbsp;- Status badges | • Create reservation<br>• Edit reservation<br>• Cancel reservation<br>• Check in guest<br>• Check out guest<br>• Filter reservations | Capture table with action buttons visible |
| **Staff Billing** | `/staff/billing` | Bill generation and management | • Generate New Bill form:<br>&nbsp;&nbsp;- Reservation dropdown<br>&nbsp;&nbsp;- Subtotal input (0-1,000,000)<br>&nbsp;&nbsp;- Tax input (min 0)<br>&nbsp;&nbsp;- Discount input (min 0)<br>&nbsp;&nbsp;- Payment Status dropdown (UNPAID/PARTIAL/PAID/REFUNDED/VOID)<br>&nbsp;&nbsp;- Total Preview display<br>&nbsp;&nbsp;- Generate Bill button<br>• Edit Bill form (conditional)<br>• Filter toolbar<br>• Bills table:<br>&nbsp;&nbsp;- ID, Reservation, Subtotal, Tax, Discount, Total, Payment Status, Issued At<br>&nbsp;&nbsp;- Action buttons: ✏️ Edit, 🚫 Void | • Generate bill<br>• Edit bill<br>• Update payment status<br>• Void bill<br>• View total preview<br>• Filter bills | Capture form with total calculation |

---

### 10.4 Guest Interfaces

| Interface Name | URL Path | Purpose | Key Components | User Actions | Screenshot Notes |
|----------------|----------|---------|----------------|--------------|------------------|
| **Guest Home** | `/guest/home` | Guest landing page | • Welcome banner with username<br>• 🏖️ Ocean View Resort welcome message<br>• Quick Start Guide info card<br>• Quick Actions cards:<br>&nbsp;&nbsp;- 📊 Dashboard<br>&nbsp;&nbsp;- 📅 My Reservations<br>&nbsp;&nbsp;- 💳 My Bills | • Navigate to Dashboard<br>• Navigate to Reservations<br>• Navigate to Bills | Capture with personalized greeting |
| **Guest Dashboard** | `/guest/dashboard` | Guest activity overview | • Page header with personalized greeting<br>• 5 Stat cards:<br>&nbsp;&nbsp;- Total Reservations<br>&nbsp;&nbsp;- Checked In<br>&nbsp;&nbsp;- Checked Out<br>&nbsp;&nbsp;- Total Bills<br>&nbsp;&nbsp;- Total Amount (highlighted)<br>• Charts:<br>&nbsp;&nbsp;- My Reservation Status (Bar chart)<br>&nbsp;&nbsp;- My Billing Status (Doughnut chart) | • View personal stats<br>• Monitor reservations<br>• Check billing status<br>• Analyze activity | Capture with guest-specific data |
| **Guest Reservations** | `/guest/reservations` | Self-service booking | • Create New Reservation form:<br>&nbsp;&nbsp;- Room dropdown (with status)<br>&nbsp;&nbsp;- Check In date (cannot be past)<br>&nbsp;&nbsp;- Check Out date (must be after check-in)<br>&nbsp;&nbsp;- Adults input (1-20)<br>&nbsp;&nbsp;- Children input (0-20, max 30 total)<br>&nbsp;&nbsp;- Input hints below fields<br>&nbsp;&nbsp;- Create Reservation button<br>• Edit Reservation form (conditional)<br>• Filter toolbar<br>• My Reservations table:<br>&nbsp;&nbsp;- ID, Room, Check In, Check Out, Adults, Children, Status<br>&nbsp;&nbsp;- Action buttons: Edit, Cancel (only for PENDING/CONFIRMED)<br>&nbsp;&nbsp;- "No actions available" for other statuses | • Create reservation<br>• Edit reservation (if PENDING/CONFIRMED)<br>• Cancel reservation<br>• View reservation history<br>• Filter reservations | Capture form with validation hints |
| **Guest Bills** | `/guest/bills` | Billing history (Read-Only) | • Page header: 💳 My Bills<br>• Filter toolbar<br>• My Bills table (Read-only):<br>&nbsp;&nbsp;- Bill ID, Reservation, Subtotal, Tax, Discount, Total, Payment Status, Issued At<br>&nbsp;&nbsp;- Status badges<br>&nbsp;&nbsp;- No action buttons (view only) | • View bills<br>• Filter bills<br>• Check payment status | Capture table showing billing details |

---

### 10.5 Common UI Components

| Component | Description | Usage | Visual Characteristics |
|-----------|-------------|-------|------------------------|
| **Navigation Bar** | Role-based menu | Displayed on all authenticated pages | • Logo/Brand<br>• Role-specific menu items<br>• Username display<br>• Logout button |
| **Status Badges** | Visual status indicators | Used in tables for status fields | • Color-coded:<br>&nbsp;&nbsp;- Green: ACTIVE, AVAILABLE, CONFIRMED, PAID<br>&nbsp;&nbsp;- Blue: CHECKED_IN, PARTIAL<br>&nbsp;&nbsp;- Yellow: PENDING, MAINTENANCE<br>&nbsp;&nbsp;- Red: CANCELLED, UNPAID, LOCKED<br>&nbsp;&nbsp;- Gray: INACTIVE, VOID |
| **Notice Messages** | Feedback and alerts | Displayed after form submissions | • Success: Green background<br>• Error: Red background<br>• Info: Blue background |
| **Form Grid** | Responsive form layout | All CRUD forms | • Two-column grid on desktop<br>• Single column on mobile<br>• Label + input pairing |
| **Action Buttons** | CRUD operations | Tables and forms | • Primary: Blue<br>• Secondary: Gray<br>• Success: Green<br>• Danger: Red<br>• Icons (✏️ Edit, ❌ Cancel/Delete, ✅ Confirm, 🚫 Void) |
| **Filter Toolbar** | Search and filter functionality | Above data tables | • Search input<br>• Clear filter button<br>• Visible count display |
| **Data Tables** | Display lists of records | All list views | • Sticky header<br>• Striped rows<br>• Hover effects<br>• Responsive scrolling |
| **Charts** | Data visualization | Dashboard pages | • Chart.js library<br>• Bar, Line, Doughnut types<br>• Color-coded datasets<br>• Responsive |

---

### 10.6 UI Test Cases Matrix

| UI Component | Test Scenario | Expected UI Behavior | Validation Points |
|--------------|---------------|----------------------|-------------------|
| **Login Form** | Enter invalid credentials | Error notice displayed in red, fields retain values (except password) | • Error message visible<br>• Username retained<br>• Password cleared |
| **Registration Form** | Submit with validation errors | Multiple error messages displayed, problematic fields highlighted | • All errors listed<br>• Field-level highlighting<br>• Focus on first error |
| **Admin Dashboard Charts** | Load dashboard | All 4 charts render with data, stat cards show numbers | • Charts visible<br>• Data loaded<br>• No errors in console |
| **Status Badges** | View tables with various statuses | Color-coded badges display correctly | • Correct colors<br>• Readable text<br>• Consistent styling |
| **Filter Toolbar** | Type in search field | Table rows filter in real-time, count updates | • Instant filtering<br>• Count accurate<br>• Clear button works |
| **Responsive Forms** | Resize browser window | Form grid adapts from 2 columns to 1 column on mobile | • Layout shifts smoothly<br>• All fields accessible<br>• No overflow |
| **Action Buttons** | Hover over buttons | Color change on hover, cursor changes to pointer | • Visual feedback<br>• Pointer cursor<br>• Smooth transition |
| **Confirmation Dialogs** | Click delete/void button | Browser confirm dialog appears | • Confirm message shown<br>• Action cancellable<br>• Proceeds on OK |
| **Password Toggle** | Click Show/Hide button on login | Password visibility toggles, button text changes | • Text visible/hidden<br>• Button text updates<br>• No value loss |
| **Date Inputs** | Select dates in reservation form | Calendar picker appears, dates selectable | • Date picker functional<br>• Valid dates only<br>• Format correct |
| **Dropdown Selects** | Open room/guest/status dropdowns | Options list appears, selection updates | • All options visible<br>• Selected value updates<br>• Dropdown closes |
| **Edit Forms** | Click Edit button | Edit form appears with pre-filled values | • Values populated<br>• Form visible<br>• Cancel button available |
| **Navigation Links** | Click nav menu items | Navigate to correct page, active item highlighted | • URL changes<br>• Page loads<br>• Active state shown |
| **Remember Username** | Check remember checkbox and login | Username saved to localStorage, pre-filled on next visit | • Username retained<br>• Checkbox state saved<br>• Clearable |
| **Total Preview (Billing)** | Enter subtotal, tax, discount | Total preview updates in real-time: (subtotal + tax - discount) | • Calculation correct<br>• Updates immediately<br>• Format: $X.XX |

---

### 10.7 Screenshot Checklist for Documentation

Use this checklist when capturing screenshots for documentation or testing:

#### Common Pages
- [ ] Landing page (index.jsp) - full view
- [ ] Login page - empty state
- [ ] Login page - with error message
- [ ] Registration page - empty state
- [ ] Registration page - with validation errors

#### Admin Interface
- [ ] Admin Home - full view with all cards
- [ ] Admin Dashboard - with all stats and charts loaded
- [ ] Manage Users - create form + table with data
- [ ] Manage Users - edit mode
- [ ] Manage Room Types - create form + table
- [ ] Manage Rooms - create form + table with status badges

#### Staff Interface
- [ ] Staff Home - full view
- [ ] Staff Dashboard - with data loaded
- [ ] Staff Reservations - create form + table
- [ ] Staff Reservations - showing action buttons (Edit, Check In, Check Out, Cancel)
- [ ] Staff Billing - generate bill form with total preview
- [ ] Staff Billing - bills table

#### Guest Interface
- [ ] Guest Home - full view
- [ ] Guest Dashboard - with personal data
- [ ] Guest Reservations - create form with hints
- [ ] Guest Reservations - table showing editable and non-editable reservations
- [ ] Guest Bills - read-only table view

#### UI Components
- [ ] Navigation bar for each role (Admin, Staff, Guest)
- [ ] Status badges in different colors
- [ ] Success notice message
- [ ] Error notice message
- [ ] Filter toolbar in action
- [ ] Confirmation dialog
- [ ] Charts (bar, line, doughnut)
- [ ] Mobile responsive view (forms at < 768px width)

---

### 10.8 Browser Compatibility Matrix

| Browser | Minimum Version | Tested Features | Notes |
|---------|----------------|-----------------|-------|
| **Google Chrome** | 90+ | All UI components, Charts, Forms, Validation | Recommended browser |
| **Mozilla Firefox** | 88+ | All UI components, Charts, Forms, Validation | Fully supported |
| **Microsoft Edge** | 90+ | All UI components, Charts, Forms, Validation | Chromium-based, fully supported |
| **Safari** | 14+ | All UI components, Charts, Forms | Date picker styling may differ |
| **Mobile Chrome** | Latest | Responsive layouts, Touch interactions | Optimal mobile experience |
| **Mobile Safari** | Latest | Responsive layouts, Touch interactions | iOS compatibility |

---

### 10.9 Accessibility Features

| Feature | Implementation | WCAG Compliance |
|---------|---------------|-----------------|
| **Form Labels** | All inputs have associated `<label>` elements | WCAG 2.1 Level A |
| **ARIA Labels** | `aria-label` attributes on complex inputs | WCAG 2.1 Level AA |
| **Keyboard Navigation** | All interactive elements accessible via Tab key | WCAG 2.1 Level A |
| **Focus Indicators** | Visual focus outline on all focusable elements | WCAG 2.1 Level AA |
| **Color Contrast** | Status badges and text meet 4.5:1 contrast ratio | WCAG 2.1 Level AA |
| **Error Identification** | Clear error messages with field association | WCAG 2.1 Level A |
| **Hints and Tooltips** | `label-hint` class provides additional context | WCAG 2.1 Level AAA |

---

### 10.10 UI Navigation Flow Diagrams

#### Admin User Journey
```
Landing Page → Login → Admin Home → {
    ├─ Admin Dashboard (View Analytics)
    ├─ Manage Users (CRUD)
    ├─ Manage Room Types (CRUD)
    └─ Manage Rooms (CRUD)
} → Logout
```

#### Staff User Journey
```
Landing Page → Login → Staff Home → {
    ├─ Staff Dashboard (View Stats)
    ├─ Staff Reservations (CRUD + Check-in/out)
    └─ Staff Billing (Generate & Manage Bills)
} → Logout
```

#### Guest User Journey
```
Landing Page → {
    ├─ Login → Guest Home
    └─ Register → Login → Guest Home
} → {
    ├─ Guest Dashboard (View Personal Stats)
    ├─ Guest Reservations (Create/Edit/Cancel)
    └─ Guest Bills (View Only)
} → Logout
```

---

**Document Version**: 1.1  
**Last Updated**: 2026-03-05  
**Prepared By**: Rovo Dev  
**Status**: Ready for Execution with UI Documentation
