package com.oceanview.resort.controller;

import com.oceanview.resort.dao.GuestProfileDAO;
import com.oceanview.resort.dao.UserDAO;
import com.oceanview.resort.dao.impl.GuestProfileJdbcDAO;
import com.oceanview.resort.dao.impl.UserJdbcDAO;
import com.oceanview.resort.exception.DataAccessException;
import com.oceanview.resort.exception.ValidationException;
import com.oceanview.resort.model.GuestProfile;
import com.oceanview.resort.model.User;
import com.oceanview.resort.service.RegisterService;
import com.oceanview.resort.util.AuthConstants;
import com.oceanview.resort.util.AuthRedirectUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
  private static final String REGISTER_VIEW = "/WEB-INF/views/common/register.jsp";

  private final UserDAO userDAO = new UserJdbcDAO();
  private final GuestProfileDAO guestProfileDAO = new GuestProfileJdbcDAO();
  private final RegisterService registerService = new RegisterService();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    User currentUser = getSessionUser(request);
    if (currentUser != null) {
      AuthRedirectUtil.sendRoleHomeRedirect(request, response, currentUser.getRole());
      return;
    }

    request.getRequestDispatcher(REGISTER_VIEW).forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    User currentUser = getSessionUser(request);
    if (currentUser != null) {
      AuthRedirectUtil.sendRoleHomeRedirect(request, response, currentUser.getRole());
      return;
    }

    String username = trimToEmpty(request.getParameter("username"));
    String password = trimToEmpty(request.getParameter("password"));
    String confirmPassword = trimToEmpty(request.getParameter("confirmPassword"));
    String fullName = trimToEmpty(request.getParameter("fullName"));
    String email = trimToEmpty(request.getParameter("email"));
    String phone = trimToEmpty(request.getParameter("phone"));
    String nicPassport = trimToEmpty(request.getParameter("nicPassport"));
    String address = trimToEmpty(request.getParameter("address"));

    try {
      registerService.validateGuestRegistration(
        username, password, confirmPassword, fullName, email, phone, nicPassport
      );
    } catch (ValidationException ex) {
      attachFormValues(request, username, fullName, email, phone, nicPassport, address);
      request.setAttribute("errorMessage", ex.getMessage());
      request.getRequestDispatcher(REGISTER_VIEW).forward(request, response);
      return;
    }

    if (userDAO.findByUsername(username).isPresent()) {
      attachFormValues(request, username, fullName, email, phone, nicPassport, address);
      request.setAttribute("errorMessage", "Username already exists.");
      request.getRequestDispatcher(REGISTER_VIEW).forward(request, response);
      return;
    }

    User user = new User();
    user.setUsername(username);
    user.setPasswordHash(registerService.hashPassword(password));
    user.setRole("GUEST");
    user.setStatus("ACTIVE");

    long userId = 0;
    try {
      userId = userDAO.create(user);

      GuestProfile guestProfile = new GuestProfile();
      guestProfile.setUserId(userId);
      guestProfile.setFullName(fullName);
      guestProfile.setEmail(email);
      guestProfile.setPhone(phone);
      guestProfile.setNicPassport(nicPassport);
      guestProfile.setAddress(address);

      guestProfileDAO.create(guestProfile);

      response.sendRedirect(request.getContextPath() + "/login?registered=1");
    } catch (DataAccessException ex) {
      if (userId > 0) {
        try {
          userDAO.deleteById(userId);
        } catch (DataAccessException ignored) {
          // Best-effort rollback for partially created user record.
        }
      }
      attachFormValues(request, username, fullName, email, phone, nicPassport, address);
      request.setAttribute("errorMessage", "Registration failed. Ensure username/email/NIC are unique.");
      request.getRequestDispatcher(REGISTER_VIEW).forward(request, response);
    }
  }

  private void attachFormValues(HttpServletRequest request,
                                String username,
                                String fullName,
                                String email,
                                String phone,
                                String nicPassport,
                                String address) {
    request.setAttribute("username", username);
    request.setAttribute("fullName", fullName);
    request.setAttribute("email", email);
    request.setAttribute("phone", phone);
    request.setAttribute("nicPassport", nicPassport);
    request.setAttribute("address", address);
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
