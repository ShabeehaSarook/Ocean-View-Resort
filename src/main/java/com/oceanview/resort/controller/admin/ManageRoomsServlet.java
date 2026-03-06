package com.oceanview.resort.controller.admin;

import com.oceanview.resort.dao.RoomDAO;
import com.oceanview.resort.dao.RoomTypeDAO;
import com.oceanview.resort.dao.impl.RoomJdbcDAO;
import com.oceanview.resort.dao.impl.RoomTypeJdbcDAO;
import com.oceanview.resort.exception.DataAccessException;
import com.oceanview.resort.model.Room;
import com.oceanview.resort.model.RoomType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet("/admin/rooms")
public class ManageRoomsServlet extends HttpServlet {
  private static final String VIEW = "/WEB-INF/views/admin/manage-rooms.jsp";

  private final RoomDAO roomDAO = new RoomJdbcDAO();
  private final RoomTypeDAO roomTypeDAO = new RoomTypeJdbcDAO();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.setAttribute("successMessage", request.getParameter("success"));
    request.setAttribute("errorMessage", request.getParameter("error"));

    try {
      List<Room> rooms = roomDAO.findAll();
      List<RoomType> roomTypes = roomTypeDAO.findAll();

      Map<Long, String> roomTypeNameMap = new HashMap<>();
      for (RoomType roomType : roomTypes) {
        roomTypeNameMap.put(roomType.getId(), roomType.getName());
      }

      request.setAttribute("rooms", rooms);
      request.setAttribute("roomTypes", roomTypes);
      request.setAttribute("roomTypeNameMap", roomTypeNameMap);

      String editIdParam = request.getParameter("editId");
      if (editIdParam != null && !editIdParam.isBlank()) {
        long editId = Long.parseLong(editIdParam);
        Optional<Room> editRoom = roomDAO.findById(editId);
        editRoom.ifPresent(room -> request.setAttribute("editRoom", room));
      }
    } catch (Exception ex) {
      request.setAttribute("errorMessage", "Unable to load rooms: " + ex.getMessage());
    }

    request.getRequestDispatcher(VIEW).forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String action = trimToEmpty(request.getParameter("action"));

    try {
      if ("create".equals(action)) {
        createRoom(request);
        redirectSuccess(response, request, "Room created successfully.");
        return;
      }
      if ("update".equals(action)) {
        updateRoom(request);
        redirectSuccess(response, request, "Room updated successfully.");
        return;
      }
      if ("delete".equals(action)) {
        deleteRoom(request);
        redirectSuccess(response, request, "Room marked inactive successfully.");
        return;
      }

      redirectError(response, request, "Invalid action.");
    } catch (IllegalArgumentException | DataAccessException ex) {
      redirectError(response, request, ex.getMessage());
    }
  }

  private void createRoom(HttpServletRequest request) {
    String roomNumber = trimToEmpty(request.getParameter("roomNumber"));
    long roomTypeId = parseId(request.getParameter("roomTypeId"), "Room type is required.");
    String status = trimToEmpty(request.getParameter("status")).toUpperCase();
    int floor = parseInt(request.getParameter("floor"), "Floor is invalid.");

    if (roomNumber.isEmpty()) {
      throw new IllegalArgumentException("Room number is required.");
    }
    validateRoomStatus(status);
    roomTypeDAO.findById(roomTypeId).orElseThrow(() -> new IllegalArgumentException("Invalid room type."));

    Room room = new Room();
    room.setRoomNumber(roomNumber);
    room.setRoomTypeId(roomTypeId);
    room.setStatus(status);
    room.setFloor(floor);
    roomDAO.create(room);
  }

  private void updateRoom(HttpServletRequest request) {
    long id = parseId(request.getParameter("id"), "Room id is required.");
    String roomNumber = trimToEmpty(request.getParameter("roomNumber"));
    long roomTypeId = parseId(request.getParameter("roomTypeId"), "Room type is required.");
    String status = trimToEmpty(request.getParameter("status")).toUpperCase();
    int floor = parseInt(request.getParameter("floor"), "Floor is invalid.");

    if (roomNumber.isEmpty()) {
      throw new IllegalArgumentException("Room number is required.");
    }
    validateRoomStatus(status);
    roomTypeDAO.findById(roomTypeId).orElseThrow(() -> new IllegalArgumentException("Invalid room type."));

    Room existing = roomDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("Room not found."));
    existing.setRoomNumber(roomNumber);
    existing.setRoomTypeId(roomTypeId);
    existing.setStatus(status);
    existing.setFloor(floor);
    roomDAO.update(existing);
  }

  private void deleteRoom(HttpServletRequest request) {
    long id = parseId(request.getParameter("id"), "Room id is required.");
    Room existing = roomDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("Room not found."));
    if ("INACTIVE".equalsIgnoreCase(existing.getStatus())) {
      throw new IllegalArgumentException("Room is already inactive.");
    }
    existing.setStatus("INACTIVE");
    boolean updated = roomDAO.update(existing);
    if (!updated) {
      throw new IllegalArgumentException("Unable to mark room inactive.");
    }
  }

  private void validateRoomStatus(String status) {
    if (!("AVAILABLE".equals(status)
      || "OCCUPIED".equals(status)
      || "MAINTENANCE".equals(status)
      || "INACTIVE".equals(status))) {
      throw new IllegalArgumentException("Invalid room status selected.");
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

  private String trimToEmpty(String value) {
    return value == null ? "" : value.trim();
  }

  private void redirectSuccess(HttpServletResponse response, HttpServletRequest request, String message) throws IOException {
    response.sendRedirect(request.getContextPath() + "/admin/rooms?success=" + encode(message));
  }

  private void redirectError(HttpServletResponse response, HttpServletRequest request, String message) throws IOException {
    response.sendRedirect(request.getContextPath() + "/admin/rooms?error=" + encode(message));
  }

  private String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }
}
