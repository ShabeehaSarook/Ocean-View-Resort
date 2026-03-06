package com.oceanview.resort.controller.guest;

import com.oceanview.resort.dao.GuestProfileDAO;
import com.oceanview.resort.dao.impl.GuestProfileJdbcDAO;
import com.oceanview.resort.model.GuestProfile;
import com.oceanview.resort.model.User;
import com.oceanview.resort.util.AuthConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public abstract class GuestBaseServlet extends HttpServlet {
  protected final GuestProfileDAO guestProfileDAO = new GuestProfileJdbcDAO();

  protected User getSessionUser(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return null;
    }
    Object user = session.getAttribute(AuthConstants.SESSION_USER);
    return user instanceof User ? (User) user : null;
  }

  protected GuestProfile requireCurrentGuestProfile(HttpServletRequest request) {
    User user = getSessionUser(request);
    if (user == null || user.getId() == null) {
      throw new IllegalArgumentException("Guest session is not valid.");
    }

    return guestProfileDAO.findByUserId(user.getId())
      .orElseThrow(() -> new IllegalArgumentException("Guest profile not found for current user."));
  }
}
