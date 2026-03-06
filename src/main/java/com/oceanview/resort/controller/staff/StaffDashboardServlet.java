package com.oceanview.resort.controller.staff;

import com.oceanview.resort.dao.BillDAO;
import com.oceanview.resort.dao.ReservationDAO;
import com.oceanview.resort.dao.impl.BillJdbcDAO;
import com.oceanview.resort.dao.impl.ReservationJdbcDAO;
import com.oceanview.resort.exception.DataAccessException;
import com.oceanview.resort.model.Bill;
import com.oceanview.resort.model.Reservation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/staff/dashboard")
public class StaffDashboardServlet extends HttpServlet {
  private static final String VIEW = "/WEB-INF/views/staff/dashboard.jsp";

  private final ReservationDAO reservationDAO = new ReservationJdbcDAO();
  private final BillDAO billDAO = new BillJdbcDAO();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      List<Reservation> reservations = reservationDAO.findAll();
      List<Bill> bills = billDAO.findAll();

      int pendingReservations = 0;
      int confirmedReservations = 0;
      int checkedInReservations = 0;
      int checkedOutReservations = 0;
      int cancelledReservations = 0;
      int noShowReservations = 0;

      for (Reservation reservation : reservations) {
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

      int unpaidBills = 0;
      int partialBills = 0;
      int paidBills = 0;
      int refundedBills = 0;
      int voidBills = 0;
      BigDecimal totalBilledAmount = BigDecimal.ZERO;

      for (Bill bill : bills) {
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

        if (bill.getTotal() != null) {
          totalBilledAmount = totalBilledAmount.add(bill.getTotal());
        }
      }

      request.setAttribute("totalReservations", reservations.size());
      request.setAttribute("pendingReservations", pendingReservations);
      request.setAttribute("confirmedReservations", confirmedReservations);
      request.setAttribute("checkedInReservations", checkedInReservations);
      request.setAttribute("checkedOutReservations", checkedOutReservations);
      request.setAttribute("cancelledReservations", cancelledReservations);
      request.setAttribute("noShowReservations", noShowReservations);

      request.setAttribute("totalBills", bills.size());
      request.setAttribute("totalBilledAmount", totalBilledAmount);
      request.setAttribute("unpaidBills", unpaidBills);
      request.setAttribute("partialBills", partialBills);
      request.setAttribute("paidBills", paidBills);
      request.setAttribute("refundedBills", refundedBills);
      request.setAttribute("voidBills", voidBills);
    } catch (DataAccessException ex) {
      request.setAttribute("errorMessage", "Unable to load staff dashboard data: " + ex.getMessage());
    }

    request.getRequestDispatcher(VIEW).forward(request, response);
  }
}
