package com.tomcat.mainServlet;

import com.tomcat.helper.Helper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "mainServlet", value = "/mainServlet")
public class MainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        Helper.HTML html = new Helper.HTML(response);
        ServletContext context = getServletContext();
        RequestDispatcher headRequestDispatcher = context.getRequestDispatcher("/header.html");
        RequestDispatcher greetRequestDispatcher = context.getRequestDispatcher("/greetServlet");   // 这里应该是其他共享组件的URL，而不是一个Servlet
        RequestDispatcher footerRequestDispatcher = context.getRequestDispatcher("/footer.html");
        html.generatorHTML(writer -> {
            try {
                /*
                 * dispatcher.include(request, response)会把当前组件的响应结果进行替换值
                 * 例如：在这个组件提交了一个HTML、servlet或JSP响应，那么在源组件会直接替换引入的诸如request.getParameter(username)值
                 * 在目标组件中对响应状态代码或者响应头所做的修改都会被忽略，最终的结果是一个包含header、ServletGreet组件、footer的这样的一个页面
                 * 另外，针对这个例子，在其他组件(GreetServlet共享组件)提交的响应不能提交一个HTML，不然footer会显示不出来
                 * 控制权include方法是源组件，而forward方法的控制权是目标组件(这里的控制权是指提交响应、修改响应头或关闭响应流等等)
                 */
                headRequestDispatcher.include(request, response);
                greetRequestDispatcher.include(request, response);
                footerRequestDispatcher.include(request, response);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        }, "RequestDispatcher include method demo");
    }
}
