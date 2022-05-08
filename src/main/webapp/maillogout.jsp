<%@ page import="com.tomcat.sessionListener.User" %><%--
  Created by IntelliJ IDEA.
  User: jiazh
  Date: 2022/4/24
  Time: 15:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=GB2312" language="java" %>
<html>
<head>
    <title>Mail Logout</title>
</head>
<body>
<%
    User user = (User) session.getAttribute("user");
    String name = null;
    if (user != null) name = user.getName();
    session.invalidate();
%>
<%= name %>再见！
<p>
    <a href="<%= response.encodeURL("maillogin.jsp") %> %">重新登录邮件系统</a>&nbsp;&nbsp;&nbsp;
</p>
</body>
</html>
