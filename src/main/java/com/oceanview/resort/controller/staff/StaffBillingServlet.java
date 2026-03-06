package com.oceanview.resort.controller.staff;

import com.oceanview.resort.dao.BillDAO;
import com.oceanview.resort.dao.GuestProfileDAO;
import com.oceanview.resort.dao.ReservationDAO;
import com.oceanview.resort.dao.RoomDAO;
import com.oceanview.resort.dao.impl.BillJdbcDAO;
import com.oceanview.resort.dao.impl.GuestProfileJdbcDAO;
import com.oceanview.resort.dao.impl.ReservationJdbcDAO;
import com.oceanview.resort.dao.impl.RoomJdbcDAO;
import com.oceanview.resort.exception.DataAccessException;
import com.oceanview.resort.exception.ValidationException;
import com.oceanview.resort.model.Bill;
import com.oceanview.resort.model.GuestProfile;
import com.oceanview.resort.model.Reservation;
import com.oceanview.resort.model.Room;
import com.oceanview.resort.model.User;
import com.oceanview.resort.service.BillingService;
import com.oceanview.resort.util.AuthConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/staff/billing")
public class StaffBillingServlet extends HttpServlet {
  private static final String VIEW = "/WEB-INF/views/staff/billing.jsp";

  private final BillDAO billDAO = new BillJdbcDAO();
  private final ReservationDAO reservationDAO = new ReservationJdbcDAO();
  private final GuestProfileDAO guestProfileDAO = new GuestProfileJdbcDAO();
  private final RoomDAO roomDAO = new RoomJdbcDAO();
  private final BillingService billingService = new BillingService();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.setAttribute("successMessage", request.getParameter("success"));
    request.setAttribute("errorMessage", request.getParameter("error"));

    try {
      List<Bill> bills = billDAO.findAll();
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

      Map<Long, String> reservationLabelMap = new HashMap<>();
      for (Reservation reservation : reservations) {
        String guestName = guestNameMap.getOrDefault(reservation.getGuestId(), "Guest #" + reservation.getGuestId());
        String roomNumber = roomNumberMap.getOrDefault(reservation.getRoomId(), "Room #" + reservation.getRoomId());
        reservationLabelMap.put(
          reservation.getId(),
          "Reservation #" + reservation.getId() + " - " + guestName + " - Room " + roomNumber +
            " (" + reservation.getCheckIn() + " to " + reservation.getCheckOut() + ")"
        );
      }

      request.setAttribute("bills", bills);
      request.setAttribute("reservations", reservations);
      request.setAttribute("reservationLabelMap", reservationLabelMap);

      String editIdParam = request.getParameter("editId");
      if (editIdParam != null && !editIdParam.isBlank()) {
        long editId = Long.parseLong(editIdParam);
        billDAO.findById(editId).ifPresent(bill -> request.setAttribute("editBill", bill));
      }
    } catch (Exception ex) {
      request.setAttribute("errorMessage", "Unable to load billing data: " + ex.getMessage());
    }

    request.getRequestDispatcher(VIEW).forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String action = trimToEmpty(request.getParameter("action"));

    try {
      if ("create".equals(action)) {
        createBill(request);
        redirectSuccess(response, request, "Bill generated successfully.");
        return;
      }
      if ("update".equals(action)) {
        updateBill(request);
        redirectSuccess(response, request, "Bill updated successfully.");
        return;
      }
      if ("delete".equals(action)) {
        deleteBill(request);
        redirectSuccess(response, request, "Bill voided successfully.");
        return;
      }

      redirectError(response, request, "Invalid action.");
    } catch (IllegalArgumentException | ValidationException | DataAccessException ex) {
      redirectError(response, request, ex.getMessage());
    }
  }

  private void createBill(HttpServletRequest request) {
    long reservationId = parseId(request.getParameter("reservationId"), "Reservation is required.");
    BigDecimal subtotal = parseDecimal(request.getParameter("subtotal"), "Subtotal is invalid.");
    BigDecimal tax = parseDecimal(request.getParameter("tax"), "Tax is invalid.");
    BigDecimal discount = parseDecimal(request.getParameter("discount"), "Discount is invalid.");
    String paymentStatus = normalizePaymentStatus(request.getParameter("paymentStatus"));

    validateBillFields(reservationId, subtotal, tax, discount, paymentStatus);

    if (billDAO.findByReservationId(reservationId).isPresent()) {
      throw new IllegalArgumentException("A bill already exists for this reservation.");
    }

    User staffUser = getSessionUser(request);
    if (staffUser == null || staffUser.getId() == null) {
      throw new IllegalArgumentException("Staff session is not valid.");
    }

    Bill bill = new Bill();
    bill.setReservationId(reservationId);
    bill.setSubtotal(subtotal);
    bill.setTax(tax);
    bill.setDiscount(discount);
    bill.setTotal(calculateTotal(subtotal, tax, discount));
    bill.setIssuedBy(staffUser.getId());
    bill.setPaymentStatus(paymentStatus);

    billDAO.create(bill);
  }

  private void updateBill(HttpServletRequest request) {
    long id = parseId(request.getParameter("id"), "Bill id is required.");
    BigDecimal subtotal = parseDecimal(request.getParameter("subtotal"), "Subtotal is invalid.");
    BigDecimal tax = parseDecimal(request.getParameter("tax"), "Tax is invalid.");
    BigDecimal discount = parseDecimal(request.getParameter("discount"), "Discount is invalid.");
    String paymentStatus = normalizePaymentStatus(request.getParameter("paymentStatus"));

    Bill existing = billDAO.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Bill not found."));

    validateBillFields(existing.getReservationId(), subtotal, tax, discount, paymentStatus);

    existing.setSubtotal(subtotal);
    existing.setTax(tax);
    existing.setDiscount(discount);
    existing.setTotal(calculateTotal(subtotal, tax, discount));
    existing.setPaymentStatus(paymentStatus);

    billDAO.update(existing);
  }

  private void deleteBill(HttpServletRequest request) {
    long id = parseId(request.getParameter("id"), "Bill id is required.");
    Bill existing = billDAO.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Bill not found."));

    if ("VOID".equalsIgnoreCase(existing.getPaymentStatus())) {
      throw new IllegalArgumentException("Bill is already void.");
    }

    existing.setPaymentStatus("VOID");
    boolean updated = billDAO.update(existing);
    if (!updated) {
      throw new IllegalArgumentException("Unable to void bill.");
    }
  }

  private void validateBillFields(long reservationId,
                                  BigDecimal subtotal,
                                  BigDecimal tax,
                                  BigDecimal discount,
                                  String paymentStatus) {
    reservationDAO.findById(reservationId)
      .orElseThrow(() -> new IllegalArgumentException("Reservation not found."));
    billingService.validateForCreateOrUpdate(subtotal, tax, discount, paymentStatus);
  }

  private BigDecimal calculateTotal(BigDecimal subtotal, BigDecimal tax, BigDecimal discount) {
    return billingService.calculateTotal(subtotal, tax, discount);
  }

  private String normalizePaymentStatus(String value) {
    return billingService.normalizePaymentStatus(value);
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

  private BigDecimal parseDecimal(String value, String message) {
    try {
      return new BigDecimal(trimToEmpty(value));
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
    response.sendRedirect(request.getContextPath() + "/staff/billing?success=" + encode(message));
  }

  private void redirectError(HttpServletResponse response, HttpServletRequest request, String message) throws IOException {
    response.sendRedirect(request.getContextPath() + "/staff/billing?error=" + encode(message));
  }

  private String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }
}
