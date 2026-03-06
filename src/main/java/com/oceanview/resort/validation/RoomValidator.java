package com.oceanview.resort.validation;

import com.oceanview.resort.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RoomValidator {
  private static final Pattern ROOM_NUMBER_PATTERN = Pattern.compile("^[A-Za-z0-9-]{1,20}$");
  private static final int MIN_FLOOR = -5;
  private static final int MAX_FLOOR = 200;

  public void validateRoom(String roomNumber, String status, int floor) {
    List<String> errors = new ArrayList<>();

    if (CommonValidator.isBlank(roomNumber)) {
      errors.add("Room number is required.");
    } else {
      if (!ROOM_NUMBER_PATTERN.matcher(roomNumber.trim()).matches()) {
        errors.add("Room number must be 1-20 characters and contain only letters, numbers, or hyphens.");
      }
    }

    String normalizedStatus = CommonValidator.normalizeUpper(status);
    if (!("AVAILABLE".equals(normalizedStatus)
      || "OCCUPIED".equals(normalizedStatus)
      || "MAINTENANCE".equals(normalizedStatus)
      || "INACTIVE".equals(normalizedStatus))) {
      errors.add("Invalid room status selected.");
    }

    if (floor < MIN_FLOOR || floor > MAX_FLOOR) {
      errors.add("Floor must be between " + MIN_FLOOR + " and " + MAX_FLOOR + ".");
    }

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
  }
}
