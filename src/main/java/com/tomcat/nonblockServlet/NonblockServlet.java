package com.tomcat.nonblockServlet;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@WebServlet(name = "nonblockServlet", value = "/nonblockServlet", asyncSupported = true)
public class NonblockServlet extends HttpServlet {
    @Override public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=GBK");
        PrintWriter writer = response.getWriter();
        writer.println("<title>非阻塞IO示例</title>");
        writer.println("进入Servlet的service()方法的时间：" + new Date() + ".<br />");
        AsyncContext context = request.startAsync();
        context.setTimeout(60 * 1000);
        ServletInputStream in = request.getInputStream();
        in.setReadListener(new MyReadListener(in, context));
        writer.println("进入Servlet的service()方法的时间：" + new Date() + ".<br /><hr>");
        writer.flush();
    }
}
