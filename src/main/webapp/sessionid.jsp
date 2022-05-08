<%--
  Created by IntelliJ IDEA.
  User: jiazh
  Date: 2022/4/24
  Time: 14:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Print Client Cookies</title>
</head>
<body>
<%
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
        out.println("no cookie");
        return;
    }
    for (Cookie cookie : cookies) { %>
<p>
    <b>Cookie name: </b>
    <%= cookie.getName() %>
    <b>Cookie value: </b>
    <%= cookie.getValue() %>
</p>
<p>
    <b>Max age in seconds: </b>
    <%= cookie.getMaxAge() %>
</p>
<% } %>
</body>
</html>
