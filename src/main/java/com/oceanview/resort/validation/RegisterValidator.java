package com.oceanview.resort.validation;

import com.oceanview.resort.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegisterValidator {
  private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9._-]{4,50}$");
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
  private static final Pattern PHONE_PATTERN = Pattern.compile("^[+0-9()\\-\\s]{7,20}$");

  public void validateForGuestRegistration(String username,
                                           String password,
                                           String confirmPassword,
                                           String fullName,
                                           String email,
                                           String phone,
                                           String nicPassport) {
    List<String> errors = new ArrayList<>();

    if (CommonValidator.isBlank(username)
      || CommonValidator.isBlank(password)
      || CommonValidator.isBlank(confirmPassword)
      || CommonValidator.isBlank(fullName)
      || CommonValidator.isBlank(email)
      || CommonValidator.isBlank(phone)
      || CommonValidator.isBlank(nicPassport)) {
      errors.add("All required fields must be filled.");
    }

    if (!CommonValidator.isBlank(username) && !USERNAME_PATTERN.matcher(username.trim()).matches()) {
      errors.add("Username must be 4-50 chars and only contain letters, numbers, dot, underscore, or hyphen.");
    }

    if (!CommonValidator.isBlank(password)) {
      if (password.length() < 8) {
        errors.add("Password must be at least 8 characters.");
      }
      if (!containsUpper(password) || !containsLower(password) || !containsDigit(password)) {
        errors.add("Password must include uppercase, lowercase, and a digit.");
      }
    }

    if (!CommonValidator.isBlank(password)
      && !CommonValidator.isBlank(confirmPassword)
      && !password.equals(confirmPassword)) {
      errors.add("Password and confirmation do not match.");
    }

    if (!CommonValidator.isBlank(email) && !EMAIL_PATTERN.matcher(email.trim()).matches()) {
      errors.add("Email format is invalid.");
    }

    if (!CommonValidator.isBlank(phone) && !PHONE_PATTERN.matcher(phone.trim()).matches()) {
      errors.add("Phone format is invalid.");
    }

    if (!CommonValidator.isBlank(nicPassport) && nicPassport.trim().length() > 40) {
      errors.add("NIC/Passport must be 40 characters or less.");
    }

    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
  }

  private boolean containsUpper(String value) {
    for (char c : value.toCharArray()) {
      if (Character.isUpperCase(c)) {
        return true;
      }
    }
    return false;
  }

  private boolean containsLower(String value) {
    for (char c : value.toCharArray()) {
      if (Character.isLowerCase(c)) {
        return true;
      }
    }
    return false;
  }

  private boolean containsDigit(String value) {
    for (char c : value.toCharArray()) {
      if (Character.isDigit(c)) {
        return true;
      }
    }
    return false;
  }
}
