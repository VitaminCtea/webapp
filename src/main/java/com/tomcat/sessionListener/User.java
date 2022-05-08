package com.tomcat.sessionListener;

import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionBindingListener;

import java.io.Serializable;

public class User implements HttpSessionBindingListener, Serializable {
    private final OnlineUsers onlineUsers = OnlineUsers.getInstance();
    private String name;

    public User(String name) { this.name = name; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // 当setAttribute时触发
    public void valueBound(HttpSessionBindingEvent event) {
        onlineUsers.add(name);
        System.out.println(name + " is bound with a session");
    }

    public void valueUnbound(HttpSessionBindingEvent event) {
        onlineUsers.remove(name);
        System.out.println(name + " is unbound with a session");
    }
}
