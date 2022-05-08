package com.tomcat.cookieServlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "cookieServlet", value = "/cookieServlet")
public class CookieServlet extends HttpServlet {
    private int count;
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                writer.println("-------------------------------------------------------");
                writer.println("Cookie name: " + cookie.getName());
                writer.println("Cookie value: " + cookie.getValue());
                writer.println("Cookie Max Age: " + cookie.getMaxAge());
            }
        } else writer.println("No Cookie");

        response.addCookie(new Cookie("CookieName" + count, "CookieValue" + count));
        count++;
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
