<!-- productList.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Product List</title>
    </head>
    <body>
        <h2>Product List</h2>
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
                <a href="${pageContext.request.contextPath}/users" class="btn btn-default">Users</a>
        <p><a href="products?action=create">Add New Product</a></p>
        <p><a href="products?action=cart">Shop Products</a></p>
        <%-- Thiết lập số sản phẩm hiển thị trên mỗi trang --%>
        <c:set var="pageSize" value="10"/>
        <c:set var="currentPage" value="${param.page != null ? param.page : 1}"/>

        <c:set var="start" value="${(currentPage - 1) * pageSize}"/>
        <c:set var="end" value="${start + pageSize}"/>
        <c:set var="totalProducts" value="${products.size()}"/>
        <c:set var="totalPages" value="${Math.ceil(totalProducts / pageSize)}"/>
        <table border="1">
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Price</th>
                <th>Description</th>
                <th>Stock</th>
            </tr>
            <c:forEach var="product" items="${products}" varStatus="status">
                <c:if test="${status.index >= start && status.index < end}">
                    <tr>
                        <td>${product.id}</td>
                        <td>${product.name}</td>
                        <td>${product.price}</td>
                        <td>${product.description}</td>
                        <td>${product.stock}</td>
                        <td>
                            <form action="products" method="get" style="display:inline;">
                                <input type="hidden" name="action" value="update" />
                                <input type="hidden" name="id" value="${product.id}" />
                                <input type="submit" value="Edit" />
                            </form>
                        </td>
                        <td>
                            <form action="products" method="get" style="display:inline;">
                                <input type="hidden" name="action" value="delete" />
                                <input type="hidden" name="id" value="${product.id}" />
                                <input type="submit" value="Delete" />
                            </form>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>

        </table>
        <!-- Phân trang -->
        <div>
            <c:if test="${currentPage > 1}">
                <a href="products?page=${currentPage - 1}">Previous</a>
            </c:if>

            <c:forEach var="i" begin="1" end="${totalPages}">
                <a href="products?page=${i}">${i}</a>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <a href="products?page=${currentPage + 1}">Next</a>
            </c:if>
        </div>
    </body>
</html>
