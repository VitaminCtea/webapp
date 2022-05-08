<%--
  Created by IntelliJ IDEA.
  User: jiazh
  Date: 2022/4/24
  Time: 15:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=GB2312" language="java" %>
<%@ page import="com.tomcat.sessionListener.User" %>
<%@ page import="com.tomcat.sessionListener.OnlineUsers" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Mail Check</title>
</head>
<body>
<%
    String name = request.getParameter("username");
    User user;
    if (name != null) session.setAttribute("user", new User(name));
    else {
        user = (User) session.getAttribute("user");
        if (user == null) response.sendRedirect(response.encodeRedirectURL("maillogin.jsp"));
    }
%>
<a href="<%= response.encodeURL("maillogin.jsp") %>">登录</a>&nbsp;&nbsp;&nbsp;
<a href="<%= response.encodeURL("maillogout.jsp") %>">注销</a>
<p>当前用户为：<%= name %></p>
<p>您的信箱中有100封邮件</p>

<%
    OnlineUsers onlineUsers = OnlineUsers.getInstance();
    List<String> users = onlineUsers.getUsers();
%>
    <hr />
    <p>当前在线人数为：<%= onlineUsers.getCount() %></p><br />
<% for (String s : users) { %>
        <%= s %>&nbsp;&nbsp;
<% } %>
</body>
</html>
