package com.oceanview.resort.dao;

import com.oceanview.resort.dao.impl.ReservationJdbcDAO;
import com.oceanview.resort.dao.impl.RoomTypeJdbcDAO;
import com.oceanview.resort.dao.impl.UserJdbcDAO;
import com.oceanview.resort.model.RoomType;
import com.oceanview.resort.model.User;
import com.oceanview.resort.util.DBConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class DaoSmokeTest {
  private static boolean dbReady;

  @BeforeAll
  static void setupDatabase() {
    try (Connection connection = DBConnection.getConnection()) {
      runSqlScript(connection, Path.of("sql", "schema.sql"));
      runSqlScript(connection, Path.of("sql", "seed.sql"));
      dbReady = true;
    } catch (Throwable ignored) {
      dbReady = false;
    }
  }

  @Test
  void userCrudSmokeTest() {
    assumeTrue(dbReady, "Database is not available for DAO smoke tests.");

    UserDAO userDAO = new UserJdbcDAO();
    String username = "dao_test_user_" + System.nanoTime();

    User user = new User();
    user.setUsername(username);
    user.setPasswordHash("hash");
    user.setRole("STAFF");
    user.setStatus("ACTIVE");

    long id = userDAO.create(user);
    assertTrue(id > 0);

    User created = userDAO.findById(id).orElseThrow();
    assertEquals(username, created.getUsername());

    created.setStatus("INACTIVE");
    assertTrue(userDAO.update(created));

    User updated = userDAO.findById(id).orElseThrow();
    assertEquals("INACTIVE", updated.getStatus());

    assertTrue(userDAO.deleteById(id));
    assertTrue(userDAO.findById(id).isEmpty());
  }

  @Test
  void roomTypeCrudSmokeTest() {
    assumeTrue(dbReady, "Database is not available for DAO smoke tests.");

    RoomTypeDAO roomTypeDAO = new RoomTypeJdbcDAO();
    String name = "Dao Test Type " + System.nanoTime();

    RoomType roomType = new RoomType();
    roomType.setName(name);
    roomType.setBasePrice(new java.math.BigDecimal("99.00"));
    roomType.setCapacity(2);
    roomType.setDescription("Temp room type");

    long id = roomTypeDAO.create(roomType);
    assertTrue(id > 0);

    RoomType created = roomTypeDAO.findById(id).orElseThrow();
    assertEquals(name, created.getName());

    created.setBasePrice(new java.math.BigDecimal("129.00"));
    assertTrue(roomTypeDAO.update(created));

    RoomType updated = roomTypeDAO.findById(id).orElseThrow();
    assertEquals(new java.math.BigDecimal("129.00"), updated.getBasePrice());

    assertTrue(roomTypeDAO.deleteById(id));
    assertTrue(roomTypeDAO.findById(id).isEmpty());
  }

  @Test
  void reservationOverlapSmokeTest() {
    assumeTrue(dbReady, "Database is not available for DAO smoke tests.");

    ReservationDAO reservationDAO = new ReservationJdbcDAO();

    boolean overlaps = reservationDAO.existsOverlappingReservation(
      3,
      LocalDate.now().plusDays(3),
      LocalDate.now().plusDays(4),
      null
    );
    assertTrue(overlaps);

    boolean doesNotOverlap = reservationDAO.existsOverlappingReservation(
      3,
      LocalDate.now().plusDays(7),
      LocalDate.now().plusDays(8),
      null
    );
    assertFalse(doesNotOverlap);
  }

  private static void runSqlScript(Connection connection, Path path) throws IOException, SQLException {
    String script = Files.readString(path);
    String[] statements = script.split(";");

    for (String raw : statements) {
      String sql = raw.strip();
      if (sql.isEmpty()) {
        continue;
      }

      String cleaned = removeInlineComments(sql).strip();
      if (cleaned.isEmpty()) {
        continue;
      }

      try (Statement statement = connection.createStatement()) {
        statement.execute(cleaned);
      }
    }
  }

  private static String removeInlineComments(String sql) {
    StringBuilder builder = new StringBuilder();
    String[] lines = sql.split("\\R");
    for (String line : lines) {
      String trimmed = line.stripLeading();
      if (!trimmed.startsWith("--")) {
        builder.append(line).append('\n');
      }
    }
    return builder.toString();
  }
}
