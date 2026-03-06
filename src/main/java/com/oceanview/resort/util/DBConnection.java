package com.oceanview.resort.util;

import com.oceanview.resort.exception.DataAccessException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnection {
  private static final String PROPERTIES_FILE = "db.properties";
  private static final Properties PROPERTIES = new Properties();

  static {
    try (InputStream inputStream = DBConnection.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
      if (inputStream == null) {
        throw new DataAccessException("Missing db.properties in classpath");
      }
      PROPERTIES.load(inputStream);
      Class.forName(PROPERTIES.getProperty("db.driver"));
    } catch (IOException | ClassNotFoundException e) {
      throw new DataAccessException("Failed to initialize database configuration", e);
    }
  }

  private DBConnection() {
  }

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(
      PROPERTIES.getProperty("db.url"),
      PROPERTIES.getProperty("db.username"),
      PROPERTIES.getProperty("db.password")
    );
  }
}
