package com.oceanview.resort.integration;

import com.oceanview.resort.exception.ValidationException;
import com.oceanview.resort.validation.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests to verify that all form validations work correctly
 * across the entire validation pipeline.
 */
class FormValidationIntegrationTest {

  @Test
  void testCompleteReservationValidationPipeline() {
    ReservationValidator validator = new ReservationValidator();
    
    // Valid reservation
    LocalDate checkIn = LocalDate.now().plusDays(7);
    LocalDate checkOut = checkIn.plusDays(3);
    assertDoesNotThrow(() -> validator.validateReservation("CONFIRMED", checkIn, checkOut, 2, 1));
    
    // Invalid: past date
    LocalDate yesterday = LocalDate.now().minusDays(1);
    assertThrows(ValidationException.class,
      () -> validator.validateReservation("CONFIRMED", yesterday, checkOut, 2, 1));
    
    // Invalid: too many guests
    assertThrows(ValidationException.class,
      () -> validator.validateReservation("CONFIRMED", checkIn, checkOut, 20, 15));
  }

  @Test
  void testCompleteBillingValidationPipeline() {
    BillingValidator validator = new BillingValidator();
    
    // Valid bill
    assertDoesNotThrow(() -> {
      validator.validateBill(new BigDecimal("500.00"), new BigDecimal("50.00"), new BigDecimal("25.00"));
      validator.validatePaymentStatus("UNPAID");
    });
    
    // Invalid: negative total
    assertThrows(ValidationException.class,
      () -> validator.validateBill(new BigDecimal("100.00"), BigDecimal.ZERO, new BigDecimal("200.00")));
    
    // Invalid: payment status
    assertThrows(ValidationException.class,
      () -> validator.validatePaymentStatus("INVALID"));
  }

  @Test
  void testCompleteRoomValidationPipeline() {
    RoomValidator validator = new RoomValidator();
    
    // Valid room
    assertDoesNotThrow(() -> validator.validateRoom("101", "AVAILABLE", 1));
    
    // Invalid: bad room number format
    assertThrows(ValidationException.class,
      () -> validator.validateRoom("Room@#!", "AVAILABLE", 1));
    
    // Invalid: floor out of range
    assertThrows(ValidationException.class,
      () -> validator.validateRoom("101", "AVAILABLE", 300));
  }

  @Test
  void testCompleteRoomTypeValidationPipeline() {
    RoomTypeValidator validator = new RoomTypeValidator();
    
    // Valid room type
    assertDoesNotThrow(() -> validator.validateRoomType(
      "Deluxe Suite",
      new BigDecimal("250.00"),
      4,
      "Beautiful ocean view room"
    ));
    
    // Invalid: capacity too high
    assertThrows(ValidationException.class,
      () -> validator.validateRoomType("Suite", new BigDecimal("250.00"), 100, null));
    
    // Invalid: price too high
    assertThrows(ValidationException.class,
      () -> validator.validateRoomType("Suite", new BigDecimal("200000.00"), 4, null));
  }

  @Test
  void testCrossValidatorConsistency() {
    // Ensure all validators handle edge cases consistently
    ReservationValidator resValidator = new ReservationValidator();
    RoomValidator roomValidator = new RoomValidator();
    RoomTypeValidator typeValidator = new RoomTypeValidator();
    BillingValidator billValidator = new BillingValidator();
    
    // All should accept valid status strings (case-insensitive)
    LocalDate tomorrow = LocalDate.now().plusDays(1);
    LocalDate dayAfter = tomorrow.plusDays(1);
    
    assertDoesNotThrow(() -> resValidator.validateReservation("confirmed", tomorrow, dayAfter, 2, 0));
    assertDoesNotThrow(() -> roomValidator.validateRoom("101", "available", 1));
    assertDoesNotThrow(() -> billValidator.validatePaymentStatus("paid"));
    
    // All should reject invalid inputs consistently
    assertThrows(ValidationException.class,
      () -> resValidator.validateReservation("INVALID", tomorrow, dayAfter, 2, 0));
    assertThrows(ValidationException.class,
      () -> roomValidator.validateRoom("101", "INVALID", 1));
    assertThrows(ValidationException.class,
      () -> billValidator.validatePaymentStatus("INVALID"));
  }

  @Test
  void testRegistrationValidationPipeline() {
    RegisterValidator validator = new RegisterValidator();
    
    // Valid registration
    assertDoesNotThrow(() -> validator.validateForGuestRegistration(
      "john_doe",
      "Password123",
      "Password123",
      "John Doe",
      "john@example.com",
      "+1-555-1234",
      "ABC123456"
    ));
    
    // Invalid: password mismatch
    assertThrows(ValidationException.class,
      () -> validator.validateForGuestRegistration(
        "john_doe",
        "Password123",
        "DifferentPassword",
        "John Doe",
        "john@example.com",
        "+1-555-1234",
        "ABC123456"
      ));
    
    // Invalid: weak password
    assertThrows(ValidationException.class,
      () -> validator.validateForGuestRegistration(
        "john_doe",
        "weak",
        "weak",
        "John Doe",
        "john@example.com",
        "+1-555-1234",
        "ABC123456"
      ));
  }

  @Test
  void testBoundaryConditions() {
    ReservationValidator resValidator = new ReservationValidator();
    RoomTypeValidator typeValidator = new RoomTypeValidator();
    BillingValidator billValidator = new BillingValidator();
    
    LocalDate checkIn = LocalDate.now().plusDays(1);
    
    // Test boundary: exactly 365 days (should pass)
    LocalDate checkOut365 = checkIn.plusDays(365);
    assertDoesNotThrow(() -> resValidator.validateReservation("CONFIRMED", checkIn, checkOut365, 1, 0));
    
    // Test boundary: 366 days (should fail)
    LocalDate checkOut366 = checkIn.plusDays(366);
    assertThrows(ValidationException.class,
      () -> resValidator.validateReservation("CONFIRMED", checkIn, checkOut366, 1, 0));
    
    // Test boundary: exactly 20 adults (should pass)
    LocalDate checkOut2 = checkIn.plusDays(2);
    assertDoesNotThrow(() -> resValidator.validateReservation("CONFIRMED", checkIn, checkOut2, 20, 0));
    
    // Test boundary: 21 adults (should fail)
    assertThrows(ValidationException.class,
      () -> resValidator.validateReservation("CONFIRMED", checkIn, checkOut2, 21, 0));
    
    // Test boundary: exactly 30 total guests (should pass)
    assertDoesNotThrow(() -> resValidator.validateReservation("CONFIRMED", checkIn, checkOut2, 20, 10));
    
    // Test boundary: 31 total guests (should fail)
    assertThrows(ValidationException.class,
      () -> resValidator.validateReservation("CONFIRMED", checkIn, checkOut2, 20, 11));
    
    // Test boundary: max capacity 50 (should pass)
    assertDoesNotThrow(() -> typeValidator.validateRoomType("Suite", new BigDecimal("100.00"), 50, null));
    
    // Test boundary: capacity 51 (should fail)
    assertThrows(ValidationException.class,
      () -> typeValidator.validateRoomType("Suite", new BigDecimal("100.00"), 51, null));
    
    // Test boundary: max subtotal $1,000,000 (should pass)
    assertDoesNotThrow(() -> billValidator.validateBill(
      new BigDecimal("1000000.00"), BigDecimal.ZERO, BigDecimal.ZERO));
    
    // Test boundary: subtotal over $1,000,000 (should fail)
    assertThrows(ValidationException.class,
      () -> billValidator.validateBill(new BigDecimal("1000000.01"), BigDecimal.ZERO, BigDecimal.ZERO));
  }
}
