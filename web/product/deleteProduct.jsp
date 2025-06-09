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
        <title>Delete Product</title>
    </head>
    <body>
        <form action="${pageContext.request.contextPath}/products?action=delete" method="POST">
            <h2>Are you want to delete product ${productName}</h2>
            <input type="hidden" name="id" value="${productId}" />
            <input type="submit" value="OK" />
            <a href="${pageContext.request.contextPath}/products" class="btn btn-default">Cancel</a>

        </form>

    </body>
</html>
