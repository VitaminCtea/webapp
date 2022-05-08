package com.tomcat.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(
        filterName = "NoteFilter",
        value = "/note",
        initParams = {
                @WebInitParam(name = "ipBlock", value = "221.4"),
                @WebInitParam(name = "blackList", value = "捣蛋鬼")
        })
public class NoteFilter implements Filter {
    private FilterConfig config;
    private String blackList;
    private String ipBlock;

    @Override public void init(FilterConfig filterConfig) {
        System.out.println("NoteFilter: init()");
        config = filterConfig;
        ipBlock = config.getInitParameter("ipBlock");
        blackList = config.getInitParameter("blackList");
    }

    @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("NoteFilter: doFilter()");
        if (!checkRemoteIP(request, response) || !checkUsername(request, response)) return;

        long before = System.currentTimeMillis();

        config.getServletContext().log("NoteFilter:before call chain.doFilter()");
        chain.doFilter(request, response);
        config.getServletContext().log("NoteFilter:after call chain.doFilter()");

        long after = System.currentTimeMillis();

        String name = "";
        if (request instanceof HttpServletRequest) name = ((HttpServletRequest) request).getRequestURI();
        config.getServletContext().log("NoteFilter: " + name + ": " + (after - before) + "ms");
    }

    @Override public void destroy() {
        System.out.println("NoteFilter: destroy()");
        config = null;
    }

    private boolean checkRemoteIP(ServletRequest request, ServletResponse response) throws IOException {
        String addr = request.getRemoteAddr();
        if (addr.indexOf(ipBlock) == 0) {
            response.setContentType("text/html;charset=GB2312");
            PrintWriter writer = response.getWriter();
            writer.println("<h1>对不起，服务器无法为您提供服务</h1>");
            writer.flush();
            return false;
        }

        return true;
    }

    private boolean checkUsername(ServletRequest request, ServletResponse response) throws IOException {
        String username = request.getParameter("username");
        if (username != null && username.contains(blackList)) {
            response.setContentType("text/html;charset=GB2312");
            PrintWriter writer = response.getWriter();
            writer.println("<h1>对不起，" + username + ", 你没有权限留言</h1>");
            writer.flush();
            return false;
        }
        return true;
    }
}
