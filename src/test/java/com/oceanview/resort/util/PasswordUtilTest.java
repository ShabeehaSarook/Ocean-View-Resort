package com.oceanview.resort.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordUtilTest {

  @Test
  void hashAndMatchShouldWork() {
    String password = "Pass@1234";
    String hashed = PasswordUtil.hashPassword(password);

    assertNotEquals(password, hashed);
    assertTrue(PasswordUtil.matches(password, hashed));
    assertFalse(PasswordUtil.matches("WrongPass", hashed));
  }
}
