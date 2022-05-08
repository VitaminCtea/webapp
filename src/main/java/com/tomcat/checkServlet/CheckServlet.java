package com.tomcat.checkServlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "checkServlet", value = "/checkServlet")
public class CheckServlet extends GenericServlet {
    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String message;
        if (username == null) message = "Please input username.";
        else message = "Hello " + username;

        request.setAttribute("msg", message);   // 与其他组件共享请求范围内的共享数据

        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher("/outputServlet");
        PrintWriter writer = response.getWriter();

        String before = generatorInfo("before");
        String after = generatorInfo("after");

        writer.println(before);
        System.out.println(before);

        // 转发的方式类似于前端的组件通信，比如vue的父子通信、兄弟通信、跨级通信，对应于$emit、bus、vuex
        // 先清空用于存放相应正文数据的缓冲区，因此Servlet源组件生成的响应结果不会被发送到客户端，只有被转发的组件生成的响应结果才会被发送到客户端
        // 也就是说在源组件上向响应流不会被发送到客户端，对应这个组件的write不生效
        // 不应该在转发之前调用ServletResponse close方法，书上是会抛出IllegalStateException异常
        // 但在这个版本的tomcat中不会抛出异常，只是转发之后的目标组件响应结果是close方法之前的响应数据，所以在目标组件不会把message作为响应发送到客户端
        // 而是Out from CheckServlet before forwarding request
        // 为了避免造成可能的异常或者莫名的bug，应避免在源组件向客户端提交响应结果
        // writer.close();
        dispatcher.forward(request, response);

        writer.println(after);
        System.out.println(after);

        // writer.close(); 在转发之后不会造成异常或bug，目标组件会正确的提交响应
    }

    private String generatorInfo(String val) { return "Out from CheckServlet " + val + " forwarding request"; }
}
