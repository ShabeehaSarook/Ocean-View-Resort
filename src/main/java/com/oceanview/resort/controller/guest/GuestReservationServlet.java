package com.oceanview.resort.controller.guest;

import com.oceanview.resort.dao.ReservationDAO;
import com.oceanview.resort.dao.RoomDAO;
import com.oceanview.resort.dao.impl.ReservationJdbcDAO;
import com.oceanview.resort.dao.impl.RoomJdbcDAO;
import com.oceanview.resort.exception.DataAccessException;
import com.oceanview.resort.exception.ValidationException;
import com.oceanview.resort.model.GuestProfile;
import com.oceanview.resort.model.Reservation;
import com.oceanview.resort.model.Room;
import com.oceanview.resort.model.User;
import com.oceanview.resort.service.ReservationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet("/guest/reservations")
public class GuestReservationServlet extends GuestBaseServlet {
  private static final String VIEW = "/WEB-INF/views/guest/reservations.jsp";

  private final ReservationDAO reservationDAO = new ReservationJdbcDAO();
  private final RoomDAO roomDAO = new RoomJdbcDAO();
  private final ReservationService reservationService = new ReservationService();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.setAttribute("successMessage", request.getParameter("success"));
    request.setAttribute("errorMessage", request.getParameter("error"));

    try {
      GuestProfile guestProfile = requireCurrentGuestProfile(request);
      List<Reservation> reservations = reservationDAO.findByGuestId(guestProfile.getId());
      List<Room> rooms = roomDAO.findAll();
      Map<Long, String> roomNumberMap = new HashMap<>();
      for (Room room : rooms) {
        roomNumberMap.put(room.getId(), room.getRoomNumber());
      }

      request.setAttribute("guestProfile", guestProfile);
      request.setAttribute("reservations", reservations);
      request.setAttribute("rooms", rooms);
      request.setAttribute("roomNumberMap", roomNumberMap);

      String editIdParam = request.getParameter("editId");
      if (editIdParam != null && !editIdParam.isBlank()) {
        long editId = Long.parseLong(editIdParam);
        Reservation editReservation = requireOwnedReservation(editId, guestProfile.getId());
        request.setAttribute("editReservation", editReservation);
      }
    } catch (IllegalArgumentException | DataAccessException ex) {
      request.setAttribute("errorMessage", ex.getMessage());
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
      if ("cancel".equals(action)) {
        cancelReservation(request);
        redirectSuccess(response, request, "Reservation cancelled successfully.");
        return;
      }

      redirectError(response, request, "Invalid action.");
    } catch (IllegalArgumentException | ValidationException | DataAccessException ex) {
      redirectError(response, request, ex.getMessage());
    }
  }

  private void createReservation(HttpServletRequest request) {
    GuestProfile guestProfile = requireCurrentGuestProfile(request);

    long roomId = parseId(request.getParameter("roomId"), "Room is required.");
    LocalDate checkIn = parseDate(request.getParameter("checkIn"), "Check-in date is invalid.");
    LocalDate checkOut = parseDate(request.getParameter("checkOut"), "Check-out date is invalid.");
    int adults = parseInt(request.getParameter("adults"), "Adults count is invalid.");
    int children = parseInt(request.getParameter("children"), "Children count is invalid.");

    validateGuestReservationInput(roomId, checkIn, checkOut, adults, children);

    if (reservationDAO.existsOverlappingReservation(roomId, checkIn, checkOut, null)) {
      throw new IllegalArgumentException("Selected room is already reserved for the given dates.");
    }

    User sessionUser = getSessionUser(request);
    if (sessionUser == null || sessionUser.getId() == null) {
      throw new IllegalArgumentException("Guest session is not valid.");
    }

    Reservation reservation = new Reservation();
    reservation.setGuestId(guestProfile.getId());
    reservation.setRoomId(roomId);
    reservation.setCheckIn(checkIn);
    reservation.setCheckOut(checkOut);
    reservation.setAdults(adults);
    reservation.setChildren(children);
    reservation.setStatus("PENDING");
    reservation.setBookedBy(sessionUser.getId());

    reservationDAO.create(reservation);
  }

  private void updateReservation(HttpServletRequest request) {
    GuestProfile guestProfile = requireCurrentGuestProfile(request);

    long id = parseId(request.getParameter("id"), "Reservation id is required.");
    Reservation existing = requireOwnedReservation(id, guestProfile.getId());

    String currentStatus = normalizeStatus(existing.getStatus());
    reservationService.ensureGuestModifyAllowed(currentStatus);

    long roomId = parseId(request.getParameter("roomId"), "Room is required.");
    LocalDate checkIn = parseDate(request.getParameter("checkIn"), "Check-in date is invalid.");
    LocalDate checkOut = parseDate(request.getParameter("checkOut"), "Check-out date is invalid.");
    int adults = parseInt(request.getParameter("adults"), "Adults count is invalid.");
    int children = parseInt(request.getParameter("children"), "Children count is invalid.");

    validateGuestReservationInput(roomId, checkIn, checkOut, adults, children);

    if (reservationDAO.existsOverlappingReservation(roomId, checkIn, checkOut, id)) {
      throw new IllegalArgumentException("Selected room is already reserved for the given dates.");
    }

    existing.setRoomId(roomId);
    existing.setCheckIn(checkIn);
    existing.setCheckOut(checkOut);
    existing.setAdults(adults);
    existing.setChildren(children);
    reservationDAO.update(existing);
  }

  private void deleteReservation(HttpServletRequest request) {
    GuestProfile guestProfile = requireCurrentGuestProfile(request);

    long id = parseId(request.getParameter("id"), "Reservation id is required.");
    Reservation existing = requireOwnedReservation(id, guestProfile.getId());

    String currentStatus = normalizeStatus(existing.getStatus());
    if (!("PENDING".equals(currentStatus) || "CONFIRMED".equals(currentStatus) || "CANCELLED".equals(currentStatus))) {
      throw new IllegalArgumentException("Only pending or confirmed reservations can be cancelled.");
    }
    if ("CANCELLED".equals(currentStatus)) {
      throw new IllegalArgumentException("Reservation is already cancelled.");
    }

    existing.setStatus("CANCELLED");
    reservationDAO.update(existing);
  }

  private void cancelReservation(HttpServletRequest request) {
    GuestProfile guestProfile = requireCurrentGuestProfile(request);

    long id = parseId(request.getParameter("id"), "Reservation id is required.");
    Reservation existing = requireOwnedReservation(id, guestProfile.getId());

    String currentStatus = normalizeStatus(existing.getStatus());
    reservationService.ensureGuestModifyAllowed(currentStatus);

    existing.setStatus("CANCELLED");
    reservationDAO.update(existing);
  }

  private Reservation requireOwnedReservation(long reservationId, long guestId) {
    Reservation reservation = reservationDAO.findById(reservationId)
      .orElseThrow(() -> new IllegalArgumentException("Reservation not found."));

    if (reservation.getGuestId() == null || reservation.getGuestId() != guestId) {
      throw new IllegalArgumentException("Access denied: reservation does not belong to current guest.");
    }

    return reservation;
  }

  private void validateGuestReservationInput(long roomId,
                                             LocalDate checkIn,
                                             LocalDate checkOut,
                                             int adults,
                                             int children) {
    Room room = roomDAO.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room not found."));

    String roomStatus = normalizeStatus(room.getStatus());
    if ("INACTIVE".equals(roomStatus) || "MAINTENANCE".equals(roomStatus)) {
      throw new IllegalArgumentException("Selected room is not available for booking.");
    }
    reservationService.validateForCreateOrUpdate("PENDING", checkIn, checkOut, adults, children);
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

  private String normalizeStatus(String value) {
    return value == null ? "" : value.trim().toUpperCase();
  }

  private String trimToEmpty(String value) {
    return value == null ? "" : value.trim();
  }

  private void redirectSuccess(HttpServletResponse response, HttpServletRequest request, String message) throws IOException {
    response.sendRedirect(request.getContextPath() + "/guest/reservations?success=" + encode(message));
  }

  private void redirectError(HttpServletResponse response, HttpServletRequest request, String message) throws IOException {
    response.sendRedirect(request.getContextPath() + "/guest/reservations?error=" + encode(message));
  }

  private String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }
}
