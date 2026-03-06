package com.oceanview.resort.dao.impl;

import com.oceanview.resort.dao.RoomDAO;
import com.oceanview.resort.exception.DataAccessException;
import com.oceanview.resort.model.Room;
import com.oceanview.resort.util.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomJdbcDAO implements RoomDAO {

  @Override
  public long create(Room room) {
    String sql = "INSERT INTO rooms (room_number, room_type_id, status, floor) VALUES (?, ?, ?, ?)";
    try (var connection = DBConnection.getConnection();
         var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      statement.setString(1, room.getRoomNumber());
      statement.setLong(2, room.getRoomTypeId());
      statement.setString(3, room.getStatus());
      statement.setInt(4, room.getFloor());
      statement.executeUpdate();

      try (ResultSet keys = statement.getGeneratedKeys()) {
        if (keys.next()) {
          return keys.getLong(1);
        }
      }
      throw new DataAccessException("Creating room failed, no ID returned.");
    } catch (SQLException e) {
      throw new DataAccessException("Error creating room", e);
    }
  }

  @Override
  public Optional<Room> findById(long id) {
    String sql = "SELECT * FROM rooms WHERE id = ?";
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
      throw new DataAccessException("Error finding room by id", e);
    }
  }

  @Override
  public Optional<Room> findByRoomNumber(String roomNumber) {
    String sql = "SELECT * FROM rooms WHERE room_number = ?";
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, roomNumber);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return Optional.of(mapRow(resultSet));
        }
      }
      return Optional.empty();
    } catch (SQLException e) {
      throw new DataAccessException("Error finding room by room number", e);
    }
  }

  @Override
  public List<Room> findAll() {
    String sql = "SELECT * FROM rooms ORDER BY id";
    List<Room> rooms = new ArrayList<>();
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        rooms.add(mapRow(resultSet));
      }
      return rooms;
    } catch (SQLException e) {
      throw new DataAccessException("Error finding all rooms", e);
    }
  }

  @Override
  public boolean update(Room room) {
    String sql = "UPDATE rooms SET room_number = ?, room_type_id = ?, status = ?, floor = ? WHERE id = ?";
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, room.getRoomNumber());
      statement.setLong(2, room.getRoomTypeId());
      statement.setString(3, room.getStatus());
      statement.setInt(4, room.getFloor());
      statement.setLong(5, room.getId());
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new DataAccessException("Error updating room", e);
    }
  }

  @Override
  public boolean deleteById(long id) {
    String sql = "DELETE FROM rooms WHERE id = ?";
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, id);
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new DataAccessException("Error deleting room", e);
    }
  }

  private Room mapRow(ResultSet resultSet) throws SQLException {
    Room room = new Room();
    room.setId(resultSet.getLong("id"));
    room.setRoomNumber(resultSet.getString("room_number"));
    room.setRoomTypeId(resultSet.getLong("room_type_id"));
    room.setStatus(resultSet.getString("status"));
    room.setFloor(resultSet.getInt("floor"));
    return room;
  }
}
