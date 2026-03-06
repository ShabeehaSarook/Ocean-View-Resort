package com.oceanview.resort.service;

import com.oceanview.resort.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BillingServiceTest {
  private final BillingService billingService = new BillingService();

  @Test
  void calculateTotalShouldReturnSubtotalPlusTaxMinusDiscount() {
    BigDecimal total = billingService.calculateTotal(
      new BigDecimal("100.00"),
      new BigDecimal("12.50"),
      new BigDecimal("7.50")
    );

    assertEquals(0, total.compareTo(new BigDecimal("105.00")));
  }

  @Test
  void validateShouldRejectNegativeValues() {
    ValidationException ex = assertThrows(ValidationException.class, () -> billingService.validateForCreateOrUpdate(
      new BigDecimal("-1.00"),
      new BigDecimal("0.00"),
      new BigDecimal("0.00"),
      "UNPAID"
    ));

    assertTrue(ex.getErrors().contains("Subtotal, tax, and discount cannot be negative."));
  }

  @Test
  void validateShouldRejectDiscountHigherThanSubtotalPlusTax() {
    ValidationException ex = assertThrows(ValidationException.class, () -> billingService.validateForCreateOrUpdate(
      new BigDecimal("100.00"),
      new BigDecimal("5.00"),
      new BigDecimal("106.00"),
      "PAID"
    ));

    assertTrue(ex.getErrors().contains("Discount is too high; total cannot be negative."));
  }

  @Test
  void validateShouldRejectInvalidPaymentStatus() {
    ValidationException ex = assertThrows(ValidationException.class, () -> billingService.validateForCreateOrUpdate(
      new BigDecimal("100.00"),
      new BigDecimal("5.00"),
      new BigDecimal("0.00"),
      "DONE"
    ));

    assertTrue(ex.getErrors().contains("Invalid payment status selected."));
  }

  @Test
  void normalizePaymentStatusShouldDefaultToUnpaid() {
    assertEquals("UNPAID", billingService.normalizePaymentStatus(""));
    assertEquals("PAID", billingService.normalizePaymentStatus("paid"));
  }
}
