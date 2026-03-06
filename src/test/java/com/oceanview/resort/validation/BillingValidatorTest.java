package com.oceanview.resort.validation;

import com.oceanview.resort.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BillingValidatorTest {
  private BillingValidator validator;

  @BeforeEach
  void setUp() {
    validator = new BillingValidator();
  }

  @Test
  void testValidBill() {
    assertDoesNotThrow(() -> validator.validateBill(
      new BigDecimal("100.00"),
      new BigDecimal("10.00"),
      new BigDecimal("5.00")
    ));
  }

  @Test
  void testNullSubtotal() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateBill(null, BigDecimal.ZERO, BigDecimal.ZERO));
    assertTrue(ex.getErrors().contains("Subtotal is required."));
  }

  @Test
  void testNegativeSubtotal() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateBill(new BigDecimal("-10.00"), BigDecimal.ZERO, BigDecimal.ZERO));
    assertTrue(ex.getErrors().contains("Subtotal cannot be negative."));
  }

  @Test
  void testSubtotalTooLarge() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateBill(new BigDecimal("2000000.00"), BigDecimal.ZERO, BigDecimal.ZERO));
    assertTrue(ex.getErrors().contains("Subtotal cannot exceed $1,000,000."));
  }

  @Test
  void testNegativeTax() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateBill(new BigDecimal("100.00"), new BigDecimal("-5.00"), BigDecimal.ZERO));
    assertTrue(ex.getErrors().contains("Tax cannot be negative."));
  }

  @Test
  void testNegativeDiscount() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateBill(new BigDecimal("100.00"), BigDecimal.ZERO, new BigDecimal("-5.00")));
    assertTrue(ex.getErrors().contains("Discount cannot be negative."));
  }

  @Test
  void testDiscountExceedsTotal() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validateBill(new BigDecimal("100.00"), BigDecimal.ZERO, new BigDecimal("150.00")));
    assertTrue(ex.getErrors().contains("Total amount cannot be negative. Reduce the discount."));
  }

  @Test
  void testValidPaymentStatus() {
    assertDoesNotThrow(() -> validator.validatePaymentStatus("UNPAID"));
    assertDoesNotThrow(() -> validator.validatePaymentStatus("PARTIAL"));
    assertDoesNotThrow(() -> validator.validatePaymentStatus("PAID"));
    assertDoesNotThrow(() -> validator.validatePaymentStatus("REFUNDED"));
    assertDoesNotThrow(() -> validator.validatePaymentStatus("VOID"));
  }

  @Test
  void testInvalidPaymentStatus() {
    ValidationException ex = assertThrows(ValidationException.class,
      () -> validator.validatePaymentStatus("INVALID"));
    assertTrue(ex.getErrors().contains("Invalid payment status selected."));
  }

  @Test
  void testPaymentStatusCaseInsensitive() {
    assertDoesNotThrow(() -> validator.validatePaymentStatus("paid"));
    assertDoesNotThrow(() -> validator.validatePaymentStatus("Unpaid"));
  }

  @Test
  void testZeroAmounts() {
    assertDoesNotThrow(() -> validator.validateBill(
      BigDecimal.ZERO,
      BigDecimal.ZERO,
      BigDecimal.ZERO
    ));
  }

  @Test
  void testMaxSubtotal() {
    assertDoesNotThrow(() -> validator.validateBill(
      new BigDecimal("1000000.00"),
      BigDecimal.ZERO,
      BigDecimal.ZERO
    ));
  }
}
