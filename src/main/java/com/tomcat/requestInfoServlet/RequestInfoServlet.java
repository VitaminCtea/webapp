package com.tomcat.requestInfoServlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static com.tomcat.helper.Helper.*;

import java.io.IOException;
import java.util.Enumeration;

@WebServlet(name = "requestInfoServlet", value = "/requestInfoServlet")
public class RequestInfoServlet extends HttpServlet {
    @Override public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HTML html = new HTML(response);
        html.generatorHTML(writer -> {
            html.write("LocalAddr", request.getLocalAddr());
            html.write("LocalName", request.getLocalName());
            html.write("LocalPort", request.getLocalPort());
            html.write("Protocol", request.getProtocol());
            html.write("RemoteAddr", request.getRemoteAddr());
            html.write("RemoteHost", request.getRemoteHost());
            html.write("RemotePort", request.getRemotePort());
            html.write("Method", request.getMethod());
            html.write("RequestURI", request.getRequestURI());
            html.write("ContextPath", request.getContextPath());
            html.write("QueryString", request.getQueryString());

            writer.println("<br>***打印HTTP请求头***");
            Enumeration<String> enumeration = request.getHeaderNames();
            while (enumeration.hasMoreElements()) {
                String headerName = enumeration.nextElement();
                writer.println("<br>" + headerName + ": " + request.getHeader(headerName));
            }

            writer.println("<br>***打印HTTP请求头结束***<br>");
            writer.println("<br>username: " + request.getParameter("username"));
        }, "requestInfoServlet");
    }
}
