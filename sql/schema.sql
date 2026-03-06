-- Ocean View Resort schema (MySQL 8+)
-- Core entities: users, guest_profiles, room_types, rooms, reservations, bills

CREATE DATABASE IF NOT EXISTS oceanview_resort
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE oceanview_resort;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(16) NOT NULL,
  status VARCHAR(16) NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_users_username (username),
  CONSTRAINT chk_users_role CHECK (role IN ('ADMIN', 'STAFF', 'GUEST')),
  CONSTRAINT chk_users_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'LOCKED'))
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS guest_profiles (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id BIGINT UNSIGNED NOT NULL,
  full_name VARCHAR(120) NOT NULL,
  email VARCHAR(120) NOT NULL,
  phone VARCHAR(20) NOT NULL,
  nic_passport VARCHAR(40) NOT NULL,
  address VARCHAR(255) NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_guest_profiles_user_id (user_id),
  UNIQUE KEY uq_guest_profiles_email (email),
  UNIQUE KEY uq_guest_profiles_nic_passport (nic_passport),
  CONSTRAINT fk_guest_profiles_user
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS room_types (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(80) NOT NULL,
  base_price DECIMAL(10,2) NOT NULL,
  capacity INT NOT NULL,
  description VARCHAR(255) NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_room_types_name (name),
  CONSTRAINT chk_room_types_base_price CHECK (base_price >= 0),
  CONSTRAINT chk_room_types_capacity CHECK (capacity > 0)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS rooms (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  room_number VARCHAR(20) NOT NULL,
  room_type_id BIGINT UNSIGNED NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
  floor INT NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  UNIQUE KEY uq_rooms_room_number (room_number),
  KEY idx_rooms_room_type_id (room_type_id),
  CONSTRAINT fk_rooms_room_type
    FOREIGN KEY (room_type_id) REFERENCES room_types(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT chk_rooms_status CHECK (status IN ('AVAILABLE', 'OCCUPIED', 'MAINTENANCE', 'INACTIVE'))
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS reservations (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  guest_id BIGINT UNSIGNED NOT NULL,
  room_id BIGINT UNSIGNED NOT NULL,
  check_in DATE NOT NULL,
  check_out DATE NOT NULL,
  adults INT NOT NULL DEFAULT 1,
  children INT NOT NULL DEFAULT 0,
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  booked_by BIGINT UNSIGNED NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_reservations_guest_id (guest_id),
  KEY idx_reservations_room_id_dates (room_id, check_in, check_out),
  KEY idx_reservations_status (status),
  CONSTRAINT fk_reservations_guest
    FOREIGN KEY (guest_id) REFERENCES guest_profiles(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT fk_reservations_room
    FOREIGN KEY (room_id) REFERENCES rooms(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT fk_reservations_booked_by
    FOREIGN KEY (booked_by) REFERENCES users(id)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT chk_reservations_date_range CHECK (check_out > check_in),
  CONSTRAINT chk_reservations_adults CHECK (adults > 0),
  CONSTRAINT chk_reservations_children CHECK (children >= 0),
  CONSTRAINT chk_reservations_status CHECK (
    status IN ('PENDING', 'CONFIRMED', 'CHECKED_IN', 'CHECKED_OUT', 'CANCELLED', 'NO_SHOW')
  )
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS bills (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  reservation_id BIGINT UNSIGNED NOT NULL,
  subtotal DECIMAL(10,2) NOT NULL,
  tax DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  discount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  total DECIMAL(10,2) NOT NULL,
  issued_by BIGINT UNSIGNED NOT NULL,
  issued_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  payment_status VARCHAR(20) NOT NULL DEFAULT 'UNPAID',
  PRIMARY KEY (id),
  UNIQUE KEY uq_bills_reservation_id (reservation_id),
  KEY idx_bills_issued_by (issued_by),
  CONSTRAINT fk_bills_reservation
    FOREIGN KEY (reservation_id) REFERENCES reservations(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_bills_issued_by
    FOREIGN KEY (issued_by) REFERENCES users(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT chk_bills_subtotal CHECK (subtotal >= 0),
  CONSTRAINT chk_bills_tax CHECK (tax >= 0),
  CONSTRAINT chk_bills_discount CHECK (discount >= 0),
  CONSTRAINT chk_bills_total CHECK (total >= 0),
  CONSTRAINT chk_bills_payment_status CHECK (
    payment_status IN ('UNPAID', 'PARTIAL', 'PAID', 'REFUNDED', 'VOID')
  )
) ENGINE=InnoDB;

-- Reservation overlap prevention should be enforced in service/DAO logic.
-- Use this overlap rule for active reservation states:
--   [new_check_in, new_check_out) overlaps [existing_check_in, existing_check_out)
-- if new_check_in < existing_check_out AND new_check_out > existing_check_in
--
-- Example DAO check query:
-- SELECT COUNT(*)
-- FROM reservations r
-- WHERE r.room_id = ?
--   AND r.status IN ('PENDING', 'CONFIRMED', 'CHECKED_IN')
--   AND ? < r.check_out
--   AND ? > r.check_in;
