package com.oceanview.resort.controller.staff;

import com.oceanview.resort.dao.GuestProfileDAO;
import com.oceanview.resort.dao.ReservationDAO;
import com.oceanview.resort.dao.RoomDAO;
import com.oceanview.resort.dao.impl.GuestProfileJdbcDAO;
import com.oceanview.resort.dao.impl.ReservationJdbcDAO;
import com.oceanview.resort.dao.impl.RoomJdbcDAO;
import com.oceanview.resort.exception.DataAccessException;
import com.oceanview.resort.exception.ValidationException;
import com.oceanview.resort.model.GuestProfile;
import com.oceanview.resort.model.Reservation;
import com.oceanview.resort.model.Room;
import com.oceanview.resort.model.User;
import com.oceanview.resort.service.ReservationService;
import com.oceanview.resort.util.AuthConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet("/staff/reservations")
public class StaffReservationServlet extends HttpServlet {
  private static final String VIEW = "/WEB-INF/views/staff/reservations.jsp";

  private final ReservationDAO reservationDAO = new ReservationJdbcDAO();
  private final GuestProfileDAO guestProfileDAO = new GuestProfileJdbcDAO();
  private final RoomDAO roomDAO = new RoomJdbcDAO();
  private final ReservationService reservationService = new ReservationService();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.setAttribute("successMessage", request.getParameter("success"));
    request.setAttribute("errorMessage", request.getParameter("error"));

    try {
      List<Reservation> reservations = reservationDAO.findAll();
      List<GuestProfile> guests = guestProfileDAO.findAll();
      List<Room> rooms = roomDAO.findAll();

      Map<Long, String> guestNameMap = new HashMap<>();
      for (GuestProfile guest : guests) {
        guestNameMap.put(guest.getId(), guest.getFullName());
      }

      Map<Long, String> roomNumberMap = new HashMap<>();
      for (Room room : rooms) {
        roomNumberMap.put(room.getId(), room.getRoomNumber());
      }

      request.setAttribute("reservations", reservations);
      request.setAttribute("guests", guests);
      request.setAttribute("rooms", rooms);
      request.setAttribute("guestNameMap", guestNameMap);
      request.setAttribute("roomNumberMap", roomNumberMap);

      String editIdParam = request.getParameter("editId");
      if (editIdParam != null && !editIdParam.isBlank()) {
        long editId = Long.parseLong(editIdParam);
        Optional<Reservation> editReservation = reservationDAO.findById(editId);
        editReservation.ifPresent(reservation -> request.setAttribute("editReservation", reservation));
      }
    } catch (Exception ex) {
      request.setAttribute("errorMessage", "Unable to load reservations: " + ex.getMessage());
    }

    request.getRequestDispatcher(VIEW).forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String action = trimToEmpty(request.getParameter("action"));

    try {
      if ("create".equals(action)) {
        createReservation(request);
        redirectSuccess(response, request, "Reservation created successfully.");
        return;
      }
      if ("update".equals(action)) {
        updateReservation(request);
        redirectSuccess(response, request, "Reservation updated successfully.");
        return;
      }
      if ("delete".equals(action)) {
        deleteReservation(request);
        redirectSuccess(response, request, "Reservation cancelled successfully.");
        return;
      }
      if ("checkin".equals(action)) {
        checkInReservation(request);
        redirectSuccess(response, request, "Reservation checked in successfully.");
        return;
      }
      if ("checkout".equals(action)) {
        checkOutReservation(request);
        redirectSuccess(response, request, "Reservation checked out successfully.");
        return;
      }

      redirectError(response, request, "Invalid action.");
    } catch (IllegalArgumentException | ValidationException | DataAccessException ex) {
      redirectError(response, request, ex.getMessage());
    }
  }

  private void createReservation(HttpServletRequest request) {
    long guestId = parseId(request.getParameter("guestId"), "Guest is required.");
    long roomId = parseId(request.getParameter("roomId"), "Room is required.");
    LocalDate checkIn = parseDate(request.getParameter("checkIn"), "Check-in date is invalid.");
    LocalDate checkOut = parseDate(request.getParameter("checkOut"), "Check-out date is invalid.");
    int adults = parseInt(request.getParameter("adults"), "Adults count is invalid.");
    int children = parseInt(request.getParameter("children"), "Children count is invalid.");
    String status = normalizeReservationStatus(request.getParameter("status"));

    validateReservationInput(guestId, roomId, checkIn, checkOut, adults, children, status);

    if (reservationService.isActiveReservationStatus(status)
      && reservationDAO.existsOverlappingReservation(roomId, checkIn, checkOut, null)) {
      throw new IllegalArgumentException("Selected room is already reserved for the given dates.");
    }

    User staffUser = getSessionUser(request);
    if (staffUser == null || staffUser.getId() == null) {
      throw new IllegalArgumentException("Staff session is not valid.");
    }

    Reservation reservation = new Reservation();
    reservation.setGuestId(guestId);
    reservation.setRoomId(roomId);
    reservation.setCheckIn(checkIn);
    reservation.setCheckOut(checkOut);
    reservation.setAdults(adults);
    reservation.setChildren(children);
    reservation.setStatus(status);
    reservation.setBookedBy(staffUser.getId());

    reservationDAO.create(reservation);
  }

  private void updateReservation(HttpServletRequest request) {
    long id = parseId(request.getParameter("id"), "Reservation id is required.");
    long guestId = parseId(request.getParameter("guestId"), "Guest is required.");
    long roomId = parseId(request.getParameter("roomId"), "Room is required.");
    LocalDate checkIn = parseDate(request.getParameter("checkIn"), "Check-in date is invalid.");
    LocalDate checkOut = parseDate(request.getParameter("checkOut"), "Check-out date is invalid.");
    int adults = parseInt(request.getParameter("adults"), "Adults count is invalid.");
    int children = parseInt(request.getParameter("children"), "Children count is invalid.");
    String status = normalizeReservationStatus(request.getParameter("status"));

    validateReservationInput(guestId, roomId, checkIn, checkOut, adults, children, status);

    Reservation existing = reservationDAO.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Reservation not found."));

    if (reservationService.isActiveReservationStatus(status)
      && reservationDAO.existsOverlappingReservation(roomId, checkIn, checkOut, id)) {
      throw new IllegalArgumentException("Selected room is already reserved for the given dates.");
    }

    existing.setGuestId(guestId);
    existing.setRoomId(roomId);
    existing.setCheckIn(checkIn);
    existing.setCheckOut(checkOut);
    existing.setAdults(adults);
    existing.setChildren(children);
    existing.setStatus(status);

    reservationDAO.update(existing);
    if ("CHECKED_IN".equals(status)) {
      updateRoomOccupancyStatus(existing.getRoomId(), "OCCUPIED");
    } else if ("CHECKED_OUT".equals(status)) {
      updateRoomOccupancyStatus(existing.getRoomId(), "AVAILABLE");
    }
  }

  private void deleteReservation(HttpServletRequest request) {
    long id = parseId(request.getParameter("id"), "Reservation id is required.");
    Reservation existing = reservationDAO.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Reservation not found."));

    String currentStatus = existing.getStatus() == null ? "" : existing.getStatus().toUpperCase();
    if ("CHECKED_OUT".equals(currentStatus) || "NO_SHOW".equals(currentStatus)) {
      throw new IllegalArgumentException("Completed reservations cannot be cancelled.");
    }
    if ("CANCELLED".equals(currentStatus)) {
      throw new IllegalArgumentException("Reservation is already cancelled.");
    }

    existing.setStatus("CANCELLED");
    reservationDAO.update(existing);

    if ("CHECKED_IN".equals(currentStatus)) {
      updateRoomOccupancyStatus(existing.getRoomId(), "AVAILABLE");
    }
  }

  private void checkInReservation(HttpServletRequest request) {
    long id = parseId(request.getParameter("id"), "Reservation id is required.");
    Reservation reservation = reservationDAO.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Reservation not found."));

    String currentStatus = reservation.getStatus() == null ? "" : reservation.getStatus().toUpperCase();
    reservationService.ensureCheckInAllowed(currentStatus);

    reservation.setStatus("CHECKED_IN");
    reservationDAO.update(reservation);
    updateRoomOccupancyStatus(reservation.getRoomId(), "OCCUPIED");
  }

  private void checkOutReservation(HttpServletRequest request) {
    long id = parseId(request.getParameter("id"), "Reservation id is required.");
    Reservation reservation = reservationDAO.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Reservation not found."));

    String currentStatus = reservation.getStatus() == null ? "" : reservation.getStatus().toUpperCase();
    reservationService.ensureCheckOutAllowed(currentStatus);

    reservation.setStatus("CHECKED_OUT");
    reservationDAO.update(reservation);
    updateRoomOccupancyStatus(reservation.getRoomId(), "AVAILABLE");
  }

  private void updateRoomOccupancyStatus(Long roomId, String status) {
    if (roomId == null) {
      return;
    }
    roomDAO.findById(roomId).ifPresent(room -> {
      room.setStatus(status);
      roomDAO.update(room);
    });
  }

  private void validateReservationInput(long guestId,
                                        long roomId,
                                        LocalDate checkIn,
                                        LocalDate checkOut,
                                        int adults,
                                        int children,
                                        String status) {
    guestProfileDAO.findById(guestId).orElseThrow(() -> new IllegalArgumentException("Guest not found."));
    roomDAO.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room not found."));
    reservationService.validateForCreateOrUpdate(status, checkIn, checkOut, adults, children);
  }

  private String normalizeReservationStatus(String value) {
    String status = trimToEmpty(value).toUpperCase();
    return status.isEmpty() ? "CONFIRMED" : status;
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

  private LocalDate parseDate(String value, String message) {
    try {
      return LocalDate.parse(trimToEmpty(value));
    } catch (DateTimeParseException ex) {
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
    response.sendRedirect(request.getContextPath() + "/staff/reservations?success=" + encode(message));
  }

  private void redirectError(HttpServletResponse response, HttpServletRequest request, String message) throws IOException {
    response.sendRedirect(request.getContextPath() + "/staff/reservations?error=" + encode(message));
  }

  private String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }
}
