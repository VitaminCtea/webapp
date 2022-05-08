package com.tomcat.contextTesterServlet;

public class Counter {
    private int count;
    public Counter(int count) { this.count = count; }
    public int addStep(int step) { return (count += step); }
    public int getCount() { return count; }
}
