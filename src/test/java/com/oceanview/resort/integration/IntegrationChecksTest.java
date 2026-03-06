package com.oceanview.resort.integration;

import com.oceanview.resort.dao.BillDAO;
import com.oceanview.resort.dao.ReservationDAO;
import com.oceanview.resort.dao.impl.BillJdbcDAO;
import com.oceanview.resort.dao.impl.ReservationJdbcDAO;
import com.oceanview.resort.model.Bill;
import com.oceanview.resort.service.BillingService;
import com.oceanview.resort.util.AuthRedirectUtil;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class IntegrationChecksTest {
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
  void loginRoleRedirectFlowShouldMapToExpectedHomes() {
    assertEquals("/admin/home", AuthRedirectUtil.roleHomePath("ADMIN"));
    assertEquals("/staff/home", AuthRedirectUtil.roleHomePath("STAFF"));
    assertEquals("/guest/home", AuthRedirectUtil.roleHomePath("GUEST"));
    assertEquals("/guest/home", AuthRedirectUtil.roleHomePath("UNKNOWN"));
  }

  @Test
  void reservationOverlapPreventionShouldBlockConflictingDates() {
    assumeTrue(dbReady, "Database is not available for integration checks.");

    ReservationDAO reservationDAO = new ReservationJdbcDAO();

    boolean overlaps = reservationDAO.existsOverlappingReservation(
      3,
      LocalDate.now().plusDays(3),
      LocalDate.now().plusDays(4),
      null
    );
    boolean doesNotOverlap = reservationDAO.existsOverlappingReservation(
      3,
      LocalDate.now().plusDays(7),
      LocalDate.now().plusDays(8),
      null
    );

    assertTrue(overlaps);
    assertFalse(doesNotOverlap);
  }

  @Test
  void billTotalCalculationsShouldMatchStoredValues() {
    assumeTrue(dbReady, "Database is not available for integration checks.");

    BillDAO billDAO = new BillJdbcDAO();
    BillingService billingService = new BillingService();
    List<Bill> bills = billDAO.findAll();

    assertFalse(bills.isEmpty(), "Seed data should provide at least one bill.");

    for (Bill bill : bills) {
      assertEquals(
        0,
        billingService.calculateTotal(bill.getSubtotal(), bill.getTax(), bill.getDiscount()).compareTo(bill.getTotal())
      );
    }
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
