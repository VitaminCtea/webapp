package com.tomcat.sessionListener;

import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

public class MySessionLifeListener implements HttpSessionListener, HttpSessionAttributeListener {
    public void sessionCreated(HttpSessionEvent se) { System.out.println("A new session is created."); }
    public void sessionDestroyed(HttpSessionEvent se) { System.out.println("A new session is to be destroyed"); }

    public void attributeAdded(HttpSessionBindingEvent event) {
        System.out.println("Attribute(" + event.getName() + "/" + event.getValue() + ") is added into a session");
    }

    public void attributeRemoved(HttpSessionBindingEvent event) {
        System.out.println("Attribute(" + event.getName() + "/" + event.getValue() + ") is removed into a session");
    }

    public void attributeReplaced(HttpSessionBindingEvent event) {
        System.out.println("Attribute(" + event.getName() + "/" + event.getValue() + ") is replaced into a session");
    }
}
