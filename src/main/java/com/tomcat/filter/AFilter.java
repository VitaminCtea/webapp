package com.tomcat.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter(filterName = "aFilter", value = "/note")
public class AFilter implements Filter {
    @Override public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AFilter.init()");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("AFilter.doFilter()");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("AFilter.destroy()");
    }
}
