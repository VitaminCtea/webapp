package com.tomcat.myServletContextListener;

import com.tomcat.contextTesterServlet.Counter;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

// @WebListener注解等价于在web.xml文件中配置<listener><listener-class>com.tomcat.myServletContextListener.MyServletContextListener</listener-class></listener>
@WebListener
public class MyServletContextListener implements ServletContextListener {
    @Override public void contextInitialized(ServletContextEvent sce) {
        System.out.println("webapp application is Initialized.");
        ServletContext context = sce.getServletContext();

        try {
            Properties properties = new Properties();
            // tomcat默认目录指向打包后的war文件，展开目录就是根目录，即：C:\Users\jiazh\webapp\target\webapp-1.0-SNAPSHOT
            properties.load(context.getResourceAsStream("/WEB-INF/classes/counter.properties"));
            context.setAttribute("counter", new Counter(Integer.parseInt(properties.getProperty("counter"))));
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("webapp application is Destroyed.");

        ServletContext context = sce.getServletContext();
        Counter counter = (Counter) context.getAttribute("counter");
        if (counter != null) {
            try {
                File file = new File(context.getRealPath("/WEB-INF/classes/counter.properties"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                int count = ((Counter) context.getAttribute("counter")).getCount();

                String description = "# The number of visitors has increased!\n";
                String dateComment = "# " + formatter.format(LocalDateTime.now()) + "\n";
                String counterKey = "counter = ";

                if (randomAccessFile.readLine().startsWith("#")) {
                    randomAccessFile.seek(description.getBytes().length);
                    randomAccessFile.writeBytes(dateComment);
                    randomAccessFile.seek(randomAccessFile.getFilePointer() + counterKey.getBytes().length);    // seek方法参数是绝对值，而不是相对的
                } else {
                    randomAccessFile.seek(0);
                    randomAccessFile.writeBytes(description + dateComment + counterKey);
                }

                randomAccessFile.writeBytes(String.valueOf(count));
                randomAccessFile.close();
            } catch (IOException e) { e.printStackTrace(); }
        }
    }
}
