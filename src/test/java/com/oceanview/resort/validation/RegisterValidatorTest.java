package com.oceanview.resort.validation;

import com.oceanview.resort.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisterValidatorTest {
  private final RegisterValidator validator = new RegisterValidator();

  @Test
  void shouldAcceptValidGuestRegistrationInput() {
    assertDoesNotThrow(() -> validator.validateForGuestRegistration(
      "guest.user1",
      "StrongPass1",
      "StrongPass1",
      "Guest User",
      "guest@example.com",
      "+1-202-555-0101",
      "NIC123456"
    ));
  }

  @Test
  void shouldRejectWhenRequiredFieldsMissing() {
    ValidationException ex = assertThrows(ValidationException.class, () -> validator.validateForGuestRegistration(
      "",
      "StrongPass1",
      "StrongPass1",
      "Guest User",
      "guest@example.com",
      "+1-202-555-0101",
      "NIC123456"
    ));

    assertTrue(ex.getErrors().contains("All required fields must be filled."));
  }

  @Test
  void shouldRejectWeakPasswordAndPasswordMismatch() {
    ValidationException ex = assertThrows(ValidationException.class, () -> validator.validateForGuestRegistration(
      "guest.user1",
      "weak",
      "weak2",
      "Guest User",
      "guest@example.com",
      "+1-202-555-0101",
      "NIC123456"
    ));

    assertTrue(ex.getErrors().contains("Password must be at least 8 characters."));
    assertTrue(ex.getErrors().contains("Password must include uppercase, lowercase, and a digit."));
    assertTrue(ex.getErrors().contains("Password and confirmation do not match."));
  }

  @Test
  void shouldRejectInvalidUsernameEmailPhoneAndNicPassport() {
    ValidationException ex = assertThrows(ValidationException.class, () -> validator.validateForGuestRegistration(
      "a",
      "StrongPass1",
      "StrongPass1",
      "Guest User",
      "invalid-email",
      "x123",
      "12345678901234567890123456789012345678901"
    ));

    assertTrue(ex.getErrors().contains("Username must be 4-50 chars and only contain letters, numbers, dot, underscore, or hyphen."));
    assertTrue(ex.getErrors().contains("Email format is invalid."));
    assertTrue(ex.getErrors().contains("Phone format is invalid."));
    assertTrue(ex.getErrors().contains("NIC/Passport must be 40 characters or less."));
  }
}
