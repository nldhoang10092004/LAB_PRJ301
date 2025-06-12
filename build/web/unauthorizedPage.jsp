<%-- 
    Document   : unauthorizedPage
    Created on : Jun 9, 2025, 9:17:44 AM
    Author     : LOQ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>403 Unauthorized</h1>
        <p>User không đủ quyền truy cập :)</p>
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </body>
</html>
