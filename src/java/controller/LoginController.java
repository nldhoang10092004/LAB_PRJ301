/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import model.User;
import userDAO.UserDAO;
import userDAO.IUserDAO;

/**
 *
 * @author LOQ
 */
@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    private IUserDAO userDao;

    public LoginController() {
        this.userDao = new UserDAO();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra và decode cookie values trước khi hiển thị form
        Cookie[] cookies = request.getCookies();
        String savedUsername = "";
        String savedPassword = "";
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                try {
                    if ("username".equals(cookie.getName())) {
                        savedUsername = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                    } else if ("password".equals(cookie.getName())) {
                        savedPassword = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                    }
                } catch (Exception e) {
                    // Nếu decode lỗi, bỏ qua cookie này
                    System.out.println("Error decoding cookie: " + e.getMessage());
                }
            }
        }
        
        request.setAttribute("savedUsername", savedUsername);
        request.setAttribute("savedPassword", savedPassword);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        try {
            if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                request.setAttribute("error", "Tên đăng nhập và mật khẩu không được để trống!");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            User user = UserDAO.checkLogin(username, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                if ("on".equals(rememberMe)) {
                    try {
                        // Encode cookie values để tránh lỗi với ký tự đặc biệt
                        String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8);
                        String encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8);
                        
                        Cookie userCookie = new Cookie("username", encodedUsername);
                        userCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
                        userCookie.setPath(request.getContextPath());
                        userCookie.setHttpOnly(true); // Bảo mật hơn
                        response.addCookie(userCookie);

                        Cookie passCookie = new Cookie("password", encodedPassword);
                        passCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
                        passCookie.setPath(request.getContextPath());
                        passCookie.setHttpOnly(true); // Bảo mật hơn
                        response.addCookie(passCookie);
                    } catch (Exception e) {
                        System.out.println("Error creating cookies: " + e.getMessage());
                        // Tiếp tục đăng nhập mà không lưu cookie
                    }
                } else {
                    // Xóa cookie nếu user không chọn "Remember Me"
                    clearCookies(request, response);
                }

                response.sendRedirect(request.getContextPath() + "/users");
            } else {
                request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
                request.setAttribute("savedUsername", username); // Giữ lại username đã nhập
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi đăng nhập: " + e.getMessage());
            request.setAttribute("savedUsername", username); // Giữ lại username đã nhập
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    /**
     * Phương thức helper để xóa cookies
     */
    private void clearCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName()) || "password".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath(request.getContextPath());
                    response.addCookie(cookie);
                }
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Login Controller with Cookie Support";
    }// </editor-fold>
}