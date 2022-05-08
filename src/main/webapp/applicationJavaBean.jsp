<%--
  Created by IntelliJ IDEA.
  User: jiazh
  Date: 2022/4/26
  Time: 15:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Application Range JavaBean Demo</title>
</head>
<body>
<%-- scope为application时，不限制是否是同一个浏览器，在多个浏览器进行共享 --%>
<jsp:useBean id="javaBean" class="com.tomcat.javaBean.CounterJavaBean" scope="application" />
<jsp:setProperty name="javaBean" property="count" value="<%= javaBean.getCount() + 1 %>" />
Current count value is:
<jsp:getProperty name="javaBean" property="count" />
</body>
</html>
