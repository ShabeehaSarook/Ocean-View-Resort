package com.oceanview.resort.validation;

import com.oceanview.resort.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomValidatorTest {
  private RoomValidator validator;

  @BeforeEach
  void setUp() {
    validator = new RoomValidator();
  }

  @Test
  void testValidRoom() {
    assertDoesNotThrow(() -> validator.validateRoom("101", "AVAILABLE", 1));
  }

  @Test
  void testValidRoomWithHyphens() {
    assertDoesNotThrow(() -> validator.validateRoom("A-204", "AVAILABLE", 2));
  }

  @Test
  void testBlankRoomNumber() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateRoom("", "AVAILABLE", 1));
    assertTrue(ex.getErrors().contains("Room number is required."));
  }

  @Test
  void testNullRoomNumber() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateRoom(null, "AVAILABLE", 1));
    assertTrue(ex.getErrors().contains("Room number is required."));
  }

  @Test
  void testInvalidRoomNumberFormat() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateRoom("Room@101", "AVAILABLE", 1));
    assertTrue(ex.getErrors().contains("Room number must be 1-20 characters and contain only letters, numbers, or hyphens."));
  }

  @Test
  void testRoomNumberTooLong() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateRoom("A123456789012345678901", "AVAILABLE", 1));
    assertTrue(ex.getErrors().contains("Room number must be 1-20 characters and contain only letters, numbers, or hyphens."));
  }

  @Test
  void testInvalidStatus() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateRoom("101", "INVALID_STATUS", 1));
    assertTrue(ex.getErrors().contains("Invalid room status selected."));
  }

  @Test
  void testAllValidStatuses() {
    assertDoesNotThrow(() -> validator.validateRoom("101", "AVAILABLE", 1));
    assertDoesNotThrow(() -> validator.validateRoom("102", "OCCUPIED", 1));
    assertDoesNotThrow(() -> validator.validateRoom("103", "MAINTENANCE", 1));
    assertDoesNotThrow(() -> validator.validateRoom("104", "INACTIVE", 1));
  }

  @Test
  void testStatusCaseInsensitive() {
    assertDoesNotThrow(() -> validator.validateRoom("101", "available", 1));
    assertDoesNotThrow(() -> validator.validateRoom("102", "Occupied", 1));
  }

  @Test
  void testFloorTooLow() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateRoom("101", "AVAILABLE", -6));
    assertTrue(ex.getErrors().contains("Floor must be between -5 and 200."));
  }

  @Test
  void testFloorTooHigh() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateRoom("101", "AVAILABLE", 201));
    assertTrue(ex.getErrors().contains("Floor must be between -5 and 200."));
  }

  @Test
  void testMinFloor() {
    assertDoesNotThrow(() -> validator.validateRoom("B5", "AVAILABLE", -5));
  }

  @Test
  void testMaxFloor() {
    assertDoesNotThrow(() -> validator.validateRoom("2001", "AVAILABLE", 200));
  }

  @Test
  void testGroundFloor() {
    assertDoesNotThrow(() -> validator.validateRoom("G1", "AVAILABLE", 0));
  }
}
