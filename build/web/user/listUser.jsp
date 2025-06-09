<%-- 
    Document   : listUser
    Created on : May 25, 2025, 9:43:00 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Employee List</h1>
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-default">Products</a>

        <p><a href="users?action=create">Add New User</a></p>
        <table border="1">
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>DOB</th>
                <th>Country</th>
                <th>Role</th>
                <th>Status</th>
                <th>Password</th>
            </tr>
            <c:forEach var="user" items="${users}">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.email}</td>
                    <td>${user.dob}</td>
                    <td>${user.country}</td>
                    <td>${user.role}</td>
                    <td>${user.status}</td>
                    <td>${user.password}</td>
                    <td>
                        <form action="users" method="get" style="display:inline;">
                            <input type="hidden" name="action" value="update" />
                            <input type="hidden" name="id" value="${user.id}" />
                            <input type="submit" value="Edit" />
                        </form>
                    </td>
                    <td>
                        <form action="users" method="get" style="display:inline;">
                            <input type="hidden" name="action" value="delete" />
                            <input type="hidden" name="id" value="${user.id}" />
                            <input type="submit" value="Delete" />
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <% out.println("Trong đề đưa ra database khi tạo bảng thì username được đặt là user, nếu không chạy được xin hãy check lại database");%>

    </body>
</html>
