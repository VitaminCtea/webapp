package com.tomcat.counterServlet;

import com.tomcat.contextTesterServlet.Counter;
import com.tomcat.helper.Helper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.*;

// 统计访问网站的用户数量(当然能，这种方法也会有一定的限制，就是统一位用户反复访问网站时候也会递增，要想获取真实的访问数量，需要对用户的标识进行判断)
// ServletContext对象会在destroy生命周期被销毁，而在运行期间会一直存在
@WebServlet(name = "counterServlet", value = "/counterServlet")
public class CounterServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletContext context = getServletContext();
        Counter counter = (Counter) context.getAttribute("counter");
        Helper.HTML html = new Helper.HTML(response);
        html.generatorHTML(writer ->
                writer.println("<h1>欢迎光临本站，您是第 " + counter.addStep(1) + " 位访问者</h1>"), "counterServlet");
    }
}
