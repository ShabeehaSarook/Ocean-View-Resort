package com.oceanview.resort.service;

import com.oceanview.resort.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReservationServiceTest {
  private final ReservationService reservationService = new ReservationService();

  @Test
  void validateForCreateOrUpdateShouldAcceptValidInput() {
    assertDoesNotThrow(() -> reservationService.validateForCreateOrUpdate(
      "PENDING",
      LocalDate.now().plusDays(1),
      LocalDate.now().plusDays(2),
      2,
      0
    ));
  }

  @Test
  void validateForCreateOrUpdateShouldRejectInvalidInput() {
    assertThrows(ValidationException.class, () -> reservationService.validateForCreateOrUpdate(
      "PENDING",
      LocalDate.now().plusDays(2),
      LocalDate.now().plusDays(1),
      0,
      -1
    ));
  }

  @Test
  void checkInTransitionShouldAllowPendingOrConfirmedOnly() {
    assertDoesNotThrow(() -> reservationService.ensureCheckInAllowed("PENDING"));
    assertDoesNotThrow(() -> reservationService.ensureCheckInAllowed("CONFIRMED"));
    assertThrows(ValidationException.class, () -> reservationService.ensureCheckInAllowed("CHECKED_OUT"));
  }

  @Test
  void checkOutTransitionShouldAllowCheckedInOnly() {
    assertDoesNotThrow(() -> reservationService.ensureCheckOutAllowed("CHECKED_IN"));
    assertThrows(ValidationException.class, () -> reservationService.ensureCheckOutAllowed("CONFIRMED"));
  }

  @Test
  void guestModifyShouldAllowPendingOrConfirmedOnly() {
    assertDoesNotThrow(() -> reservationService.ensureGuestModifyAllowed("PENDING"));
    assertDoesNotThrow(() -> reservationService.ensureGuestModifyAllowed("CONFIRMED"));
    assertThrows(ValidationException.class, () -> reservationService.ensureGuestModifyAllowed("CHECKED_IN"));
  }

  @Test
  void isActiveReservationStatusShouldReturnExpectedValues() {
    assertTrue(reservationService.isActiveReservationStatus("PENDING"));
    assertTrue(reservationService.isActiveReservationStatus("CONFIRMED"));
    assertTrue(reservationService.isActiveReservationStatus("CHECKED_IN"));
    assertFalse(reservationService.isActiveReservationStatus("CANCELLED"));
  }
}
