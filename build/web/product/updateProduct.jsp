<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Product Edit</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
            }
            .form-group {
                margin-bottom: 15px;
            }
            label {
                display: block;
                margin-bottom: 5px;
                font-weight: bold;
            }
            input[type="text"],
            input[type="email"] {
                width: 100%;
                padding: 8px;
                border: 1px solid #ddd;
                border-radius: 4px;
                box-sizing: border-box;
            }
            .btn {
                display: inline-block;
                padding: 6px 12px;
                margin-bottom: 0;
                font-size: 14px;
                font-weight: 400;
                line-height: 1.42857143;
                text-align: center;
                white-space: nowrap;
                vertical-align: middle;
                cursor: pointer;
                border: 1px solid transparent;
                border-radius: 4px;
                text-decoration: none;
            }
            .btn-primary {
                color: #fff;
                background-color: #337ab7;
                border-color: #2e6da4;
            }
            .btn-default {
                color: #333;
                background-color: #fff;
                border-color: #ccc;
            }
        </style>
    </head>
    <body>
        <h1>Employee Form</h1>
        <form action="${pageContext.request.contextPath}/products?action=update" method="post">
            
             <div class="form-group">
                <label for="id">ID:</label>
                <input type="hidden" id="id" name="id" placeholder="ID" value = "${product.id}">
            </div>
            
            <div class="form-group">
                <label for="name">Name:</label>
                <input type="text" id="name" name="name" placeholder="Product Name" value = "${product.name}" >
            </div>

            <div class="form-group">
                <label for="price">Price</label>
                <input type="text" id="price" name="price" placeholder="Price" value = "${product.price}" >
            </div>

            <div class="form-group">
                <label for="description">Description</label>
                <input type="text" id="description" name="description" placeholder="Description" value = "${product.description}">
            </div>

            <div class="form-group">
                <label for="stock">Stock</label>
                <input type="text" id="stock" name="stock" placeholder="Stock"  value = "${product.stock}">
            </div>   
            
            <div class="form-group">
                <button type="submit" class="btn btn-primary">Save</button>
            </div>
        </form>
        <a href="${pageContext.request.contextPath}/products" class="btn btn-default">Cancel</a>
    </body>
</html>