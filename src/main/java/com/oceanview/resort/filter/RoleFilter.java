package com.oceanview.resort.filter;

import com.oceanview.resort.model.User;
import com.oceanview.resort.util.AuthConstants;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/admin/*", "/staff/*", "/guest/*"})
public class RoleFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    User sessionUser = getSessionUser(httpRequest);
    if (sessionUser == null) {
      httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?authRequired=1");
      return;
    }

    String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
    if (!isRoleAllowed(path, sessionUser.getRole())) {
      httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied for this role.");
      return;
    }

    chain.doFilter(request, response);
  }

  private boolean isRoleAllowed(String path, String role) {
    if (path.startsWith("/admin/")) {
      return "ADMIN".equalsIgnoreCase(role);
    }
    if (path.startsWith("/staff/")) {
      return "STAFF".equalsIgnoreCase(role);
    }
    if (path.startsWith("/guest/")) {
      return "GUEST".equalsIgnoreCase(role);
    }
    return false;
  }

  private User getSessionUser(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return null;
    }
    Object user = session.getAttribute(AuthConstants.SESSION_USER);
    return user instanceof User ? (User) user : null;
  }
}
