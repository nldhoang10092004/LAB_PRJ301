<%-- 
    Document   : deleteEMP
    Created on : May 17, 2025, 9:46:45 AM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Employees</title>
    </head>
    <body>
        <form action="${pageContext.request.contextPath}/users?action=delete" method="POST">
            <h2>Are you want to delete user ${username}</h2>
            <input type="hidden" name="id" value="${userId}" />
            <input type="submit" value="OK" />
            <a href="${pageContext.request.contextPath}/users" class="btn btn-default">Cancel</a>

        </form>

    </body>
</html>
