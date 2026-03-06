package com.oceanview.resort.validation;

import com.oceanview.resort.exception.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationValidator {
  private static final int MIN_ADULTS = 1;
  private static final int MAX_ADULTS = 20;
  private static final int MIN_CHILDREN = 0;
  private static final int MAX_CHILDREN = 20;
  private static final int MAX_TOTAL_GUESTS = 30;
  private static final int MAX_STAY_DAYS = 365;

  public void validateReservation(String status,
                                  LocalDate checkIn,
                                  LocalDate checkOut,
                                  int adults,
                                  int children) {
    List<String> errors = new ArrayList<>();

    String normalizedStatus = CommonValidator.normalizeUpper(status);
    if (!("PENDING".equals(normalizedStatus)
      || "CONFIRMED".equals(normalizedStatus)
      || "CHECKED_IN".equals(normalizedStatus)
      || "CHECKED_OUT".equals(normalizedStatus)
      || "CANCELLED".equals(normalizedStatus)
      || "NO_SHOW".equals(normalizedStatus))) {
      errors.add("Invalid reservation status selected.");
    }

    if (checkIn == null || checkOut == null) {
      errors.add("Check-in and check-out dates are required.");
    } else {
      LocalDate today = LocalDate.now();
      if (checkIn.isBefore(today)) {
        errors.add("Check-in date cannot be in the past.");
      }
      
      if (!checkOut.isAfter(checkIn)) {
        errors.add("Check-out date must be after check-in date.");
      }
      
      long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
      if (daysBetween > MAX_STAY_DAYS) {
        errors.add("Maximum stay duration is " + MAX_STAY_DAYS + " days.");
      }
      if (daysBetween < 1) {
        errors.add("Minimum stay duration is 1 day.");
      }
    }

    if (adults < MIN_ADULTS) {
      errors.add("At least " + MIN_ADULTS + " adult is required.");
    }
    
    if (adults > MAX_ADULTS) {
      errors.add("Maximum " + MAX_ADULTS + " adults allowed.");
    }

    if (children < MIN_CHILDREN) {
      errors.add("Children cannot be negative.");
    }
    
    if (children > MAX_CHILDREN) {
      errors.add("Maximum " + MAX_CHILDREN + " children allowed.");
    }
    
    int totalGuests = adults + children;
    if (totalGuests > MAX_TOTAL_GUESTS) {
      errors.add("Maximum total guests is " + MAX_TOTAL_GUESTS + ".");
    }

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
  }
}
