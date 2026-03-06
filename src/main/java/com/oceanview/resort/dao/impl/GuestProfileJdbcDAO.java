package com.oceanview.resort.dao.impl;

import com.oceanview.resort.dao.GuestProfileDAO;
import com.oceanview.resort.exception.DataAccessException;
import com.oceanview.resort.model.GuestProfile;
import com.oceanview.resort.util.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GuestProfileJdbcDAO implements GuestProfileDAO {

  @Override
  public long create(GuestProfile profile) {
    String sql = "INSERT INTO guest_profiles (user_id, full_name, email, phone, nic_passport, address) VALUES (?, ?, ?, ?, ?, ?)";
    try (var connection = DBConnection.getConnection();
         var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      statement.setLong(1, profile.getUserId());
      statement.setString(2, profile.getFullName());
      statement.setString(3, profile.getEmail());
      statement.setString(4, profile.getPhone());
      statement.setString(5, profile.getNicPassport());
      statement.setString(6, profile.getAddress());
      statement.executeUpdate();

      try (ResultSet keys = statement.getGeneratedKeys()) {
        if (keys.next()) {
          return keys.getLong(1);
        }
      }
      throw new DataAccessException("Creating guest profile failed, no ID returned.");
    } catch (SQLException e) {
      throw new DataAccessException("Error creating guest profile", e);
    }
  }

  @Override
  public Optional<GuestProfile> findById(long id) {
    String sql = "SELECT * FROM guest_profiles WHERE id = ?";
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
      throw new DataAccessException("Error finding guest profile by id", e);
    }
  }

  @Override
  public Optional<GuestProfile> findByUserId(long userId) {
    String sql = "SELECT * FROM guest_profiles WHERE user_id = ?";
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, userId);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return Optional.of(mapRow(resultSet));
        }
      }
      return Optional.empty();
    } catch (SQLException e) {
      throw new DataAccessException("Error finding guest profile by user id", e);
    }
  }

  @Override
  public List<GuestProfile> findAll() {
    String sql = "SELECT * FROM guest_profiles ORDER BY id";
    List<GuestProfile> profiles = new ArrayList<>();
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        profiles.add(mapRow(resultSet));
      }
      return profiles;
    } catch (SQLException e) {
      throw new DataAccessException("Error finding all guest profiles", e);
    }
  }

  @Override
  public boolean update(GuestProfile profile) {
    String sql = "UPDATE guest_profiles SET full_name = ?, email = ?, phone = ?, nic_passport = ?, address = ? WHERE id = ?";
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, profile.getFullName());
      statement.setString(2, profile.getEmail());
      statement.setString(3, profile.getPhone());
      statement.setString(4, profile.getNicPassport());
      statement.setString(5, profile.getAddress());
      statement.setLong(6, profile.getId());
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new DataAccessException("Error updating guest profile", e);
    }
  }

  @Override
  public boolean deleteById(long id) {
    String sql = "DELETE FROM guest_profiles WHERE id = ?";
    try (var connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, id);
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new DataAccessException("Error deleting guest profile", e);
    }
  }

  private GuestProfile mapRow(ResultSet resultSet) throws SQLException {
    GuestProfile profile = new GuestProfile();
    profile.setId(resultSet.getLong("id"));
    profile.setUserId(resultSet.getLong("user_id"));
    profile.setFullName(resultSet.getString("full_name"));
    profile.setEmail(resultSet.getString("email"));
    profile.setPhone(resultSet.getString("phone"));
    profile.setNicPassport(resultSet.getString("nic_passport"));
    profile.setAddress(resultSet.getString("address"));
    return profile;
  }
}
