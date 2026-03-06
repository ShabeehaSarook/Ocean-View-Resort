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

@WebFilter("/*")
public class AuthFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String contextPath = httpRequest.getContextPath();
    String uri = httpRequest.getRequestURI();
    String path = uri.substring(contextPath.length());

    if (!isProtectedPath(path)) {
      chain.doFilter(request, response);
      return;
    }

    User sessionUser = getSessionUser(httpRequest);
    if (sessionUser == null) {
      httpResponse.sendRedirect(contextPath + "/login?authRequired=1");
      return;
    }

    chain.doFilter(request, response);
  }

  private boolean isProtectedPath(String path) {
    if (path == null || path.isBlank()) {
      return false;
    }

    return path.startsWith("/admin/")
      || path.startsWith("/staff/")
      || path.startsWith("/guest/");
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
