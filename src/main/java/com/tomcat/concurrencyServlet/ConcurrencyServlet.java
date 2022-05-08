package com.tomcat.concurrencyServlet;

import com.tomcat.helper.Helper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@WebServlet(name = "concurrencyServlet", value = "/concurrencyServlet")
public class ConcurrencyServlet extends HttpServlet {
    @Override
    protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        try {
            TimeUnit.SECONDS.sleep(6);
        } catch (InterruptedException e) { e.printStackTrace(); }

        response.setContentType("text/html;charset=GBK");
        new Helper.HTML(response).generatorHTML(writer -> writer.println("你好：" + username), "Concurrency demo");
    }
}
