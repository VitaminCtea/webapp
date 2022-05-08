package com.tomcat.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.IOException;

public class ReplaceTextWrapper extends HttpServletResponseWrapper {
    private final ReplaceTextStream replaceTextStream;
    public ReplaceTextWrapper(ServletResponse response, String searchString, String replaceString) throws IOException {
        super((HttpServletResponse) response);
        replaceTextStream = new ReplaceTextStream(response.getOutputStream(), searchString, replaceString);
    }

    @Override public ServletOutputStream getOutputStream() { return replaceTextStream; }
}
