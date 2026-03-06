# Ocean View Resort - Manual UAT Script

## Preconditions
- Application is deployed and reachable on Tomcat 10+.
- DB schema and seed are applied.
- Test accounts exist for `ADMIN`, `STAFF`, and `GUEST`.
- Browser starts with a clean session (logout first).

## Admin UAT
1. Login with Admin account.
Expected: Redirect to `/admin/dashboard`.
2. Open `Users` and create a `STAFF` user.
Expected: Success message and user appears in list.
3. Update that user status to `LOCKED`, then back to `ACTIVE`.
Expected: Status changes persist.
4. Deactivate a non-critical user.
Expected: User remains in list with `INACTIVE` status (soft delete).
5. Open `Room Types` and create/update one room type.
Expected: Changes persist and list refresh shows updated values.
6. Open `Rooms` and inactivate one room.
Expected: Room status becomes `INACTIVE` (not hard deleted).

## Staff UAT
1. Login with Staff account.
Expected: Redirect to `/staff/dashboard`.
2. Create a reservation for an existing guest.
Expected: Reservation appears with `PENDING`/`CONFIRMED`.
3. Attempt overlapping reservation for same room/date range.
Expected: Blocked with overlap error.
4. Check in then check out reservation.
Expected: Status transitions follow business rules.
5. Cancel another active reservation via list action.
Expected: Reservation status becomes `CANCELLED`.
6. Generate bill with subtotal/tax/discount.
Expected: Total = subtotal + tax - discount.
7. Void the bill from list action.
Expected: Bill remains with payment status `VOID`.

## Guest UAT
1. Login with Guest account.
Expected: Redirect to `/guest/dashboard`.
2. Create reservation for an available room.
Expected: Reservation appears under `My Reservations`.
3. Edit own pending/confirmed reservation.
Expected: Changes saved.
4. Cancel own reservation.
Expected: Status changes to `CANCELLED`.
5. Attempt access to Admin or Staff URLs.
Expected: Access denied (`403`) or redirected to login if not authenticated.
6. Open `My Bills`.
Expected: Only bills for guest-owned reservations are visible.

## Sign-off
- Mark each step `PASS`/`FAIL`.
- Attach screenshots for failures and include error text/stack trace if available.
