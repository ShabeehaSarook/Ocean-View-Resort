package com.oceanview.resort.validation;

import com.oceanview.resort.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RoomTypeValidatorTest {
  private RoomTypeValidator validator;

  @BeforeEach
  void setUp() {
    validator = new RoomTypeValidator();
  }

  @Test
  void testValidRoomType() {
    assertDoesNotThrow(() -> validator.validateRoomType(
      "Deluxe Suite",
      new BigDecimal("200.00"),
      4,
      "Spacious room with ocean view"
    ));
  }

  @Test
  void testValidRoomTypeWithoutDescription() {
    assertDoesNotThrow(() -> validator.validateRoomType(
      "Standard Room",
      new BigDecimal("100.00"),
      2,
      null
    ));
  }

  @Test
  void testBlankName() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateRoomType("", new BigDecimal("100.00"), 2, null));
    assertTrue(ex.getErrors().contains("Room type name is required."));
  }

  @Test
  void testNullName() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateRoomType(null, new BigDecimal("100.00"), 2, null));
    assertTrue(ex.getErrors().contains("Room type name is required."));
  }

  @Test
  void testNameTooLong() {
    String longName = "A".repeat(101);
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateRoomType(longName, new BigDecimal("100.00"), 2, null));
    assertTrue(ex.getErrors().contains("Room type name must be 100 characters or less."));
  }

  @Test
  void testMaxNameLength() {
    String maxName = "A".repeat(100);
    assertDoesNotThrow(() -> validator.validateRoomType(maxName, new BigDecimal("100.00"), 2, null));
  }

  @Test
  void testNullBasePrice() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateRoomType("Suite", null, 2, null));
    assertTrue(ex.getErrors().contains("Base price is required."));
  }

  @Test
  void testNegativeBasePrice() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateRoomType("Suite", new BigDecimal("-10.00"), 2, null));
    assertTrue(ex.getErrors().contains("Base price cannot be negative."));
  }

  @Test
  void testBasePriceTooHigh() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateRoomType("Suite", new BigDecimal("150000.00"), 2, null));
    assertTrue(ex.getErrors().contains("Base price cannot exceed $100,000."));
  }

  @Test
  void testMaxBasePrice() {
    assertDoesNotThrow(() -> validator.validateRoomType("Presidential Suite", new BigDecimal("100000.00"), 2, null));
  }

  @Test
  void testZeroBasePrice() {
    assertDoesNotThrow(() -> validator.validateRoomType("Complimentary Room", BigDecimal.ZERO, 2, null));
  }

  @Test
  void testCapacityTooLow() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateRoomType("Suite", new BigDecimal("100.00"), 0, null));
    assertTrue(ex.getErrors().contains("Capacity must be between 1 and 50."));
  }

  @Test
  void testCapacityTooHigh() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateRoomType("Suite", new BigDecimal("100.00"), 51, null));
    assertTrue(ex.getErrors().contains("Capacity must be between 1 and 50."));
  }

  @Test
  void testMinCapacity() {
    assertDoesNotThrow(() -> validator.validateRoomType("Single Room", new BigDecimal("100.00"), 1, null));
  }

  @Test
  void testMaxCapacity() {
    assertDoesNotThrow(() -> validator.validateRoomType("Group Suite", new BigDecimal("500.00"), 50, null));
  }

  @Test
  void testDescriptionTooLong() {
    String longDesc = "A".repeat(501);
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateRoomType("Suite", new BigDecimal("100.00"), 2, longDesc));
    assertTrue(ex.getErrors().contains("Description must be 500 characters or less."));
  }

  @Test
  void testMaxDescriptionLength() {
    String maxDesc = "A".repeat(500);
    assertDoesNotThrow(() -> validator.validateRoomType("Suite", new BigDecimal("100.00"), 2, maxDesc));
  }

  @Test
  void testEmptyDescription() {
    assertDoesNotThrow(() -> validator.validateRoomType("Suite", new BigDecimal("100.00"), 2, ""));
  }
}
