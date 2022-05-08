package com.tomcat.uploadServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet(name = "uploadServlet", value = "/uploadServlet")
@MultipartConfig
public class UploadServlet extends HttpServlet {
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        String savePath = request.getServletContext().getRealPath("/store");

        File file = new File(savePath);
        if (!file.exists()) file.mkdir();

        PrintWriter writer = response.getWriter();
        Collection<Part> parts = request.getParts();

        for (Part part: parts) {
            String header = part.getHeader("content-disposition");
            System.out.println("------------------------------------part------------------------------------");
            System.out.println("type: " + part.getContentType());
            System.out.println("size: " + part.getSize());
            System.out.println("name: " + part.getName());
            System.out.println("header: " + header);

            if (part.getContentType() == null) {
                String name = part.getName();
                String value = request.getParameter(name);
                writer.println(name + ": " + value + "\r\n");
            } else if (part.getName().contains("file")) {
                String filename = getFileName(header);
                part.write(savePath + File.separator + filename);
                writer.println(filename + " is saved.");
                writer.println("The size of " + filename + " is " + part.getSize() + " byte\r\n");
            }
        }
        writer.close();
    }

    private String getFileName(String header) { return header.substring(header.lastIndexOf("=") + 2, header.length() - 1); }
}
