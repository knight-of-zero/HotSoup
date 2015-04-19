package me.soupbringer.hotsoup.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;

/**
 * Simple Hello World servlet to see if things are connected properly.
 */
@Singleton
public final class TestServlet extends HttpServlet {
  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    response.getWriter().append("Hello World!");
    // request.getRequestDispatcher("/index.jsp").forward(request, response);
  }

}
