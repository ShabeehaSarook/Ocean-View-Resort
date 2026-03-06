package com.oceanview.resort.validation;

import com.oceanview.resort.exception.ValidationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BillingValidator {
  private static final BigDecimal MAX_SUBTOTAL = new BigDecimal("1000000.00");
  private static final BigDecimal MIN_AMOUNT = BigDecimal.ZERO;

  public void validateBill(BigDecimal subtotal, BigDecimal tax, BigDecimal discount) {
    List<String> errors = new ArrayList<>();

    if (subtotal == null) {
      errors.add("Subtotal is required.");
    } else {
      if (subtotal.compareTo(MIN_AMOUNT) < 0) {
        errors.add("Subtotal cannot be negative.");
      }
      if (subtotal.compareTo(MAX_SUBTOTAL) > 0) {
        errors.add("Subtotal cannot exceed $1,000,000.");
      }
    }

    if (tax != null && tax.compareTo(MIN_AMOUNT) < 0) {
      errors.add("Tax cannot be negative.");
    }

    if (discount != null && discount.compareTo(MIN_AMOUNT) < 0) {
      errors.add("Discount cannot be negative.");
    }

    if (subtotal != null && tax != null && discount != null) {
      BigDecimal total = subtotal.add(tax).subtract(discount);
      if (total.compareTo(MIN_AMOUNT) < 0) {
        errors.add("Total amount cannot be negative. Reduce the discount.");
      }
    }

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
  }

  public void validatePaymentStatus(String paymentStatus) {
    List<String> errors = new ArrayList<>();

    String normalized = CommonValidator.normalizeUpper(paymentStatus);
    if (!("UNPAID".equals(normalized)
      || "PARTIAL".equals(normalized)
      || "PAID".equals(normalized)
      || "REFUNDED".equals(normalized)
      || "VOID".equals(normalized))) {
      errors.add("Invalid payment status selected.");
    }

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
  }
}
