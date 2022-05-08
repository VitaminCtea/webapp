package com.tomcat.loadServlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@WebServlet(name = "loadServlet", value = "/loadServlet", loadOnStartup = 1)
public class LoadServlet extends HttpServlet {
    @Override public void init() {
        Properties ps = new Properties();
        Properties ps_ch = new Properties();
        try {
            ServletContext context = getServletContext();
            // 使用字符流，而不是使用字节流可以解决.properties文件中的中文乱码问题
            try (Reader ps_in = new InputStreamReader(context.getResourceAsStream("/WEB-INF/classes/messageresource.properties"), StandardCharsets.UTF_8);
                 Reader ps_ch_in = new InputStreamReader(context.getResourceAsStream("/WEB-INF/classes/messageresource_ch.properties"), StandardCharsets.UTF_8)) {
                ps.load(ps_in);
                ps_ch.load(ps_ch_in);
            }

            context.setAttribute("ps", ps);
            context.setAttribute("ps_ch", ps_ch);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        init();
        PrintWriter writer = response.getWriter();
        writer.println("The resource file is reloaded.");
        writer.close();
    }
}
