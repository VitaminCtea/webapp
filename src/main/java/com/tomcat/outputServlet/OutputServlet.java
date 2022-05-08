package com.tomcat.outputServlet;

import jakarta.servlet.GenericServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "outputServlet", value = "/outputServlet")
public class OutputServlet extends GenericServlet {
    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        String message = (String) request.getAttribute("msg");
        PrintWriter writer = response.getWriter();
        writer.println(message);
        writer.close();
    }
}
