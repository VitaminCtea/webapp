<%--
  Created by IntelliJ IDEA.
  User: jiazh
  Date: 2022/4/28
  Time: 12:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="mm" uri="/mytaglib" %>
<html>
<head>
    <title><mm:message key="hello.title" /></title>
</head>
<body>
    <b><mm:message key="hello.hi" />: ${ param.username }</b>
</body>
</html>
