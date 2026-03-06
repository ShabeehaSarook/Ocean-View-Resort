package com.oceanview.resort.validation;

public final class CommonValidator {
  private CommonValidator() {
  }

  public static boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }

  public static String normalizeUpper(String value) {
    return value == null ? "" : value.trim().toUpperCase();
  }
}
