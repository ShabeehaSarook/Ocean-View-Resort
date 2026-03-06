package com.oceanview.resort.controller;

import com.oceanview.resort.dao.UserDAO;
import com.oceanview.resort.dao.impl.UserJdbcDAO;
import com.oceanview.resort.model.User;
import com.oceanview.resort.util.AuthConstants;
import com.oceanview.resort.util.AuthRedirectUtil;
import com.oceanview.resort.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = {"/login", "/logout"})
public class AuthServlet extends HttpServlet {
  private static final String LOGIN_VIEW = "/WEB-INF/views/common/login.jsp";

  private final UserDAO userDAO = new UserJdbcDAO();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String servletPath = request.getServletPath();
    if ("/logout".equals(servletPath)) {
      performLogout(request, response);
      return;
    }

    User currentUser = getSessionUser(request);
    if (currentUser != null) {
      AuthRedirectUtil.sendRoleHomeRedirect(request, response, currentUser.getRole());
      return;
    }

    request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String servletPath = request.getServletPath();
    if ("/logout".equals(servletPath)) {
      performLogout(request, response);
      return;
    }

    User currentUser = getSessionUser(request);
    if (currentUser != null) {
      AuthRedirectUtil.sendRoleHomeRedirect(request, response, currentUser.getRole());
      return;
    }

    String username = trimToEmpty(request.getParameter("username"));
    String password = trimToEmpty(request.getParameter("password"));

    if (username.isEmpty() || password.isEmpty()) {
      request.setAttribute("errorMessage", "Username and password are required.");
      request.setAttribute("username", username);
      request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
      return;
    }

    Optional<User> userOpt = userDAO.findByUsername(username);
    if (userOpt.isEmpty()) {
      denyLogin(request, response, username);
      return;
    }

    User user = userOpt.get();
    boolean passwordOk = PasswordUtil.matches(password, user.getPasswordHash());
    boolean active = "ACTIVE".equalsIgnoreCase(user.getStatus());

    if (!passwordOk || !active) {
      denyLogin(request, response, username);
      return;
    }

    HttpSession existingSession = request.getSession(false);
    if (existingSession != null) {
      existingSession.invalidate();
    }

    HttpSession newSession = request.getSession(true);
    newSession.setAttribute(AuthConstants.SESSION_USER, user);
    newSession.setMaxInactiveInterval(30 * 60);

    response.sendRedirect(request.getContextPath() + AuthRedirectUtil.roleHomePath(user.getRole()) + "?loginSuccess=1");
  }

  private void performLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    clearSessionCookie(request, response);
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    response.sendRedirect(request.getContextPath() + "/login?logout=1");
  }

  private void clearSessionCookie(HttpServletRequest request, HttpServletResponse response) {
    Cookie cookie = new Cookie("JSESSIONID", "");
    String contextPath = request.getContextPath();
    cookie.setPath(contextPath == null || contextPath.isBlank() ? "/" : contextPath);
    cookie.setMaxAge(0);
    cookie.setHttpOnly(true);
    cookie.setSecure(request.isSecure());
    response.addCookie(cookie);
  }

  private void denyLogin(HttpServletRequest request, HttpServletResponse response, String username)
    throws ServletException, IOException {
    request.setAttribute("errorMessage", "Invalid username or password.");
    request.setAttribute("username", username);
    request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
  }

  private User getSessionUser(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return null;
    }
    Object user = session.getAttribute(AuthConstants.SESSION_USER);
    return (user instanceof User) ? (User) user : null;
  }

  private String trimToEmpty(String value) {
    return value == null ? "" : value.trim();
  }
}
