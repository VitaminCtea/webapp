package com.tomcat.nonblockServlet;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MyReadListener implements ReadListener {
    private final ServletInputStream in;
    private final AsyncContext context;
    private final StringBuilder sb = new StringBuilder();
    public MyReadListener(ServletInputStream in, AsyncContext context) {
        this.in = in;
        this.context = context;
    }

    @Override public void onDataAvailable() throws IOException {
        System.out.println("数据可用！");
        try {
            TimeUnit.SECONDS.sleep(5);
            int len;
            byte[] buff = new byte[1024];
            while (in.isReady() && (len = in.read(buff)) > 0) sb.append(new String(buff, 0, len));
        } catch (InterruptedException e) { e.printStackTrace(); }
    }

    @Override public void onAllDataRead() throws IOException {
        System.out.println("数据读取完成！");
        System.out.println(sb);
        context.getRequest().setAttribute("msg", sb.toString());
        context.dispatch("/outputServlet");
    }

    @Override public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }
}
