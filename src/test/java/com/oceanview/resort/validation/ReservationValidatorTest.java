package com.oceanview.resort.validation;

import com.oceanview.resort.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReservationValidatorTest {
  private final ReservationValidator validator = new ReservationValidator();

  @Test
  void shouldAcceptValidReservationInput() {
    assertDoesNotThrow(() -> validator.validateReservation(
      "CONFIRMED",
      LocalDate.now().plusDays(1),
      LocalDate.now().plusDays(3),
      2,
      1
    ));
  }

  @Test
  void shouldRejectInvalidStatus() {
    ValidationException ex = assertThrows(ValidationException.class, () -> validator.validateReservation(
      "UNKNOWN",
      LocalDate.now().plusDays(1),
      LocalDate.now().plusDays(3),
      2,
      0
    ));

    assertTrue(ex.getErrors().contains("Invalid reservation status selected."));
  }

  @Test
  void shouldRejectInvalidDateRange() {
    ValidationException ex = assertThrows(ValidationException.class, () -> validator.validateReservation(
      "PENDING",
      LocalDate.now().plusDays(2),
      LocalDate.now().plusDays(2),
      2,
      0
    ));

    assertTrue(ex.getErrors().contains("Check-out date must be after check-in date."));
  }

  @Test
  void shouldRejectInvalidGuestCounts() {
    ValidationException ex = assertThrows(ValidationException.class, () -> validator.validateReservation(
      "PENDING",
      LocalDate.now().plusDays(1),
      LocalDate.now().plusDays(2),
      0,
      -1
    ));

    assertTrue(ex.getErrors().contains("Adults must be at least 1."));
    assertTrue(ex.getErrors().contains("Children cannot be negative."));
  }
}
