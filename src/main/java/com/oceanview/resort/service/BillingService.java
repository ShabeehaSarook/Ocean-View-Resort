package com.oceanview.resort.service;

import com.oceanview.resort.exception.ValidationException;
import com.oceanview.resort.validation.CommonValidator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BillingService {

  public BigDecimal calculateTotal(BigDecimal subtotal, BigDecimal tax, BigDecimal discount) {
    if (subtotal == null || tax == null || discount == null) {
      throw new ValidationException("Subtotal, tax, and discount are required.");
    }
    return subtotal.add(tax).subtract(discount);
  }

  public void validateForCreateOrUpdate(BigDecimal subtotal,
                                        BigDecimal tax,
                                        BigDecimal discount,
                                        String paymentStatus) {
    List<String> errors = new ArrayList<>();

    if (subtotal == null || tax == null || discount == null) {
      errors.add("Subtotal, tax, and discount are required.");
    } else {
      if (subtotal.compareTo(BigDecimal.ZERO) < 0
        || tax.compareTo(BigDecimal.ZERO) < 0
        || discount.compareTo(BigDecimal.ZERO) < 0) {
        errors.add("Subtotal, tax, and discount cannot be negative.");
      }

      if (calculateTotal(subtotal, tax, discount).compareTo(BigDecimal.ZERO) < 0) {
        errors.add("Discount is too high; total cannot be negative.");
      }
    }

    String normalizedPaymentStatus = normalizePaymentStatus(paymentStatus);
    if (!("UNPAID".equals(normalizedPaymentStatus)
      || "PARTIAL".equals(normalizedPaymentStatus)
      || "PAID".equals(normalizedPaymentStatus)
      || "REFUNDED".equals(normalizedPaymentStatus)
      || "VOID".equals(normalizedPaymentStatus))) {
      errors.add("Invalid payment status selected.");
    }

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
  }

  public String normalizePaymentStatus(String value) {
    String normalized = CommonValidator.normalizeUpper(value);
    return normalized.isEmpty() ? "UNPAID" : normalized;
  }
}
