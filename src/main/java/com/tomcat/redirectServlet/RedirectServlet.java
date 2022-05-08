package com.tomcat.redirectServlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "redirectServlet", value = "/redirectServlet")
public class RedirectServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        * sendRedirect方法的控制权和forward方法一样，必须在目标组件上提交响应、关闭响应流等
        * 唯一区别就是源组件和目标组件不共享同一个ServletRequest对象，因此不共享请求范围内的共享数据
        * 重定向的地址可以是同一服务器上的同一Web应用中的组件，可以是Internet上的任意一个有效的网页
        * sendRedirect方法参数以/开头时，表示的是相对于当前服务器根路径的URL，而不是相对于当前Web应用根路径的URL
        * 也就是说在同一个服务器中，必须要以根URL为开头的URL地址，否则会报锁清秋的页面不存在的错误
        */
        String username = request.getParameter("username");
        String message;
        if (username == null) message = "Please input username";
        else message = "hello " + username;

        // 将request转发给需要共享的组件
        request.setAttribute("msg", message);
        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher("/afterRedirectServlet");
        dispatcher.forward(request, response);
        response.sendRedirect("/webapp/afterRedirectServlet?msg=" + message);
    }
}
