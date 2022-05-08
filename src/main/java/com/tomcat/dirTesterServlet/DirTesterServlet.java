package com.tomcat.dirTesterServlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

@WebServlet(name = "dirTesterServlet", value = "/dirTesterServlet")
public class DirTesterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        response.setContentType("text/html;charset=GB2312");
        PrintWriter writer = response.getWriter();
        Enumeration<String> enumeration = context.getAttributeNames();
        while (enumeration.hasMoreElements()) {
            String attributeName = enumeration.nextElement();
            writer.println("<br>" + attributeName + ": " + context.getAttribute(attributeName));
        }

        writer.close();

        File wordDir = (File) context.getAttribute("jakarta.servlet.context.tempdir");  // 获取Web项目的工作目录
        FileOutputStream fileOutputStream = new FileOutputStream(wordDir + "/temp.txt");
        fileOutputStream.write("temp file".getBytes());
        fileOutputStream.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
