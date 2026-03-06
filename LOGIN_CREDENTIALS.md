# 🔐 Ocean View Resort - Login Credentials Guide

## Quick Access Credentials

All default accounts use the same password for development/testing purposes.

---

## Default Login Credentials

| Role | Username | Password | Access Level | Home Page After Login |
|------|----------|----------|--------------|----------------------|
| **👨‍💼 ADMIN** | `admin` | `Pass@123` | Full system access - manage users, rooms, room types | `/admin/home` |
| **👔 STAFF** | `staff1` | `Pass@123` | Manage reservations, billing, check-in/check-out | `/staff/home` |
| **👤 GUEST** | `guest1` | `Pass@123` | Self-service booking, view own reservations & bills | `/guest/home` |

---

## Step-by-Step Login Instructions

### 🌐 Step 1: Access the Application
1. Open your web browser (Chrome, Firefox, or Edge recommended)
2. Navigate to: **`http://localhost:8080/Ocean.View_Resort/`**
3. Click the **"Login"** button on the landing page

### 🔑 Step 2: Choose Your Role & Login

#### Option A: Login as ADMIN
```
Username: admin
Password: Pass@123
```
**What you can do:**
- ✅ View system-wide dashboard with analytics
- ✅ Manage all users (create, edit, deactivate)
- ✅ Manage room types (create, edit, delete)
- ✅ Manage rooms (create, edit, change status)
- ✅ View all reservations and bills
- ✅ Access system reports via API

#### Option B: Login as STAFF
```
Username: staff1
Password: Pass@123
```
**What you can do:**
- ✅ View staff dashboard with today's operations
- ✅ Create reservations for any guest
- ✅ Manage reservations (edit, cancel, check-in, check-out)
- ✅ Generate and manage bills
- ✅ Update payment status
- ❌ Cannot manage users or room types (admin only)

#### Option C: Login as GUEST
```
Username: guest1
Password: Pass@123
```
**What you can do:**
- ✅ View personal dashboard with your stats
- ✅ Create your own reservations (self-booking)
- ✅ Edit/cancel your reservations (if PENDING or CONFIRMED)
- ✅ View your billing history (read-only)
- ❌ Cannot manage other guests' reservations
- ❌ Cannot generate bills (staff only)
- ❌ Cannot access admin or staff functions

---

## Guest Profile Information

The default **guest1** account has the following profile:

| Field | Value |
|-------|-------|
| **Full Name** | Alex Johnson |
| **Email** | alex.johnson@example.com |
| **Phone** | +1-555-0101 |
| **NIC/Passport** | P1234567 |
| **Address** | 221B Palm Coast, Miami |

---

## Password Requirements

When creating new accounts via registration or admin panel, passwords must meet these criteria:

- ✅ Minimum 8 characters
- ✅ At least one uppercase letter (A-Z)
- ✅ At least one lowercase letter (a-z)
- ✅ At least one digit (0-9)
- ✅ Example valid passwords: `Pass@123`, `Admin2026`, `Guest@456`

---

## Common Login Issues & Solutions

| Issue | Cause | Solution |
|-------|-------|----------|
| **"Invalid username or password"** | Wrong credentials entered | • Verify username and password are correct<br>• Password is case-sensitive: `Pass@123` ≠ `pass@123`<br>• Try copying from this document |
| **"Page not found (404)"** | Application not deployed or wrong URL | • Ensure Tomcat is running<br>• Check URL: `http://localhost:8080/Ocean.View_Resort/`<br>• Verify deployment name matches |
| **"Database connection error"** | Database not running or wrong config | • Start MySQL service<br>• Verify `oceanview_resort` database exists<br>• Check `src/main/resources/db.properties` |
| **"Access denied"** | Trying to access restricted page | • GUEST cannot access `/admin/*` or `/staff/*` pages<br>• STAFF cannot access `/admin/*` pages<br>• Login with appropriate role |
| **Session expired** | Inactive for too long | • Click "Logout" and login again<br>• Session timeout is typically 30 minutes |

---

## Creating New Accounts

### As ADMIN - Create Users via Admin Panel
1. Login as **admin**
2. Navigate to **Manage Users** (`/admin/users`)
3. Fill the "Add New User" form:
   - Username: (4-50 characters, letters, numbers, underscores)
   - Password: (must meet requirements above)
   - Role: Choose ADMIN, STAFF, or GUEST
   - Status: Choose ACTIVE, INACTIVE, or LOCKED
4. Click **"Create User"**

### As Public User - Self-Registration (Guest only)
1. From landing page, click **"Register"**
2. Navigate to `/register`
3. Fill the registration form:
   - Username, Password, Confirm Password
   - Full Name, Email, Phone
   - NIC/Passport, Address
4. Click **"Register"**
5. New account created with **GUEST** role
6. Redirect to login page

---

## Security Notes

⚠️ **IMPORTANT FOR PRODUCTION:**

1. **Change Default Passwords**: The `Pass@123` password is for development only
2. **Use Strong Passwords**: Enforce complex passwords in production
3. **Disable/Remove Test Accounts**: Remove `admin`, `staff1`, `guest1` in production
4. **Enable HTTPS**: Use SSL/TLS for encrypted communication
5. **Session Management**: Implement proper session timeout and security headers
6. **Password Hashing**: System uses bcrypt (already implemented ✅)

---

## Quick Test Scenarios

### Test 1: Role-Based Access Control
```
1. Login as guest1
2. Try to access: http://localhost:8080/Ocean.View_Resort/admin/users
3. Expected: Access denied (403) or redirect to login
```

### Test 2: Password Validation
```
1. Go to Register page
2. Try password: "password" (no uppercase, no digit)
3. Expected: Validation error
4. Try password: "Pass@123"
5. Expected: Validation passes
```

### Test 3: Different Role Dashboards
```
1. Login as admin → See system-wide stats (all users, rooms, revenue)
2. Logout → Login as staff1 → See operational stats (today's check-ins/outs)
3. Logout → Login as guest1 → See personal stats (your reservations only)
```

---

## Visual Login Flow

```
┌─────────────────┐
│  Landing Page   │
│  (index.jsp)    │
└────────┬────────┘
         │ Click "Login"
         ▼
┌─────────────────┐
│   Login Page    │
│   (/login)      │
└────────┬────────┘
         │ Enter credentials + Submit
         ▼
    ┌────┴────┐
    │ Verify  │
    │ Role    │
    └────┬────┘
         │
    ┌────┼────────────────────┐
    │    │                    │
    ▼    ▼                    ▼
┌────────┐  ┌────────┐  ┌────────┐
│ ADMIN  │  │ STAFF  │  │ GUEST  │
│ /admin │  │ /staff │  │ /guest │
│ /home  │  │ /home  │  │ /home  │
└────────┘  └────────┘  └────────┘
```

---

## API Testing (Optional)

If you want to test the API directly (for staff/admin):

```bash
# Example: Get revenue report (requires authentication)
curl -X GET "http://localhost:8080/Ocean.View_Resort/api/reports/revenue" \
     -H "Cookie: JSESSIONID=<your-session-id>"
```

---

## Need Help?

- 📚 **Full Test Cases**: See `COMPREHENSIVE_TEST_CASES.md`
- 🗄️ **Database Schema**: See `sql/schema.sql`
- 🌱 **Seed Data**: See `sql/seed.sql`
- 📖 **README**: See `README.md`

---

**Last Updated**: 2026-03-05  
**Application Version**: 1.0  
**Environment**: Development/Testing
