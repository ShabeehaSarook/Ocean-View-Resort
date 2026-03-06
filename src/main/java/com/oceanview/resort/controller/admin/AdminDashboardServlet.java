package com.oceanview.resort.controller.admin;

import com.oceanview.resort.dao.BillDAO;
import com.oceanview.resort.dao.ReservationDAO;
import com.oceanview.resort.dao.RoomDAO;
import com.oceanview.resort.dao.UserDAO;
import com.oceanview.resort.dao.impl.BillJdbcDAO;
import com.oceanview.resort.dao.impl.ReservationJdbcDAO;
import com.oceanview.resort.dao.impl.RoomJdbcDAO;
import com.oceanview.resort.dao.impl.UserJdbcDAO;
import com.oceanview.resort.exception.DataAccessException;
import com.oceanview.resort.model.Bill;
import com.oceanview.resort.model.Room;
import com.oceanview.resort.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
  private static final String VIEW = "/WEB-INF/views/admin/dashboard.jsp";

  private final UserDAO userDAO = new UserJdbcDAO();
  private final RoomDAO roomDAO = new RoomJdbcDAO();
  private final ReservationDAO reservationDAO = new ReservationJdbcDAO();
  private final BillDAO billDAO = new BillJdbcDAO();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      List<User> users = userDAO.findAll();
      List<Room> rooms = roomDAO.findAll();
      int reservationsCount = reservationDAO.findAll().size();
      List<Bill> bills = billDAO.findAll();

      int adminUsers = 0;
      int staffUsers = 0;
      int guestUsers = 0;
      for (User user : users) {
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
          adminUsers++;
        } else if ("STAFF".equalsIgnoreCase(user.getRole())) {
          staffUsers++;
        } else if ("GUEST".equalsIgnoreCase(user.getRole())) {
          guestUsers++;
        }
      }

      int availableRooms = 0;
      int occupiedRooms = 0;
      int maintenanceRooms = 0;
      int inactiveRooms = 0;
      for (Room room : rooms) {
        if ("AVAILABLE".equalsIgnoreCase(room.getStatus())) {
          availableRooms++;
        } else if ("OCCUPIED".equalsIgnoreCase(room.getStatus())) {
          occupiedRooms++;
        } else if ("MAINTENANCE".equalsIgnoreCase(room.getStatus())) {
          maintenanceRooms++;
        } else if ("INACTIVE".equalsIgnoreCase(room.getStatus())) {
          inactiveRooms++;
        }
      }

      BigDecimal totalRevenue = BigDecimal.ZERO;
      for (Bill bill : bills) {
        if (bill.getTotal() != null) {
          totalRevenue = totalRevenue.add(bill.getTotal());
        }
      }

      request.setAttribute("totalUsers", users.size());
      request.setAttribute("totalRooms", rooms.size());
      request.setAttribute("totalReservations", reservationsCount);
      request.setAttribute("totalRevenue", totalRevenue);

      request.setAttribute("adminUsers", adminUsers);
      request.setAttribute("staffUsers", staffUsers);
      request.setAttribute("guestUsers", guestUsers);

      request.setAttribute("availableRooms", availableRooms);
      request.setAttribute("occupiedRooms", occupiedRooms);
      request.setAttribute("maintenanceRooms", maintenanceRooms);
      request.setAttribute("inactiveRooms", inactiveRooms);
    } catch (DataAccessException ex) {
      request.setAttribute("errorMessage", "Unable to load dashboard data: " + ex.getMessage());
    }

    request.getRequestDispatcher(VIEW).forward(request, response);
  }
}
