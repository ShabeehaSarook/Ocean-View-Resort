package com.oceanview.resort.controller.guest;

import com.oceanview.resort.dao.BillDAO;
import com.oceanview.resort.dao.ReservationDAO;
import com.oceanview.resort.dao.impl.BillJdbcDAO;
import com.oceanview.resort.dao.impl.ReservationJdbcDAO;
import com.oceanview.resort.exception.DataAccessException;
import com.oceanview.resort.model.Bill;
import com.oceanview.resort.model.GuestProfile;
import com.oceanview.resort.model.Reservation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet("/guest/dashboard")
public class GuestDashboardServlet extends GuestBaseServlet {
  private static final String VIEW = "/WEB-INF/views/guest/dashboard.jsp";

  private final ReservationDAO reservationDAO = new ReservationJdbcDAO();
  private final BillDAO billDAO = new BillJdbcDAO();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      GuestProfile guestProfile = requireCurrentGuestProfile(request);
      List<Reservation> reservations = reservationDAO.findByGuestId(guestProfile.getId());

      int pendingReservations = 0;
      int confirmedReservations = 0;
      int checkedInReservations = 0;
      int checkedOutReservations = 0;
      int cancelledReservations = 0;
      int noShowReservations = 0;

      Set<Long> ownReservationIds = new HashSet<>();
      for (Reservation reservation : reservations) {
        ownReservationIds.add(reservation.getId());
        String status = reservation.getStatus() == null ? "" : reservation.getStatus().toUpperCase();
        if ("PENDING".equals(status)) {
          pendingReservations++;
        } else if ("CONFIRMED".equals(status)) {
          confirmedReservations++;
        } else if ("CHECKED_IN".equals(status)) {
          checkedInReservations++;
        } else if ("CHECKED_OUT".equals(status)) {
          checkedOutReservations++;
        } else if ("CANCELLED".equals(status)) {
          cancelledReservations++;
        } else if ("NO_SHOW".equals(status)) {
          noShowReservations++;
        }
      }

      List<Bill> allBills = billDAO.findAll();
      int totalBills = 0;
      int unpaidBills = 0;
      int partialBills = 0;
      int paidBills = 0;
      int refundedBills = 0;
      int voidBills = 0;
      BigDecimal totalBilledAmount = BigDecimal.ZERO;

      for (Bill bill : allBills) {
        if (!ownReservationIds.contains(bill.getReservationId())) {
          continue;
        }

        totalBills++;
        if (bill.getTotal() != null) {
          totalBilledAmount = totalBilledAmount.add(bill.getTotal());
        }

        String paymentStatus = bill.getPaymentStatus() == null ? "" : bill.getPaymentStatus().toUpperCase();
        if ("UNPAID".equals(paymentStatus)) {
          unpaidBills++;
        } else if ("PARTIAL".equals(paymentStatus)) {
          partialBills++;
        } else if ("PAID".equals(paymentStatus)) {
          paidBills++;
        } else if ("REFUNDED".equals(paymentStatus)) {
          refundedBills++;
        } else if ("VOID".equals(paymentStatus)) {
          voidBills++;
        }
      }

      request.setAttribute("guestProfile", guestProfile);
      request.setAttribute("totalReservations", reservations.size());
      request.setAttribute("pendingReservations", pendingReservations);
      request.setAttribute("confirmedReservations", confirmedReservations);
      request.setAttribute("checkedInReservations", checkedInReservations);
      request.setAttribute("checkedOutReservations", checkedOutReservations);
      request.setAttribute("cancelledReservations", cancelledReservations);
      request.setAttribute("noShowReservations", noShowReservations);

      request.setAttribute("totalBills", totalBills);
      request.setAttribute("totalBilledAmount", totalBilledAmount);
      request.setAttribute("unpaidBills", unpaidBills);
      request.setAttribute("partialBills", partialBills);
      request.setAttribute("paidBills", paidBills);
      request.setAttribute("refundedBills", refundedBills);
      request.setAttribute("voidBills", voidBills);
    } catch (IllegalArgumentException | DataAccessException ex) {
      request.setAttribute("errorMessage", ex.getMessage());
    }

    request.getRequestDispatcher(VIEW).forward(request, response);
  }
}
