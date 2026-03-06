package com.oceanview.resort.service;

import com.oceanview.resort.util.PasswordUtil;
import com.oceanview.resort.validation.RegisterValidator;

public class RegisterService {
  private final RegisterValidator registerValidator;

  public RegisterService() {
    this(new RegisterValidator());
  }

  public RegisterService(RegisterValidator registerValidator) {
    this.registerValidator = registerValidator;
  }

  public void validateGuestRegistration(String username,
                                        String password,
                                        String confirmPassword,
                                        String fullName,
                                        String email,
                                        String phone,
                                        String nicPassport) {
    registerValidator.validateForGuestRegistration(
      username, password, confirmPassword, fullName, email, phone, nicPassport
    );
  }

  public String hashPassword(String plainPassword) {
    return PasswordUtil.hashPassword(plainPassword);
  }
}
