package com.oceanview.resort.validation;

import com.oceanview.resort.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EnhancedReservationValidatorTest {
  private ReservationValidator validator;

  @BeforeEach
  void setUp() {
    validator = new ReservationValidator();
  }

  @Test
  void testPastCheckInDate() {
    LocalDate yesterday = LocalDate.now().minusDays(1);
    LocalDate tomorrow = LocalDate.now().plusDays(1);
    
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateReservation("CONFIRMED", yesterday, tomorrow, 2, 0));
    assertTrue(ex.getErrors().contains("Check-in date cannot be in the past."));
  }

  @Test
  void testCheckInToday() {
    LocalDate today = LocalDate.now();
    LocalDate tomorrow = today.plusDays(1);
    
    assertDoesNotThrow(() -> validator.validateReservation("CONFIRMED", today, tomorrow, 2, 0));
  }

  @Test
  void testMaxStayDuration() {
    LocalDate checkIn = LocalDate.now().plusDays(1);
    LocalDate checkOut = checkIn.plusDays(366);
    
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateReservation("CONFIRMED", checkIn, checkOut, 2, 0));
    assertTrue(ex.getErrors().contains("Maximum stay duration is 365 days."));
  }

  @Test
  void testExactly365Days() {
    LocalDate checkIn = LocalDate.now().plusDays(1);
    LocalDate checkOut = checkIn.plusDays(365);
    
    assertDoesNotThrow(() -> validator.validateReservation("CONFIRMED", checkIn, checkOut, 2, 0));
  }

  @Test
  void testTooManyAdults() {
    LocalDate checkIn = LocalDate.now().plusDays(1);
    LocalDate checkOut = checkIn.plusDays(2);
    
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateReservation("CONFIRMED", checkIn, checkOut, 21, 0));
    assertTrue(ex.getErrors().contains("Maximum 20 adults allowed."));
  }

  @Test
  void testMaxAdults() {
    LocalDate checkIn = LocalDate.now().plusDays(1);
    LocalDate checkOut = checkIn.plusDays(2);
    
    assertDoesNotThrow(() -> validator.validateReservation("CONFIRMED", checkIn, checkOut, 20, 0));
  }

  @Test
  void testTooManyChildren() {
    LocalDate checkIn = LocalDate.now().plusDays(1);
    LocalDate checkOut = checkIn.plusDays(2);
    
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateReservation("CONFIRMED", checkIn, checkOut, 1, 21));
    assertTrue(ex.getErrors().contains("Maximum 20 children allowed."));
  }

  @Test
  void testMaxChildren() {
    LocalDate checkIn = LocalDate.now().plusDays(1);
    LocalDate checkOut = checkIn.plusDays(2);
    
    assertDoesNotThrow(() -> validator.validateReservation("CONFIRMED", checkIn, checkOut, 1, 20));
  }

  @Test
  void testTooManyTotalGuests() {
    LocalDate checkIn = LocalDate.now().plusDays(1);
    LocalDate checkOut = checkIn.plusDays(2);
    
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateReservation("CONFIRMED", checkIn, checkOut, 20, 11));
    assertTrue(ex.getErrors().contains("Maximum total guests is 30."));
  }

  @Test
  void testMaxTotalGuests() {
    LocalDate checkIn = LocalDate.now().plusDays(1);
    LocalDate checkOut = checkIn.plusDays(2);
    
    assertDoesNotThrow(() -> validator.validateReservation("CONFIRMED", checkIn, checkOut, 20, 10));
  }

  @Test
  void testSameDayCheckInCheckOut() {
    LocalDate checkIn = LocalDate.now().plusDays(1);
    LocalDate checkOut = checkIn;
    
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateReservation("CONFIRMED", checkIn, checkOut, 2, 0));
    assertTrue(ex.getErrors().contains("Check-out date must be after check-in date."));
  }

  @Test
  void testMinimumStay() {
    LocalDate checkIn = LocalDate.now().plusDays(1);
    LocalDate checkOut = checkIn.plusDays(1);
    
    assertDoesNotThrow(() -> validator.validateReservation("CONFIRMED", checkIn, checkOut, 2, 0));
  }
}
