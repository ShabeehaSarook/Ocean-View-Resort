package com.oceanview.resort.controller.staff;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/staff/home")
public class StaffHomeServlet extends HttpServlet {
  private static final String VIEW = "/WEB-INF/views/staff/home.jsp";

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.getRequestDispatcher(VIEW).forward(request, response);
  }
}
