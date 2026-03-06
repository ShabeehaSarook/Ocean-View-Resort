package com.oceanview.resort.controller;

import com.oceanview.resort.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DBHealthServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/plain;charset=UTF-8");

    try (Connection ignored = DBConnection.getConnection()) {
      resp.setStatus(HttpServletResponse.SC_OK);
      resp.getWriter().write("DB OK");
    } catch (SQLException e) {
      resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
      resp.getWriter().write("DB DOWN: " + e.getMessage());
    }
  }
}
