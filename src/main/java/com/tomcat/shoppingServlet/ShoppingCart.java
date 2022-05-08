package com.tomcat.shoppingServlet;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCart implements Serializable {
    private static final long serialVersionUID = -8941117567006742755L;
    private final Map<String, Integer> items = new ConcurrentHashMap<>();
    private volatile String token;
    private int numberOfItems;

    public void add(String itemName) {
        if (items.containsKey(itemName)) items.computeIfPresent(itemName, (key, val) -> val + 1);
        else items.put(itemName, 1);
        synchronized (this) { numberOfItems++; }
    }

    public synchronized void addToken(String token) { this.token = token; }

    public synchronized int getNumberOfItems() { return numberOfItems; }
    public synchronized Map<String, Integer> getItems() { return Collections.unmodifiableMap(items); }
    public String getToken() { return token; }
    public boolean isTokensEmpty() { return token == null; }

    @Override public String toString() { return "ShoppingCart"; }
}

