package com.tomcat.dispatcherServlet;

import static com.tomcat.helper.Helper.RandomColor.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebServlet(name = "dispatcher", value = "/dispatcher")
public class DispatcherServlet extends GenericServlet {
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        request.setAttribute("USER_NAME", request.getParameter("username"));
        request.setAttribute("PASSWORD", request.getParameter("password"));
        ServletContext context = request.getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher("/hello.jsp");
        dispatcher.forward(request, response);
        printHttpServletRequestInterfaceHttpRequestInfoMethod(request);
    }

    private void printHttpServletRequestInterfaceHttpRequestInfoMethod(ServletRequest request) {
        HttpServletRequest r = (HttpServletRequest) request;
        System.out.println(createColorText("ContextPath: " + r.getContextPath()));

        System.out.println("Cookies info: ");
        Cookie[] cookies = r.getCookies();
        for (Cookie cookie : cookies) {
            getAttribute("Comment", cookie.getComment());
            getAttribute("Domain", cookie.getDomain());
            getAttribute("Name", cookie.getName());
            getAttribute("Path", cookie.getPath());
            getAttribute("Value", cookie.getValue());
            getAttribute("MaxAge", cookie.getMaxAge());
            getAttribute("Secure", cookie.getSecure());
        }

        System.out.println(createColorText("Content-Type: " + r.getHeader("Content-Type")));
        System.out.println(createColorText("Request Method: " + r.getMethod()));
        System.out.println(createColorText("RequestURI: " + r.getRequestURI()));
        System.out.println(createColorText("Query Parameter: " + r.getQueryString()));
    }

    private void getAttribute(String name, Object info) { System.out.println(createColorText("\t" + name + ": " + info)); }
}
