package com.oceanview.resort.api;

import com.oceanview.resort.dao.BillDAO;
import com.oceanview.resort.dao.GuestProfileDAO;
import com.oceanview.resort.dao.ReservationDAO;
import com.oceanview.resort.dao.RoomDAO;
import com.oceanview.resort.dao.UserDAO;
import com.oceanview.resort.dao.impl.BillJdbcDAO;
import com.oceanview.resort.dao.impl.GuestProfileJdbcDAO;
import com.oceanview.resort.dao.impl.ReservationJdbcDAO;
import com.oceanview.resort.dao.impl.RoomJdbcDAO;
import com.oceanview.resort.dao.impl.UserJdbcDAO;
import com.oceanview.resort.exception.DataAccessException;
import com.oceanview.resort.model.Bill;
import com.oceanview.resort.model.GuestProfile;
import com.oceanview.resort.model.Reservation;
import com.oceanview.resort.model.Room;
import com.oceanview.resort.model.User;
import com.oceanview.resort.util.AuthConstants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet("/api/reports/*")
public class ReportApiServlet extends HttpServlet {
  private final UserDAO userDAO = new UserJdbcDAO();
  private final RoomDAO roomDAO = new RoomJdbcDAO();
  private final ReservationDAO reservationDAO = new ReservationJdbcDAO();
  private final BillDAO billDAO = new BillJdbcDAO();
  private final GuestProfileDAO guestProfileDAO = new GuestProfileJdbcDAO();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    User sessionUser = getSessionUser(request);
    if (sessionUser == null) {
      writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
      return;
    }

    String path = request.getPathInfo();
    if (path == null || "/".equals(path)) {
      writeError(response, HttpServletResponse.SC_NOT_FOUND, "Report endpoint not found");
      return;
    }

    try {
      ChartData data;
      switch (path) {
        case "/users-by-role":
          ensureAdminOrStaff(sessionUser);
          data = usersByRoleData();
          break;
        case "/occupancy":
          ensureAdminOrStaff(sessionUser);
          data = roomOccupancyData();
          break;
        case "/revenue":
          data = revenueTrendData(sessionUser);
          break;
        case "/reservation-trend":
          data = reservationTrendData(sessionUser);
          break;
        case "/reservation-status":
          data = reservationStatusData(sessionUser);
          break;
        case "/billing-status":
          data = billingStatusData(sessionUser);
          break;
        default:
          writeError(response, HttpServletResponse.SC_NOT_FOUND, "Report endpoint not found");
          return;
      }

      writeChartData(response, data);
    } catch (SecurityException ex) {
      writeError(response, HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
    } catch (IllegalArgumentException ex) {
      writeError(response, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
    } catch (DataAccessException ex) {
      writeError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Data access error");
    }
  }

  private ChartData usersByRoleData() {
    List<User> users = userDAO.findAll();

    int admin = 0;
    int staff = 0;
    int guest = 0;

    for (User user : users) {
      String role = normalize(user.getRole());
      if ("ADMIN".equals(role)) {
        admin++;
      } else if ("STAFF".equals(role)) {
        staff++;
      } else if ("GUEST".equals(role)) {
        guest++;
      }
    }

    return chartData(
      List.of("ADMIN", "STAFF", "GUEST"),
      List.of(admin, staff, guest)
    );
  }

  private ChartData roomOccupancyData() {
    List<Room> rooms = roomDAO.findAll();

    int available = 0;
    int occupied = 0;
    int maintenance = 0;
    int inactive = 0;

    for (Room room : rooms) {
      String status = normalize(room.getStatus());
      if ("AVAILABLE".equals(status)) {
        available++;
      } else if ("OCCUPIED".equals(status)) {
        occupied++;
      } else if ("MAINTENANCE".equals(status)) {
        maintenance++;
      } else if ("INACTIVE".equals(status)) {
        inactive++;
      }
    }

    return chartData(
      List.of("AVAILABLE", "OCCUPIED", "MAINTENANCE", "INACTIVE"),
      List.of(available, occupied, maintenance, inactive)
    );
  }

  private ChartData reservationStatusData(User sessionUser) {
    List<Reservation> reservations = getReservationsForUserScope(sessionUser);
    int pending = 0;
    int confirmed = 0;
    int checkedIn = 0;
    int checkedOut = 0;
    int cancelled = 0;
    int noShow = 0;
    for (Reservation reservation : reservations) {
      String status = normalize(reservation.getStatus());
      if ("PENDING".equals(status)) {
        pending++;
      } else if ("CONFIRMED".equals(status)) {
        confirmed++;
      } else if ("CHECKED_IN".equals(status)) {
        checkedIn++;
      } else if ("CHECKED_OUT".equals(status)) {
        checkedOut++;
      } else if ("CANCELLED".equals(status)) {
        cancelled++;
      } else if ("NO_SHOW".equals(status)) {
        noShow++;
      }
    }

    return chartData(
      List.of("PENDING", "CONFIRMED", "CHECKED_IN", "CHECKED_OUT", "CANCELLED", "NO_SHOW"),
      List.of(pending, confirmed, checkedIn, checkedOut, cancelled, noShow)
    );
  }

  private ChartData billingStatusData(User sessionUser) {
    List<Bill> bills = getBillsForUserScope(sessionUser);

    int unpaid = 0;
    int partial = 0;
    int paid = 0;
    int refunded = 0;
    int voided = 0;

    for (Bill bill : bills) {
      String status = normalize(bill.getPaymentStatus());
      if ("UNPAID".equals(status)) {
        unpaid++;
      } else if ("PARTIAL".equals(status)) {
        partial++;
      } else if ("PAID".equals(status)) {
        paid++;
      } else if ("REFUNDED".equals(status)) {
        refunded++;
      } else if ("VOID".equals(status)) {
        voided++;
      }
    }

    return chartData(
      List.of("UNPAID", "PARTIAL", "PAID", "REFUNDED", "VOID"),
      List.of(unpaid, partial, paid, refunded, voided)
    );
  }

  private ChartData revenueTrendData(User sessionUser) {
    List<Bill> bills = getBillsForUserScope(sessionUser);
    LinkedHashMap<YearMonth, BigDecimal> months = latestSixMonthsRevenue();

    for (Bill bill : bills) {
      if (bill.getIssuedAt() == null || bill.getTotal() == null) {
        continue;
      }

      YearMonth key = YearMonth.from(bill.getIssuedAt());
      if (months.containsKey(key)) {
        months.put(key, months.get(key).add(bill.getTotal()));
      }
    }

    List<String> labels = new ArrayList<>();
    List<Number> values = new ArrayList<>();
    for (Map.Entry<YearMonth, BigDecimal> entry : months.entrySet()) {
      labels.add(entry.getKey().toString());
      values.add(entry.getValue().setScale(2, RoundingMode.HALF_UP).doubleValue());
    }

    return chartData(labels, values);
  }

  private ChartData reservationTrendData(User sessionUser) {
    List<Reservation> reservations = getReservationsForUserScope(sessionUser);
    LinkedHashMap<YearMonth, Integer> months = latestSixMonthsReservationCount();

    for (Reservation reservation : reservations) {
      if (reservation.getCreatedAt() == null) {
        continue;
      }

      YearMonth key = YearMonth.from(reservation.getCreatedAt());
      if (months.containsKey(key)) {
        months.put(key, months.get(key) + 1);
      }
    }

    List<String> labels = new ArrayList<>();
    List<Number> values = new ArrayList<>();
    for (Map.Entry<YearMonth, Integer> entry : months.entrySet()) {
      labels.add(entry.getKey().toString());
      values.add(entry.getValue());
    }

    return chartData(labels, values);
  }

  private List<Reservation> getReservationsForUserScope(User sessionUser) {
    if (isGuest(sessionUser)) {
      long guestId = requireCurrentGuestId(sessionUser);
      return reservationDAO.findByGuestId(guestId);
    }
    return reservationDAO.findAll();
  }

  private List<Bill> getBillsForUserScope(User sessionUser) {
    if (!isGuest(sessionUser)) {
      return billDAO.findAll();
    }

    long guestId = requireCurrentGuestId(sessionUser);
    List<Reservation> ownReservations = reservationDAO.findByGuestId(guestId);
    Set<Long> ownReservationIds = new HashSet<>();
    for (Reservation reservation : ownReservations) {
      ownReservationIds.add(reservation.getId());
    }

    List<Bill> ownBills = new ArrayList<>();
    for (Bill bill : billDAO.findAll()) {
      if (ownReservationIds.contains(bill.getReservationId())) {
        ownBills.add(bill);
      }
    }
    return ownBills;
  }

  private long requireCurrentGuestId(User sessionUser) {
    if (sessionUser.getId() == null) {
      throw new SecurityException("Guest session is invalid");
    }

    GuestProfile profile = guestProfileDAO.findByUserId(sessionUser.getId())
      .orElseThrow(() -> new SecurityException("Guest profile not found"));

    if (profile.getId() == null) {
      throw new SecurityException("Guest profile is invalid");
    }
    return profile.getId();
  }

  private LinkedHashMap<YearMonth, BigDecimal> latestSixMonthsRevenue() {
    YearMonth current = YearMonth.now();
    LinkedHashMap<YearMonth, BigDecimal> months = new LinkedHashMap<>();
    for (int i = 5; i >= 0; i--) {
      months.put(current.minusMonths(i), BigDecimal.ZERO);
    }
    return months;
  }

  private LinkedHashMap<YearMonth, Integer> latestSixMonthsReservationCount() {
    YearMonth current = YearMonth.now();
    LinkedHashMap<YearMonth, Integer> months = new LinkedHashMap<>();
    for (int i = 5; i >= 0; i--) {
      months.put(current.minusMonths(i), 0);
    }
    return months;
  }

  private void ensureAdminOrStaff(User sessionUser) {
    String role = normalize(sessionUser.getRole());
    if (!("ADMIN".equals(role) || "STAFF".equals(role))) {
      throw new SecurityException("Access denied");
    }
  }

  private boolean isGuest(User sessionUser) {
    return "GUEST".equals(normalize(sessionUser.getRole()));
  }

  private User getSessionUser(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return null;
    }
    Object user = session.getAttribute(AuthConstants.SESSION_USER);
    return user instanceof User ? (User) user : null;
  }

  private ChartData chartData(List<String> labels, List<? extends Number> values) {
    List<Number> normalizedValues = new ArrayList<>();
    for (Number value : values) {
      normalizedValues.add(value == null ? 0 : value);
    }
    return new ChartData(labels, normalizedValues);
  }

  private void writeChartData(HttpServletResponse response, ChartData data) throws IOException {
    response.setContentType("application/json;charset=UTF-8");

    StringBuilder json = new StringBuilder();
    json.append("{\"labels\":[");
    for (int i = 0; i < data.labels.size(); i++) {
      if (i > 0) {
        json.append(',');
      }
      json.append('"').append(escapeJson(data.labels.get(i))).append('"');
    }

    json.append("],\"values\":[");
    for (int i = 0; i < data.values.size(); i++) {
      if (i > 0) {
        json.append(',');
      }
      json.append(data.values.get(i));
    }
    json.append("]}");

    response.getWriter().write(json.toString());
  }

  private void writeError(HttpServletResponse response, int statusCode, String message) throws IOException {
    response.setStatus(statusCode);
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write("{\"error\":\"" + escapeJson(message) + "\"}");
  }

  private String normalize(String value) {
    return value == null ? "" : value.trim().toUpperCase();
  }

  private String escapeJson(String value) {
    if (value == null) {
      return "";
    }
    return value.replace("\\", "\\\\").replace("\"", "\\\"");
  }

  private static final class ChartData {
    private final List<String> labels;
    private final List<Number> values;

    private ChartData(List<String> labels, List<Number> values) {
      this.labels = labels;
      this.values = values;
    }
  }
}
