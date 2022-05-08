package com.tomcat.sessionListener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

public class OnlineCounterListener implements HttpSessionListener {
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        ServletContext context = session.getServletContext();
        Integer counter = (Integer) context.getAttribute("counterPerson");
        context.setAttribute("counterPerson", counter == null ? Integer.valueOf(1) : Integer.valueOf(counter + 1));
        session.setMaxInactiveInterval(60);
        System.out.println("A new session is created");
    }
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        ServletContext context = session.getServletContext();
        Integer counter = (Integer) context.getAttribute("counterPerson");
        if (counter == null) return;
        context.setAttribute("counterPerson", --counter);
        System.out.println("A new session is to be destroyed");
    }
}
