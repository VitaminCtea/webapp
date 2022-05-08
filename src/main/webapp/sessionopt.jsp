<%--
  Created by IntelliJ IDEA.
  User: jiazh
  Date: 2022/4/26
  Time: 1:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=GB2312" language="java" %>
<%@ page import="com.tomcat.sessionListener.MyData" %>
<html>
<head>
    <title>Session Listener Demo</title>
</head>
<body>
    <%
        String action = request.getParameter("action");
        if (action == null) {
    %>
            <a href="sessionopt.jsp?action=add">加入属性</a>
            <a href="sessionopt.jsp?action=invalidate">结束会话</a>
    <% }
        else if (action.equals("invalidate")) {
            session.invalidate();
    %>
            <a href="sessionopt.jsp">开始新的会话</a>
    <% }
        else if (action.equals("add")) {
            session.setAttribute("data", new MyData(1));
    %>
            <a href="sessionopt.jsp?action=replace">替换属性</a>
            <a href="sessionopt.jsp?action=remove">删除属性</a>
            <a href="sessionopt.jsp?action=invalidate">结束会话</a>
    <% }
        else if (action.equals("remove")) {
            session.removeAttribute("data");
    %>
            <a href="sessionopt.jsp?action=add">加入属性</a>
            <a href="sessionopt.jsp?action=invalidate">结束会话</a>
    <% }
        else if (action.equals("replace")) {
            session.setAttribute("data", new MyData(1));
    %>
            <a href="sessionopt.jsp?action=remove">删除属性</a>
            <a href="sessionopt.jsp?action=invalidate">结束会话</a>
    <% }%>
</body>
</html>
