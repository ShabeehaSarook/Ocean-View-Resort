package com.oceanview.resort.listener;

import com.oceanview.resort.util.DBConnection;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppContextListener implements ServletContextListener {
  private static final Logger LOGGER = Logger.getLogger(AppContextListener.class.getName());

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    try (Connection ignored = DBConnection.getConnection()) {
      LOGGER.info("Database connectivity check passed at startup.");
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, "Database connectivity check failed at startup.", e);
    }
  }
}
