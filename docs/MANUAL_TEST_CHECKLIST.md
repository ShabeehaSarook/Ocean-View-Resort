# Ocean View Resort - Manual Test Checklist

## Test Environment
- App deployed on Tomcat 10+ and reachable.
- MySQL schema (`sql/schema.sql`) and seed (`sql/seed.sql`) applied.
- At least one account per role exists: `ADMIN`, `STAFF`, `GUEST`.
- Browser cache cleared before role-access tests.

## Admin
- Login as `ADMIN` and verify redirect to `/admin/home`.
- Open Admin Dashboard and confirm chart panels load without JS errors.
- User CRUD:
  - Create `STAFF` and `GUEST` users with valid data.
  - Update user role/status.
  - Deactivate non-critical test user.
  - Verify duplicate username is blocked.
- Room Type CRUD:
  - Create, update, and delete room types.
  - Verify invalid capacity/price inputs are rejected.
- Room CRUD:
  - Create and edit room with valid room type.
  - Verify duplicate room number is blocked.
  - Inactivate a room and confirm status persistence after refresh.

## Staff
- Login as `STAFF` and verify redirect to `/staff/home`.
- Reservation CRUD:
  - Create reservation for existing guest and room.
  - Update reservation dates and guest counts.
  - Cancel test reservation that is not billed.
  - Verify invalid dates (check-out <= check-in) are rejected.
- Reservation flow:
  - Check in `PENDING`/`CONFIRMED` reservation.
  - Check out `CHECKED_IN` reservation.
  - Verify invalid transitions are blocked with friendly error.
- Billing:
  - Generate bill for reservation.
  - Update tax/discount and verify total recalculates.
  - Mark payment status and verify persistence.

## Guest
- Login as `GUEST` and verify redirect to `/guest/home`.
- Own reservation CRUD:
  - Create reservation for available room.
  - Edit own `PENDING`/`CONFIRMED` reservation.
  - Cancel own reservation.
  - Verify guest cannot edit/check-in checked-out reservation.
- Own bills:
  - View bills list and confirm only own reservation bills are visible.
  - Attempt direct URL access to another guest bill/reservation and confirm denied.

## Security & Access Control
- Access `/admin/*` while logged out and verify redirect to login.
- Login as `GUEST` and access `/admin/*` and `/staff/*`; verify `403` error page.
- Login as `STAFF` and access `/admin/*`; verify `403`.
- Confirm session logout invalidates access to protected routes.

## Data Integrity
- Create overlapping active reservations for same room and date range; verify second request is blocked.
- Confirm cancelling reservation does not remove guest/room records.
- Confirm bill always references valid reservation.
- Verify DB unique constraints:
  - `users.username` unique.
  - `rooms.room_number` unique.

## Error Handling
- Open unknown route (e.g., `/no-such-page`) and verify friendly `404` page.
- Trigger forbidden access and verify friendly `403` page.
- Trigger server-side error path (if available) and verify friendly `500` page.
