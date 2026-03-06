package com.oceanview.resort.controller.guest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/guest/home")
public class GuestHomeServlet extends GuestBaseServlet {
  private static final String VIEW = "/WEB-INF/views/guest/home.jsp";

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.getRequestDispatcher(VIEW).forward(request, response);
  }
}
