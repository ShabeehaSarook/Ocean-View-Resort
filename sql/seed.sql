USE oceanview_resort;

-- Password placeholder hash (replace in production)
-- Example plain password for all users during development: Pass@123
INSERT INTO users (id, username, password_hash, role, status) VALUES
  (1, 'admin', '$2a$12$SNxL.saiL1pz3GlqISMjH.94oeia9O38DAKjQ07rVu.IP6q7g2vLO', 'ADMIN', 'ACTIVE'),
  (2, 'staff1', '$2a$12$SNxL.saiL1pz3GlqISMjH.94oeia9O38DAKjQ07rVu.IP6q7g2vLO', 'STAFF', 'ACTIVE'),
  (3, 'guest1', '$2a$12$SNxL.saiL1pz3GlqISMjH.94oeia9O38DAKjQ07rVu.IP6q7g2vLO', 'GUEST', 'ACTIVE')
ON DUPLICATE KEY UPDATE
  username = VALUES(username),
  password_hash = VALUES(password_hash),
  role = VALUES(role),
  status = VALUES(status);

INSERT INTO guest_profiles (id, user_id, full_name, email, phone, nic_passport, address) VALUES
  (1, 3, 'Alex Johnson', 'alex.johnson@example.com', '+1-555-0101', 'P1234567', '221B Palm Coast, Miami')
ON DUPLICATE KEY UPDATE full_name = VALUES(full_name);

INSERT INTO room_types (id, name, base_price, capacity, description) VALUES
  (1, 'Standard', 120.00, 2, 'Cozy room with garden view'),
  (2, 'Deluxe', 180.00, 3, 'Spacious room with partial sea view'),
  (3, 'Suite', 300.00, 4, 'Luxury suite with full ocean view')
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO rooms (id, room_number, room_type_id, status, floor) VALUES
  (1, '101', 1, 'AVAILABLE', 1),
  (2, '102', 1, 'AVAILABLE', 1),
  (3, '201', 2, 'AVAILABLE', 2),
  (4, '301', 3, 'AVAILABLE', 3)
ON DUPLICATE KEY UPDATE room_number = VALUES(room_number);

INSERT INTO reservations (id, guest_id, room_id, check_in, check_out, adults, children, status, booked_by) VALUES
  (1, 1, 3, CURDATE() + INTERVAL 2 DAY, CURDATE() + INTERVAL 5 DAY, 2, 1, 'CONFIRMED', 2)
ON DUPLICATE KEY UPDATE status = VALUES(status);

INSERT INTO bills (id, reservation_id, subtotal, tax, discount, total, issued_by, payment_status) VALUES
  (1, 1, 540.00, 54.00, 0.00, 594.00, 2, 'UNPAID')
ON DUPLICATE KEY UPDATE total = VALUES(total);
