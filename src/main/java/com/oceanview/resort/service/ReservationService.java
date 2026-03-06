package com.oceanview.resort.service;

import com.oceanview.resort.exception.ValidationException;
import com.oceanview.resort.validation.CommonValidator;
import com.oceanview.resort.validation.ReservationValidator;

import java.time.LocalDate;

public class ReservationService {
  private final ReservationValidator reservationValidator;

  public ReservationService() {
    this(new ReservationValidator());
  }

  public ReservationService(ReservationValidator reservationValidator) {
    this.reservationValidator = reservationValidator;
  }

  public void validateForCreateOrUpdate(String status,
                                        LocalDate checkIn,
                                        LocalDate checkOut,
                                        int adults,
                                        int children) {
    reservationValidator.validateReservation(status, checkIn, checkOut, adults, children);
  }

  public void ensureCheckInAllowed(String currentStatus) {
    String status = CommonValidator.normalizeUpper(currentStatus);
    if (!("PENDING".equals(status) || "CONFIRMED".equals(status))) {
      throw new ValidationException("Only pending or confirmed reservations can be checked in.");
    }
  }

  public void ensureCheckOutAllowed(String currentStatus) {
    String status = CommonValidator.normalizeUpper(currentStatus);
    if (!"CHECKED_IN".equals(status)) {
      throw new ValidationException("Only checked-in reservations can be checked out.");
    }
  }

  public void ensureGuestModifyAllowed(String currentStatus) {
    String status = CommonValidator.normalizeUpper(currentStatus);
    if (!("PENDING".equals(status) || "CONFIRMED".equals(status))) {
      throw new ValidationException("Only pending or confirmed reservations can be modified by guest.");
    }
  }

  public boolean isActiveReservationStatus(String status) {
    String normalized = CommonValidator.normalizeUpper(status);
    return "PENDING".equals(normalized)
      || "CONFIRMED".equals(normalized)
      || "CHECKED_IN".equals(normalized);
  }
}
