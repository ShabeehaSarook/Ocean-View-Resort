# Ocean View Resort - Testing Plan

## 1. Unit Tests
- `ReservationServiceTest`
  - Reservation input validation (status/date/guest counts)
  - Check-in/check-out transition guards
  - Guest modification guard
  - Active status evaluation
- `BillingServiceTest`
  - Bill total formula: `subtotal + tax - discount`
  - Negative amount validation
  - Discount > subtotal+tax validation
  - Payment status validation
  - Payment status normalization
- `RegisterValidatorTest`
  - Required fields, username/email/phone/NIC formats
  - Password strength and password-confirmation matching

## 2. Integration Checks
- Login/role redirect flow
  - `IntegrationChecksTest.loginRoleRedirectFlowShouldMapToExpectedDashboards`
  - Verifies role -> route mapping (`ADMIN`, `STAFF`, `GUEST`)
- Reservation overlap prevention
  - `IntegrationChecksTest.reservationOverlapPreventionShouldBlockConflictingDates`
  - Verifies DAO overlap query detects conflict and non-conflict windows
- Bill total calculations
  - `IntegrationChecksTest.billTotalCalculationsShouldMatchStoredValues`
  - Verifies stored bill totals equal computed totals from current DB data

## 3. Manual UAT Script
- Use `docs/MANUAL_UAT_SCRIPT.md` for step-by-step UAT by role:
  - Admin
  - Staff
  - Guest
- Includes expected outcomes for access control, data ownership, and core workflows.

## 4. Execution
- Run all automated tests:
  - `.\mvnw.cmd -s .mvn/local-settings.xml test`
- Review results:
  - All unit tests must pass.
  - Integration checks should pass when DB is available.
  - Execute UAT script and record pass/fail evidence per step.
