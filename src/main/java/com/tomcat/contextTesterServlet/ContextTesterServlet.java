package com.tomcat.contextTesterServlet;

import com.tomcat.helper.Helper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;

public class ContextTesterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletContext context = getServletContext();

        Helper.HTML html = new Helper.HTML(response);
        html.generatorHTML(writer -> {
            html.write("Email", context.getInitParameter("email"));
            html.write("Path", context.getRealPath("/WEB-INF"));
            html.write("MimeType", context.getMimeType("/WEB-INF/WEB.xml"));
            html.write("MajorVersion", context.getMajorVersion());
            html.write("ServerInfo", context.getServerInfo());
        }, "contextTesterServlet");

        context.log("这是ContextTesterServlet输出的日志");
    }
}
