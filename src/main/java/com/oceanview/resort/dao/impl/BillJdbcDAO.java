package com.oceanview.resort.dao.impl;

import com.oceanview.resort.dao.BillDAO;
import com.oceanview.resort.exception.DataAccessException;
import com.oceanview.resort.model.Bill;
import com.oceanview.resort.util.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BillJdbcDAO implements BillDAO {

  @Override
  public long create(Bill bill) {
    String sql = "INSERT INTO bills (reservation_id, subtotal, tax, discount, total, issued_by, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (var connection = DBConnection.getConnection();
         var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      statement.setLong(1, bill.getReservationId());
      statement.setBigDecimal(2, bill.getSubtotal());
      statement.setBigDecimal(3, bill.getTax());
      statement.setBigDecimal(4, bill.getDiscount());
      statement.setBigDecimal(5, bill.getTotal());
      statement.setLong(6, bill.getIssuedBy());
      statement.setString(7, bill.getPaymentStatus());
      statement.executeUpdate();

      try (ResultSet keys = statement.getGeneratedKeys()) {
        if (keys.next()) {
          return keys.getLong(1);
        }
      }
      throw new DataAccessException("Creating bill failed, no ID returned.");
    } catch (SQLException e) {
      throw new DataAccessException("Error creating bill", e);
    }
  }

  @Override
  public Optional<Bill> findById(long id) {
    String sql = "SELECT * FROM bills WHERE id = ?";
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
      throw new DataAccessException("Error finding bill by id", e);
    }
  }

  @Override
  public Optional<Bill> findByReservationId(long reservationId) {
    String sql = "SELECT * FROM bills WHERE reservation_id = ?";
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, reservationId);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return Optional.of(mapRow(resultSet));
        }
      }
      return Optional.empty();
    } catch (SQLException e) {
      throw new DataAccessException("Error finding bill by reservation id", e);
    }
  }

  @Override
  public List<Bill> findAll() {
    String sql = "SELECT * FROM bills ORDER BY id";
    List<Bill> bills = new ArrayList<>();
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        bills.add(mapRow(resultSet));
      }
      return bills;
    } catch (SQLException e) {
      throw new DataAccessException("Error finding all bills", e);
    }
  }

  @Override
  public boolean update(Bill bill) {
    String sql = "UPDATE bills SET subtotal = ?, tax = ?, discount = ?, total = ?, payment_status = ? WHERE id = ?";
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setBigDecimal(1, bill.getSubtotal());
      statement.setBigDecimal(2, bill.getTax());
      statement.setBigDecimal(3, bill.getDiscount());
      statement.setBigDecimal(4, bill.getTotal());
      statement.setString(5, bill.getPaymentStatus());
      statement.setLong(6, bill.getId());
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new DataAccessException("Error updating bill", e);
    }
  }

  @Override
  public boolean deleteById(long id) {
    String sql = "DELETE FROM bills WHERE id = ?";
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, id);
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new DataAccessException("Error deleting bill", e);
    }
  }

  private Bill mapRow(ResultSet resultSet) throws SQLException {
    Bill bill = new Bill();
    bill.setId(resultSet.getLong("id"));
    bill.setReservationId(resultSet.getLong("reservation_id"));
    bill.setSubtotal(resultSet.getBigDecimal("subtotal"));
    bill.setTax(resultSet.getBigDecimal("tax"));
    bill.setDiscount(resultSet.getBigDecimal("discount"));
    bill.setTotal(resultSet.getBigDecimal("total"));
    bill.setIssuedBy(resultSet.getLong("issued_by"));
    bill.setPaymentStatus(resultSet.getString("payment_status"));

    Timestamp issuedAt = resultSet.getTimestamp("issued_at");
    if (issuedAt != null) {
      bill.setIssuedAt(issuedAt.toLocalDateTime());
    }

    return bill;
  }
}
