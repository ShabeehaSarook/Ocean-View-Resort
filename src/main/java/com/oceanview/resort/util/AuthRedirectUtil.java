package com.oceanview.resort.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public final class AuthRedirectUtil {
  private AuthRedirectUtil() {
  }

  public static String roleHomePath(String role) {
    if ("ADMIN".equalsIgnoreCase(role)) {
      return "/admin/home";
    }
    if ("STAFF".equalsIgnoreCase(role)) {
      return "/staff/home";
    }
    return "/guest/home";
  }

  public static void sendRoleHomeRedirect(HttpServletRequest request, HttpServletResponse response, String role)
    throws IOException {
    response.sendRedirect(request.getContextPath() + roleHomePath(role));
  }
}
