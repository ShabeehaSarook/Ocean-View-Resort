package com.oceanview.resort.dao.impl;

import com.oceanview.resort.dao.UserDAO;
import com.oceanview.resort.exception.DataAccessException;
import com.oceanview.resort.model.User;
import com.oceanview.resort.util.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserJdbcDAO implements UserDAO {

  @Override
  public long create(User user) {
    String sql = "INSERT INTO users (username, password_hash, role, status) VALUES (?, ?, ?, ?)";
    try (var connection = DBConnection.getConnection();
         var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      statement.setString(1, user.getUsername());
      statement.setString(2, user.getPasswordHash());
      statement.setString(3, user.getRole());
      statement.setString(4, user.getStatus());
      statement.executeUpdate();

      try (ResultSet keys = statement.getGeneratedKeys()) {
        if (keys.next()) {
          return keys.getLong(1);
        }
      }
      throw new DataAccessException("Creating user failed, no ID returned.");
    } catch (SQLException e) {
      throw new DataAccessException("Error creating user", e);
    }
  }

  @Override
  public Optional<User> findById(long id) {
    String sql = "SELECT * FROM users WHERE id = ?";
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
      throw new DataAccessException("Error finding user by id", e);
    }
  }

  @Override
  public Optional<User> findByUsername(String username) {
    String sql = "SELECT * FROM users WHERE username = ?";
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, username);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return Optional.of(mapRow(resultSet));
        }
      }
      return Optional.empty();
    } catch (SQLException e) {
      throw new DataAccessException("Error finding user by username", e);
    }
  }

  @Override
  public List<User> findAll() {
    String sql = "SELECT * FROM users ORDER BY id";
    List<User> users = new ArrayList<>();
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        users.add(mapRow(resultSet));
      }
      return users;
    } catch (SQLException e) {
      throw new DataAccessException("Error finding all users", e);
    }
  }

  @Override
  public boolean update(User user) {
    String sql = "UPDATE users SET username = ?, password_hash = ?, role = ?, status = ? WHERE id = ?";
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, user.getUsername());
      statement.setString(2, user.getPasswordHash());
      statement.setString(3, user.getRole());
      statement.setString(4, user.getStatus());
      statement.setLong(5, user.getId());
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new DataAccessException("Error updating user", e);
    }
  }

  @Override
  public boolean deleteById(long id) {
    String sql = "DELETE FROM users WHERE id = ?";
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, id);
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new DataAccessException("Error deleting user", e);
    }
  }

  private User mapRow(ResultSet resultSet) throws SQLException {
    User user = new User();
    user.setId(resultSet.getLong("id"));
    user.setUsername(resultSet.getString("username"));
    user.setPasswordHash(resultSet.getString("password_hash"));
    user.setRole(resultSet.getString("role"));
    user.setStatus(resultSet.getString("status"));

    Timestamp createdAt = resultSet.getTimestamp("created_at");
    if (createdAt != null) {
      user.setCreatedAt(createdAt.toLocalDateTime());
    }

    Timestamp updatedAt = resultSet.getTimestamp("updated_at");
    if (updatedAt != null) {
      user.setUpdatedAt(updatedAt.toLocalDateTime());
    }
    return user;
  }
}
