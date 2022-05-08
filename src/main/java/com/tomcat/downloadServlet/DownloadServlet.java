package com.tomcat.downloadServlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@WebServlet(name = "downloadServlet", value = "/downloadServlet")
public class DownloadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        OutputStream out = response.getOutputStream();
        String filename = request.getParameter("filename");
        if (filename == null) {
            out.write("Please enter the name of the file to download.".getBytes());
            out.close();
            return;
        }

        ServletContext context = getServletContext();
        String tomcatProjectFilePath = "/files/" + filename;
        String filePath = context.getRealPath(tomcatProjectFilePath);
        File file = new File(filePath);
        if (!file.exists()) {
            out.write("The file name you entered does not exist in the server. Please try again".getBytes());
            out.close();
            return;
        }

        InputStream in = context.getResourceAsStream(tomcatProjectFilePath);
        response.setContentType("application/force-download");  // 让浏览器以下载的方式来处理响应正文
        response.setHeader("Content-Length", String.valueOf(in.available()));
        response.setHeader("content-Disposition", "attachment;filename=\"" + filename + "\" ");

        int bytesRead;
        byte[] buffer = new byte[512];
        while ((bytesRead = in.read(buffer)) != -1) out.write(buffer, 0, bytesRead);

        in.close();
        out.close();
    }
}
