package com.tomcat.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;

import java.io.IOException;

@WebFilter(
        filterName = "replaceTextFilter",
        value = "/note",
        initParams = {
                @WebInitParam(name = "search", value = "暴力"),
                @WebInitParam(name = "replace", value = "和平" )
        })
public class ReplaceTextFilter implements Filter {
    private FilterConfig config;
    private String searchString;
    private String replaceString;

    @Override public void init(FilterConfig filterConfig) {
        System.out.println("ReplaceTextFilter: init()");
        config = filterConfig;
        searchString = config.getInitParameter("search");
        replaceString = config.getInitParameter("replace");
    }

    @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("ReplaceTextFilter: doFilter()");
        ReplaceTextWrapper replaceTextWrapper = new ReplaceTextWrapper(response, searchString, replaceString);
        config.getServletContext().log("ReplaceTextFilter:before call chain.doFilter()");
        chain.doFilter(request, replaceTextWrapper);
        config.getServletContext().log("ReplaceTextFilter:after call chain.doFilter()");
        replaceTextWrapper.getOutputStream().close();
    }

    @Override public void destroy() {
        System.out.println("ReplaceTextFilter: destroy()");
        config = null;
    }
}
