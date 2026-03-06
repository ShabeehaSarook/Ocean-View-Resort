package com.oceanview.resort.service;

import com.oceanview.resort.exception.ValidationException;
import com.oceanview.resort.util.PasswordUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisterServiceTest {
  private final RegisterService registerService = new RegisterService();

  @Test
  void validateGuestRegistrationShouldThrowForInvalidData() {
    assertThrows(ValidationException.class, () -> registerService.validateGuestRegistration(
      "ab",
      "weak",
      "weak",
      "",
      "bad-email",
      "bad",
      ""
    ));
  }

  @Test
  void hashPasswordShouldProduceVerifiableHash() {
    String plainPassword = "StrongPass1";
    String hash = registerService.hashPassword(plainPassword);

    assertNotEquals(plainPassword, hash);
    assertTrue(PasswordUtil.matches(plainPassword, hash));
  }
}
