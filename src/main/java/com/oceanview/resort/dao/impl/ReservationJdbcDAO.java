package com.oceanview.resort.dao.impl;

import com.oceanview.resort.dao.ReservationDAO;
import com.oceanview.resort.exception.DataAccessException;
import com.oceanview.resort.model.Reservation;
import com.oceanview.resort.util.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservationJdbcDAO implements ReservationDAO {

  @Override
  public long create(Reservation reservation) {
    String sql = "INSERT INTO reservations (guest_id, room_id, check_in, check_out, adults, children, status, booked_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    try (var connection = DBConnection.getConnection();
         var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      statement.setLong(1, reservation.getGuestId());
      statement.setLong(2, reservation.getRoomId());
      statement.setObject(3, reservation.getCheckIn());
      statement.setObject(4, reservation.getCheckOut());
      statement.setInt(5, reservation.getAdults());
      statement.setInt(6, reservation.getChildren());
      statement.setString(7, reservation.getStatus());
      if (reservation.getBookedBy() == null) {
        statement.setNull(8, java.sql.Types.BIGINT);
      } else {
        statement.setLong(8, reservation.getBookedBy());
      }
      statement.executeUpdate();

      try (ResultSet keys = statement.getGeneratedKeys()) {
        if (keys.next()) {
          return keys.getLong(1);
        }
      }
      throw new DataAccessException("Creating reservation failed, no ID returned.");
    } catch (SQLException e) {
      throw new DataAccessException("Error creating reservation", e);
    }
  }

  @Override
  public Optional<Reservation> findById(long id) {
    String sql = "SELECT * FROM reservations WHERE id = ?";
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return Optional.of(mapRow(resultSet));
        }
      }
      return Optional.empty();
    } catch (SQLException e) {
      throw new DataAccessException("Error finding reservation by id", e);
    }
  }

  @Override
  public List<Reservation> findAll() {
    String sql = "SELECT * FROM reservations ORDER BY id";
    List<Reservation> reservations = new ArrayList<>();
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        reservations.add(mapRow(resultSet));
      }
      return reservations;
    } catch (SQLException e) {
      throw new DataAccessException("Error finding all reservations", e);
    }
  }

  @Override
  public List<Reservation> findByGuestId(long guestId) {
    String sql = "SELECT * FROM reservations WHERE guest_id = ? ORDER BY id";
    List<Reservation> reservations = new ArrayList<>();
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, guestId);
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          reservations.add(mapRow(resultSet));
        }
      }
      return reservations;
    } catch (SQLException e) {
      throw new DataAccessException("Error finding reservations by guest id", e);
    }
  }

  @Override
  public boolean existsOverlappingReservation(long roomId, LocalDate checkIn, LocalDate checkOut, Long excludeReservationId) {
    String baseSql = "SELECT COUNT(*) FROM reservations WHERE room_id = ? " +
      "AND status IN ('PENDING','CONFIRMED','CHECKED_IN') " +
      "AND ? < check_out AND ? > check_in";
    String sql = excludeReservationId == null ? baseSql : baseSql + " AND id <> ?";

    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, roomId);
      statement.setObject(2, checkIn);
      statement.setObject(3, checkOut);
      if (excludeReservationId != null) {
        statement.setLong(4, excludeReservationId);
      }

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return resultSet.getInt(1) > 0;
        }
        return false;
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error checking overlapping reservations", e);
    }
  }

  @Override
  public boolean update(Reservation reservation) {
    String sql = "UPDATE reservations SET guest_id = ?, room_id = ?, check_in = ?, check_out = ?, adults = ?, children = ?, status = ?, booked_by = ? WHERE id = ?";
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, reservation.getGuestId());
      statement.setLong(2, reservation.getRoomId());
      statement.setObject(3, reservation.getCheckIn());
      statement.setObject(4, reservation.getCheckOut());
      statement.setInt(5, reservation.getAdults());
      statement.setInt(6, reservation.getChildren());
      statement.setString(7, reservation.getStatus());
      if (reservation.getBookedBy() == null) {
        statement.setNull(8, java.sql.Types.BIGINT);
      } else {
        statement.setLong(8, reservation.getBookedBy());
      }
      statement.setLong(9, reservation.getId());
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new DataAccessException("Error updating reservation", e);
    }
  }

  @Override
  public boolean deleteById(long id) {
    String sql = "DELETE FROM reservations WHERE id = ?";
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, id);
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new DataAccessException("Error deleting reservation", e);
    }
  }

  private Reservation mapRow(ResultSet resultSet) throws SQLException {
    Reservation reservation = new Reservation();
    reservation.setId(resultSet.getLong("id"));
    reservation.setGuestId(resultSet.getLong("guest_id"));
    reservation.setRoomId(resultSet.getLong("room_id"));
    reservation.setCheckIn(resultSet.getObject("check_in", LocalDate.class));
    reservation.setCheckOut(resultSet.getObject("check_out", LocalDate.class));
    reservation.setAdults(resultSet.getInt("adults"));
    reservation.setChildren(resultSet.getInt("children"));
    reservation.setStatus(resultSet.getString("status"));

    long bookedBy = resultSet.getLong("booked_by");
    if (!resultSet.wasNull()) {
      reservation.setBookedBy(bookedBy);
    }

    Timestamp createdAt = resultSet.getTimestamp("created_at");
    if (createdAt != null) {
      reservation.setCreatedAt(createdAt.toLocalDateTime());
    }

    Timestamp updatedAt = resultSet.getTimestamp("updated_at");
    if (updatedAt != null) {
      reservation.setUpdatedAt(updatedAt.toLocalDateTime());
    }
    return reservation;
  }
}
