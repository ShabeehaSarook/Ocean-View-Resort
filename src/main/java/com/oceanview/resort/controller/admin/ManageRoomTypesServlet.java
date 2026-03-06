package com.oceanview.resort.controller.admin;

import com.oceanview.resort.dao.RoomTypeDAO;
import com.oceanview.resort.dao.impl.RoomTypeJdbcDAO;
import com.oceanview.resort.exception.DataAccessException;
import com.oceanview.resort.model.RoomType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebServlet("/admin/roomtypes")
public class ManageRoomTypesServlet extends HttpServlet {
  private static final String VIEW = "/WEB-INF/views/admin/manage-roomtypes.jsp";

  private final RoomTypeDAO roomTypeDAO = new RoomTypeJdbcDAO();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.setAttribute("successMessage", request.getParameter("success"));
    request.setAttribute("errorMessage", request.getParameter("error"));

    try {
      request.setAttribute("roomTypes", roomTypeDAO.findAll());

      String editIdParam = request.getParameter("editId");
      if (editIdParam != null && !editIdParam.isBlank()) {
        long editId = Long.parseLong(editIdParam);
        Optional<RoomType> editRoomType = roomTypeDAO.findById(editId);
        editRoomType.ifPresent(roomType -> request.setAttribute("editRoomType", roomType));
      }
    } catch (Exception ex) {
      request.setAttribute("errorMessage", "Unable to load room types: " + ex.getMessage());
    }

    request.getRequestDispatcher(VIEW).forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String action = trimToEmpty(request.getParameter("action"));

    try {
      if ("create".equals(action)) {
        createRoomType(request);
        redirectSuccess(response, request, "Room type created successfully.");
        return;
      }
      if ("update".equals(action)) {
        updateRoomType(request);
        redirectSuccess(response, request, "Room type updated successfully.");
        return;
      }
      if ("delete".equals(action)) {
        deleteRoomType(request);
        redirectSuccess(response, request, "Room type deleted successfully.");
        return;
      }

      redirectError(response, request, "Invalid action.");
    } catch (IllegalArgumentException | DataAccessException ex) {
      redirectError(response, request, ex.getMessage());
    }
  }

  private void createRoomType(HttpServletRequest request) {
    String name = trimToEmpty(request.getParameter("name"));
    BigDecimal basePrice = parseDecimal(request.getParameter("basePrice"), "Base price is invalid.");
    int capacity = parseInt(request.getParameter("capacity"), "Capacity is invalid.");
    String description = trimToEmpty(request.getParameter("description"));

    if (name.isEmpty()) {
      throw new IllegalArgumentException("Room type name is required.");
    }
    if (capacity <= 0) {
      throw new IllegalArgumentException("Capacity must be greater than zero.");
    }
    if (basePrice.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Base price cannot be negative.");
    }

    RoomType roomType = new RoomType();
    roomType.setName(name);
    roomType.setBasePrice(basePrice);
    roomType.setCapacity(capacity);
    roomType.setDescription(description);
    roomTypeDAO.create(roomType);
  }

  private void updateRoomType(HttpServletRequest request) {
    long id = parseId(request.getParameter("id"), "Room type id is required.");
    String name = trimToEmpty(request.getParameter("name"));
    BigDecimal basePrice = parseDecimal(request.getParameter("basePrice"), "Base price is invalid.");
    int capacity = parseInt(request.getParameter("capacity"), "Capacity is invalid.");
    String description = trimToEmpty(request.getParameter("description"));

    if (name.isEmpty()) {
      throw new IllegalArgumentException("Room type name is required.");
    }
    if (capacity <= 0) {
      throw new IllegalArgumentException("Capacity must be greater than zero.");
    }
    if (basePrice.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Base price cannot be negative.");
    }

    RoomType existing = roomTypeDAO.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Room type not found."));

    existing.setName(name);
    existing.setBasePrice(basePrice);
    existing.setCapacity(capacity);
    existing.setDescription(description);
    roomTypeDAO.update(existing);
  }

  private void deleteRoomType(HttpServletRequest request) {
    long id = parseId(request.getParameter("id"), "Room type id is required.");
    boolean deleted = roomTypeDAO.deleteById(id);
    if (!deleted) {
      throw new IllegalArgumentException("Room type not found or already deleted.");
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

  private int parseInt(String value, String message) {
    try {
      return Integer.parseInt(trimToEmpty(value));
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException(message);
    }
  }

  private BigDecimal parseDecimal(String value, String message) {
    try {
      return new BigDecimal(trimToEmpty(value));
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException(message);
    }
  }

  private String trimToEmpty(String value) {
    return value == null ? "" : value.trim();
  }

  private void redirectSuccess(HttpServletResponse response, HttpServletRequest request, String message) throws IOException {
    response.sendRedirect(request.getContextPath() + "/admin/roomtypes?success=" + encode(message));
  }

  private void redirectError(HttpServletResponse response, HttpServletRequest request, String message) throws IOException {
    response.sendRedirect(request.getContextPath() + "/admin/roomtypes?error=" + encode(message));
  }

  private String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }
}
