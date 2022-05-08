<%--
  Created by IntelliJ IDEA.
  User: jiazh
  Date: 2022/4/28
  Time: 13:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="mm" uri="/mytaglib" %>
<%
    String language = request.getParameter("language");
    if (language == null) language = "English";
    session.setAttribute("language", language);
%>
<html>
<head>
    <title><mm:message key="login.title" /></title>
</head>
<body>
    <br>
    <form name="loginForm" method="post" action="customLabelExample.jsp">
        <table>
            <tr>
                <td><div align="right"><mm:message key="login.user" />: </div></td>
                <td><input type="text" name="username" /></td>
            </tr>
            <tr>
                <td><div align="right"><mm:message key="login.password" />: </div></td>
                <td><input type="password" name="password" /></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" name="submit" value="<mm:message key="login.submit" />" /></td>
            </tr>
        </table>
    </form>
</body>
</html>
