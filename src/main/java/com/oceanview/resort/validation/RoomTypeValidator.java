package com.oceanview.resort.validation;

import com.oceanview.resort.exception.ValidationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RoomTypeValidator {
  private static final BigDecimal MAX_BASE_PRICE = new BigDecimal("100000.00");
  private static final BigDecimal MIN_PRICE = BigDecimal.ZERO;
  private static final int MIN_CAPACITY = 1;
  private static final int MAX_CAPACITY = 50;
  private static final int MAX_NAME_LENGTH = 100;
  private static final int MAX_DESCRIPTION_LENGTH = 500;

  public void validateRoomType(String name, BigDecimal basePrice, int capacity, String description) {
    List<String> errors = new ArrayList<>();

    if (CommonValidator.isBlank(name)) {
      errors.add("Room type name is required.");
    } else {
      if (name.trim().length() > MAX_NAME_LENGTH) {
        errors.add("Room type name must be " + MAX_NAME_LENGTH + " characters or less.");
      }
    }

    if (basePrice == null) {
      errors.add("Base price is required.");
    } else {
      if (basePrice.compareTo(MIN_PRICE) < 0) {
        errors.add("Base price cannot be negative.");
      }
      if (basePrice.compareTo(MAX_BASE_PRICE) > 0) {
        errors.add("Base price cannot exceed $100,000.");
      }
    }

    if (capacity < MIN_CAPACITY || capacity > MAX_CAPACITY) {
      errors.add("Capacity must be between " + MIN_CAPACITY + " and " + MAX_CAPACITY + ".");
    }

    if (description != null && description.trim().length() > MAX_DESCRIPTION_LENGTH) {
      errors.add("Description must be " + MAX_DESCRIPTION_LENGTH + " characters or less.");
    }

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
  }
}
