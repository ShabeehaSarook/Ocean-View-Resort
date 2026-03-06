package com.oceanview.resort.controller.admin;

import com.oceanview.resort.dao.UserDAO;
import com.oceanview.resort.dao.impl.UserJdbcDAO;
import com.oceanview.resort.exception.DataAccessException;
import com.oceanview.resort.model.User;
import com.oceanview.resort.util.AuthConstants;
import com.oceanview.resort.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebServlet("/admin/users")
public class ManageUsersServlet extends HttpServlet {
  private static final String VIEW = "/WEB-INF/views/admin/manage-users.jsp";

  private final UserDAO userDAO = new UserJdbcDAO();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.setAttribute("successMessage", request.getParameter("success"));
    request.setAttribute("errorMessage", request.getParameter("error"));

    try {
      request.setAttribute("users", userDAO.findAll());

      String editIdParam = request.getParameter("editId");
      if (editIdParam != null && !editIdParam.isBlank()) {
        long editId = Long.parseLong(editIdParam);
        Optional<User> editUser = userDAO.findById(editId);
        editUser.ifPresent(user -> request.setAttribute("editUser", user));
      }
    } catch (Exception ex) {
      request.setAttribute("errorMessage", "Unable to load users: " + ex.getMessage());
    }

    request.getRequestDispatcher(VIEW).forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String action = trimToEmpty(request.getParameter("action"));

    try {
      if ("create".equals(action)) {
        createUser(request);
        redirectSuccess(response, request, "User created successfully.");
        return;
      }
      if ("update".equals(action)) {
        updateUser(request);
        redirectSuccess(response, request, "User updated successfully.");
        return;
      }
      if ("delete".equals(action)) {
        deleteUser(request);
        redirectSuccess(response, request, "User deactivated successfully.");
        return;
      }

      redirectError(response, request, "Invalid action.");
    } catch (IllegalArgumentException | DataAccessException ex) {
      redirectError(response, request, ex.getMessage());
    }
  }

  private void createUser(HttpServletRequest request) {
    String username = trimToEmpty(request.getParameter("username"));
    String password = trimToEmpty(request.getParameter("password"));
    String role = trimToEmpty(request.getParameter("role")).toUpperCase();
    String status = trimToEmpty(request.getParameter("status")).toUpperCase();

    if (username.isEmpty() || password.isEmpty() || role.isEmpty() || status.isEmpty()) {
      throw new IllegalArgumentException("All user fields are required.");
    }
    if (password.length() < 8) {
      throw new IllegalArgumentException("Password must be at least 8 characters.");
    }
    validateRole(role);
    validateStatus(status);

    if (userDAO.findByUsername(username).isPresent()) {
      throw new IllegalArgumentException("Username already exists.");
    }

    User user = new User();
    user.setUsername(username);
    user.setPasswordHash(PasswordUtil.hashPassword(password));
    user.setRole(role);
    user.setStatus(status);
    userDAO.create(user);
  }

  private void updateUser(HttpServletRequest request) {
    long id = parseId(request.getParameter("id"), "User id is required.");
    String username = trimToEmpty(request.getParameter("username"));
    String password = trimToEmpty(request.getParameter("password"));
    String role = trimToEmpty(request.getParameter("role")).toUpperCase();
    String status = trimToEmpty(request.getParameter("status")).toUpperCase();

    if (username.isEmpty() || role.isEmpty() || status.isEmpty()) {
      throw new IllegalArgumentException("Username, role and status are required.");
    }
    validateRole(role);
    validateStatus(status);

    User existing = userDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found."));

    Optional<User> sameUsername = userDAO.findByUsername(username);
    if (sameUsername.isPresent() && !sameUsername.get().getId().equals(id)) {
      throw new IllegalArgumentException("Username already exists.");
    }

    existing.setUsername(username);
    existing.setRole(role);
    existing.setStatus(status);
    if (!password.isBlank()) {
      if (password.length() < 8) {
        throw new IllegalArgumentException("Password must be at least 8 characters.");
      }
      existing.setPasswordHash(PasswordUtil.hashPassword(password));
    }

    userDAO.update(existing);
  }

  private void deleteUser(HttpServletRequest request) {
    long id = parseId(request.getParameter("id"), "User id is required.");

    User current = getSessionUser(request);
    if (current != null && current.getId() != null && current.getId() == id) {
      throw new IllegalArgumentException("You cannot delete your own account while logged in.");
    }

    User existing = userDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found."));
    if ("INACTIVE".equalsIgnoreCase(existing.getStatus()) || "LOCKED".equalsIgnoreCase(existing.getStatus())) {
      throw new IllegalArgumentException("User is already inactive.");
    }

    existing.setStatus("INACTIVE");
    boolean updated = userDAO.update(existing);
    if (!updated) {
      throw new IllegalArgumentException("Unable to deactivate user.");
    }
  }

  private void validateRole(String role) {
    if (!("ADMIN".equals(role) || "STAFF".equals(role) || "GUEST".equals(role))) {
      throw new IllegalArgumentException("Invalid role selected.");
    }
  }

  private void validateStatus(String status) {
    if (!("ACTIVE".equals(status) || "INACTIVE".equals(status) || "LOCKED".equals(status))) {
      throw new IllegalArgumentException("Invalid status selected.");
    }
  }

  private long parseId(String value, String message) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(message);
    }
    try {
      return Long.parseLong(value);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException(message);
    }
  }

  private User getSessionUser(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return null;
    }
    Object user = session.getAttribute(AuthConstants.SESSION_USER);
    return user instanceof User ? (User) user : null;
  }

  private String trimToEmpty(String value) {
    return value == null ? "" : value.trim();
  }

  private void redirectSuccess(HttpServletResponse response, HttpServletRequest request, String message) throws IOException {
    response.sendRedirect(request.getContextPath() + "/admin/users?success=" + encode(message));
  }

  private void redirectError(HttpServletResponse response, HttpServletRequest request, String message) throws IOException {
    response.sendRedirect(request.getContextPath() + "/admin/users?error=" + encode(message));
  }

  private String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }
}
