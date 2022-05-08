package com.tomcat.fontServlet;

import com.tomcat.helper.Helper;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// 使用注解来配置web.xml中的ServletConfig
@WebServlet(
        name = "fontServlet1", value = "/fontServlet1",
        initParams = { @WebInitParam(name = "color", value = "red"), @WebInitParam(name = "size", value = "15") })
public class FontServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String word = request.getParameter("word");
        final String newWord = word == null ? "hello" : word;

        // 获取web.xml中servlet元素内配置的init-param元素(ServletConfig.getInitParameter(String name))
        String color = getInitParameter("color");
        String size = getInitParameter("size");

        System.out.println("servletName: " + this.getServletName());

        new Helper.HTML(response)
                .generatorHTML(writer -> writer.println("<font size='" + size + "' color='" + color + "'>" + newWord + "</font>"), "fontServlet");
    }
}
