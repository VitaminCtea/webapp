<%--
  Created by IntelliJ IDEA.
  User: jiazh
  Date: 2022/4/24
  Time: 15:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=GB2312" language="java" %>
<%@ page import="com.tomcat.sessionListener.User" %>
<html>
<head>
    <title>Mail Login</title>
</head>
<body onload="document.loginForm.username.focus()">
<%
    String name = "";
    User user;
    if (!session.isNew()) {
        user = (User) session.getAttribute("user");
        if (user == null) name = "";
        else name = user.getName();
    }
%>
<p>欢迎光临邮件系统</p>
<p>Session ID: <%= session.getId() %></p>
<table width="500" border="0">
    <tr>
        <td>
            <table width="500" border="0">
                <form name="loginForm" method="post" action="<%= response.encodeURL("mailcheck.jsp") %>">
                    <tr>
                        <td width="401">
                            <div align="right">User Name:&nbsp;</div>
                        </td>
                        <td width="399">
                            <input type="text" name="username" value="<%= name %>"/>
                        </td>
                    </tr>
                    <tr>
                        <td width="401">
                            <div align="right">Password:&nbsp;</div>
                        </td>
                        <td width="399">
                            <input type="password" name="password"/>
                        </td>
                    </tr>
                    <tr>
                        <td width="401">&nbsp;</td>
                        <td width="399">
                            <br/>
                            <input type="submit" name="submit" value="提交"/>
                        </td>
                    </tr>
                </form>
            </table>
        </td>
    </tr>
</table>
</body>
</html>
