<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Giỏ hàng</title>
    </head>
    <p><a href="${pageContext.request.contextPath}/productListCart.jsp">Shop Products</a></p>

    <body>
        <h2>Giỏ hàng của bạn</h2>
        <table border="1">
            <thead>
                <tr>
                    <th>Sản phẩm</th>
                    <th>Giá</th>
                    <th>Số lượng</th>
                    <th>Tổng</th>

                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${cart}">
                    <tr>
                        <td>${item.product.name}</td>
                        <td>${item.product.price}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/cart" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="update"/>
                                <input type="hidden" name="productId" value="${item.product.id}"/>
                                <input type="hidden" name="quantity" value="${item.quantity - 1}"/>
                                <input type="submit" value="-" ${item.quantity == 1 ? "disabled" : ""}/>
                            </form>
                            ${item.quantity}
                            <form action="${pageContext.request.contextPath}/cart" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="update"/>
                                <input type="hidden" name="productId" value="${item.product.id}"/>
                                <input type="hidden" name="quantity" value="${item.quantity + 1}"/>
                                <input type="submit" value="+"/>
                            </form>
                        </td>
                        <td>${item.product.price * item.quantity}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/cart" method="post">
                                <input type="hidden" name="action" value="remove"/>
                                <input type="hidden" name="productId" value="${item.product.id}"/>
                                <input type="submit" value="Xoá"/>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>

        </table>

        <form action="<%= request.getContextPath()%>/cart" method="post">
            <input type="hidden" name="action" value="checkout"/>
            <input type="submit" value="Tiến hành thanh toán"/>
        </form>

        <c:if test="${not empty message}">
            <p style="color:green;">${message}</p>
        </c:if>

    </body>
</html>
