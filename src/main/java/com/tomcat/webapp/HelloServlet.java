package com.tomcat.webapp;

import com.tomcat.helper.Helper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Enumeration;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    @Override public void init() {
        message = "Hello Servlet!";
        ServletContext context = getServletContext();
        System.out.println(context.getInitParameterNames().hasMoreElements());
        Enumeration<String> names = context.getAttributeNames();
        while (names.hasMoreElements()) System.out.println(names.nextElement());
     }

    @Override public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        new Helper.HTML(response).generatorHTML(writer -> writer.println("<h1>" + message + "</h1>"), "helloServlet");
    }

    public void destroy() {}
}