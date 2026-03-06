package com.oceanview.resort.dao.impl;

import com.oceanview.resort.dao.RoomTypeDAO;
import com.oceanview.resort.exception.DataAccessException;
import com.oceanview.resort.model.RoomType;
import com.oceanview.resort.util.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomTypeJdbcDAO implements RoomTypeDAO {

  @Override
  public long create(RoomType roomType) {
    String sql = "INSERT INTO room_types (name, base_price, capacity, description) VALUES (?, ?, ?, ?)";
    try (var connection = DBConnection.getConnection();
         var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      statement.setString(1, roomType.getName());
      statement.setBigDecimal(2, roomType.getBasePrice());
      statement.setInt(3, roomType.getCapacity());
      statement.setString(4, roomType.getDescription());
      statement.executeUpdate();

      try (ResultSet keys = statement.getGeneratedKeys()) {
        if (keys.next()) {
          return keys.getLong(1);
        }
      }
      throw new DataAccessException("Creating room type failed, no ID returned.");
    } catch (SQLException e) {
      throw new DataAccessException("Error creating room type", e);
    }
  }

  @Override
  public Optional<RoomType> findById(long id) {
    String sql = "SELECT * FROM room_types WHERE id = ?";
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
      throw new DataAccessException("Error finding room type by id", e);
    }
  }

  @Override
  public List<RoomType> findAll() {
    String sql = "SELECT * FROM room_types ORDER BY id";
    List<RoomType> roomTypes = new ArrayList<>();
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        roomTypes.add(mapRow(resultSet));
      }
      return roomTypes;
    } catch (SQLException e) {
      throw new DataAccessException("Error finding all room types", e);
    }
  }

  @Override
  public boolean update(RoomType roomType) {
    String sql = "UPDATE room_types SET name = ?, base_price = ?, capacity = ?, description = ? WHERE id = ?";
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, roomType.getName());
      statement.setBigDecimal(2, roomType.getBasePrice());
      statement.setInt(3, roomType.getCapacity());
      statement.setString(4, roomType.getDescription());
      statement.setLong(5, roomType.getId());
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new DataAccessException("Error updating room type", e);
    }
  }

  @Override
  public boolean deleteById(long id) {
    String sql = "DELETE FROM room_types WHERE id = ?";
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, id);
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new DataAccessException("Error deleting room type", e);
    }
  }

  private RoomType mapRow(ResultSet resultSet) throws SQLException {
    RoomType roomType = new RoomType();
    roomType.setId(resultSet.getLong("id"));
    roomType.setName(resultSet.getString("name"));
    roomType.setBasePrice(resultSet.getBigDecimal("base_price"));
    roomType.setCapacity(resultSet.getInt("capacity"));
    roomType.setDescription(resultSet.getString("description"));
    return roomType;
  }
}
