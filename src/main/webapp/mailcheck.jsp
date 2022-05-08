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
<a href="<%= response.encodeURL("maillogin.jsp") %>">��¼</a>&nbsp;&nbsp;&nbsp;
<a href="<%= response.encodeURL("maillogout.jsp") %>">ע��</a>
<p>��ǰ�û�Ϊ��<%= name %></p>
<p>������������100���ʼ�</p>

<%
    OnlineUsers onlineUsers = OnlineUsers.getInstance();
    List<String> users = onlineUsers.getUsers();
%>
    <hr />
    <p>��ǰ��������Ϊ��<%= onlineUsers.getCount() %></p><br />
<% for (String s : users) { %>
        <%= s %>&nbsp;&nbsp;
<% } %>
</body>
</html>
