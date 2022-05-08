package com.tomcat.noteServlet;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "noteServlet", value = "/note")
public class NoteServlet extends HttpServlet {
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("NoteServlet: service()");

        response.setContentType("text/html;charset=GB2312");
        ServletOutputStream writer = response.getOutputStream();

        writer.println("<html>");
        writer.println("<head><title>留言簿</title></head>");
        writer.println("<body>");

        String username = request.getParameter("username");
        String content = request.getParameter("content");

        if (username != null) username = new String(username.getBytes(StandardCharsets.ISO_8859_1), "GB2312");
        if (content != null) content = new String(content.getBytes(StandardCharsets.ISO_8859_1), "GB2312");
        if (content != null && content.length() > 0) writer.println("<p>" + username + "的留言为：" + content + "</p>");

        writer.println("<form action='" + request.getContextPath() + "/note' method='POST' accept-charset='ISO-8859-1'>");
        writer.println("<b>姓名：</b>");
        writer.println("<input type='text' size='10' name='username' /><br>");
        writer.println("<b>留言：</b>");
        writer.println("<textarea name='content' rows='5' cols='20'></textarea><br><br>");
        writer.println("<input type='submit' name='submit' value='提交' />");
        writer.println("</form>");
        writer.println("</body></html>");
    }
}
