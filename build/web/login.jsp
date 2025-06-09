<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <style>
        body { 
            font-family: Arial, sans-serif; 
            margin: 20px; 
            background-color: #f5f5f5;
        }
        .login-container {
            max-width: 400px;
            margin: 50px auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .form-group { 
            margin-bottom: 15px; 
        }
        label { 
            display: block; 
            margin-bottom: 5px; 
            font-weight: bold; 
            color: #333;
        }
        input[type="text"], input[type="password"] {
            width: 100%; 
            padding: 10px; 
            border: 1px solid #ddd; 
            border-radius: 4px; 
            box-sizing: border-box;
            font-size: 14px;
        }
        input[type="text"]:focus, input[type="password"]:focus {
            border-color: #337ab7;
            outline: none;
            box-shadow: 0 0 5px rgba(51, 122, 183, 0.3);
        }
        .btn { 
            padding: 10px 20px; 
            font-size: 14px; 
            cursor: pointer; 
            border-radius: 4px; 
            border: none;
            width: 100%;
        }
        .btn-primary { 
            color: #fff; 
            background-color: #337ab7; 
            transition: background-color 0.3s;
        }
        .btn-primary:hover {
            background-color: #286090;
        }
        .error { 
            color: #d9534f; 
            background-color: #f2dede;
            border: 1px solid #ebccd1;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 15px;
        }
        .checkbox-group {
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .checkbox-group input[type="checkbox"] {
            width: auto;
        }
        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <h1>Đăng Nhập</h1>
        
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="username">Tên đăng nhập:</label>
                <input type="text" 
                       id="username" 
                       name="username" 
                       value="${not empty savedUsername ? savedUsername : ''}" 
                       required 
                       autocomplete="username">
            </div>
            
            <div class="form-group">
                <label for="password">Mật khẩu:</label>
                <input type="password" 
                       id="password" 
                       name="password" 
                       value="${not empty savedPassword ? savedPassword : ''}" 
                       required
                       autocomplete="current-password">
            </div>
            
            <div class="form-group">
                <div class="checkbox-group">
                    <input type="checkbox" 
                           id="rememberMe" 
                           name="rememberMe" 
                           ${not empty savedUsername ? 'checked' : ''}>
                    <label for="rememberMe">Ghi nhớ đăng nhập</label>
                </div>
            </div>
            
            <div class="form-group">
                <button type="submit" class="btn btn-primary">Đăng Nhập</button>
            </div>
        </form>
    </div>
</body>
</html>