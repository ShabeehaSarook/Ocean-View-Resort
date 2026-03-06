package com.oceanview.resort.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {
  private static final int LOG_ROUNDS = 12;

  private PasswordUtil() {
  }

  public static String hashPassword(String plainPassword) {
    return BCrypt.hashpw(plainPassword, BCrypt.gensalt(LOG_ROUNDS));
  }

  public static boolean matches(String plainPassword, String hashedPassword) {
    if (plainPassword == null || hashedPassword == null || hashedPassword.isBlank()) {
      return false;
    }
    return BCrypt.checkpw(plainPassword, hashedPassword);
  }
}
