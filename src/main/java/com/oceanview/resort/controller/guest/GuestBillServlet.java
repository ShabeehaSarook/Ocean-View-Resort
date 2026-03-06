package com.oceanview.resort.controller.guest;

import com.oceanview.resort.dao.BillDAO;
import com.oceanview.resort.dao.ReservationDAO;
import com.oceanview.resort.dao.RoomDAO;
import com.oceanview.resort.dao.impl.BillJdbcDAO;
import com.oceanview.resort.dao.impl.ReservationJdbcDAO;
import com.oceanview.resort.dao.impl.RoomJdbcDAO;
import com.oceanview.resort.exception.DataAccessException;
import com.oceanview.resort.model.Bill;
import com.oceanview.resort.model.GuestProfile;
import com.oceanview.resort.model.Reservation;
import com.oceanview.resort.model.Room;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet("/guest/bills")
public class GuestBillServlet extends GuestBaseServlet {
  private static final String VIEW = "/WEB-INF/views/guest/bills.jsp";

  private final BillDAO billDAO = new BillJdbcDAO();
  private final ReservationDAO reservationDAO = new ReservationJdbcDAO();
  private final RoomDAO roomDAO = new RoomJdbcDAO();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      GuestProfile guestProfile = requireCurrentGuestProfile(request);
      List<Reservation> ownReservations = reservationDAO.findByGuestId(guestProfile.getId());
      List<Bill> allBills = billDAO.findAll();
      List<Room> rooms = roomDAO.findAll();

      Set<Long> ownReservationIds = new HashSet<>();
      Map<Long, Reservation> reservationMap = new HashMap<>();
      for (Reservation reservation : ownReservations) {
        ownReservationIds.add(reservation.getId());
        reservationMap.put(reservation.getId(), reservation);
      }

      Map<Long, Room> roomMap = new HashMap<>();
      for (Room room : rooms) {
        roomMap.put(room.getId(), room);
      }

      List<Bill> ownBills = new ArrayList<>();
      for (Bill bill : allBills) {
        if (ownReservationIds.contains(bill.getReservationId())) {
          ownBills.add(bill);
        }
      }

      Map<Long, String> billReservationLabelMap = new HashMap<>();
      for (Bill bill : ownBills) {
        Reservation reservation = reservationMap.get(bill.getReservationId());
        if (reservation == null) {
          continue;
        }

        Room room = roomMap.get(reservation.getRoomId());
        String roomNumber = room == null ? "N/A" : room.getRoomNumber();
        billReservationLabelMap.put(
          bill.getId(),
          "Reservation #" + reservation.getId() + " - Room " + roomNumber +
            " (" + reservation.getCheckIn() + " to " + reservation.getCheckOut() + ")"
        );
      }

      request.setAttribute("guestProfile", guestProfile);
      request.setAttribute("bills", ownBills);
      request.setAttribute("billReservationLabelMap", billReservationLabelMap);
    } catch (IllegalArgumentException | DataAccessException ex) {
      request.setAttribute("errorMessage", ex.getMessage());
    }

    request.getRequestDispatcher(VIEW).forward(request, response);
  }
}
