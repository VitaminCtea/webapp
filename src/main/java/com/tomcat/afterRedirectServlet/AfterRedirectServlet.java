package com.tomcat.afterRedirectServlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "afterRedirectServlet", value = "/afterRedirectServlet")
public class AfterRedirectServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String message = (String) request.getAttribute("msg");
        System.out.println("请求范围内的消息：" + message);

        message = request.getParameter("msg");
        System.out.println("请求参数中的消息：" + message);

        PrintWriter writer = response.getWriter();
        writer.println(message);
        writer.close();
    }
}
