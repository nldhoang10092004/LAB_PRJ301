package controller;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;
import service.IUserService;
import service.UserService;
import userDAO.UserDAO;

/**
 *
 * @author ADMIN
 */
public class UserManagement extends HttpServlet {

    private IUserService service;

    @Override
    public void init() throws ServletException {
        this.service = new UserService();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UserManagement</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UserManagement at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
        // Kiểm tra cookie để tự động đăng nhập nếu có
        Cookie[] cookies = request.getCookies();
        String username = null, password = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) username = cookie.getValue();
                if ("password".equals(cookie.getName())) password = cookie.getValue();
            }
        }
        if (username != null && password != null) {
            try {
                User user = UserDAO.checkLogin(username, password);
                if (user != null) {
                    session = request.getSession();
                    session.setAttribute("user", user);
                }
            } catch (SQLException e) {
                // Bỏ qua lỗi, chuyển hướng về login
            }
        }
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
    }
    // Tiếp tục xử lý các yêu cầu
    String action = request.getParameter("action");
    if (action == null) {
        action = "";
    }
    switch (action) {
        case "create":
            getCreateUser(request, response);
            break;
        case "update":
            getUpdateUser(request, response);
            break;
        case "delete":
            getDeleteUser(request, response);
            break;
        case "select":
            selectUser(request, response);
            break;
        default:
            getListUsers(request, response);
            break;
    }
}

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    // Tiếp tục xử lý các yêu cầu POST
    String action = request.getParameter("action");
    if (action == null) {
        action = "";
    }
    switch (action) {
        case "create":
            postCreateUser(request, response);
            break;
        case "update":
            postUpdateUser(request, response);
            break;
        case "delete":
            postDeleteUser(request, response);
            break;
        case "select":
            selectUser(request, response);
            break;
        default:
            break;
    }
}

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void getListUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> userlist = service.getAllUsers();
        request.setAttribute("users", userlist);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/listUser.jsp");
        dispatcher.forward(request, response);
    }

    private void getCreateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/user/createUser.jsp");
    }

    private void getUpdateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            User user = service.getUserById(id);
            if (user != null) {
                request.setAttribute("user", user);
                request.getRequestDispatcher("/user/updateUser.jsp").forward(request, response);
            } else {
                request.setAttribute("msg", "Edit failed!");
                getListUsers(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("msg", "ID không hợp lệ!");
            getListUsers(request, response);
        }
    }

    private void getDeleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            User user = service.getUserById(id);
            if (user != null) {
                request.setAttribute("userId", user.getId());
                request.setAttribute("username", user.getUsername());
                request.getRequestDispatcher("/user/deleteUser.jsp").forward(request, response);
            } else {
                request.setAttribute("msg", "Delete failed!");
                getListUsers(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("msg", "ID không hợp lệ!");
            getListUsers(request, response);
        }
    }

    private void selectUser(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

   private void postCreateUser(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    try {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String country = request.getParameter("country");
        String role = request.getParameter("role");
        String password = request.getParameter("password");
        String dob = request.getParameter("dob"); // Giữ nguyên yyyy-MM-dd

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên không được để trống!");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email không hợp lệ!");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự!");
        }

        boolean status = request.getParameter("status") != null;
        User user = new User(0, name, email, country, role, status, password, dob); // id = 0 vì tự tăng
        service.createUser(user);
        getListUsers(request, response);
    } catch (IllegalArgumentException e) {
        request.setAttribute("error", e.getMessage());
        getCreateUser(request, response);
    } catch (Exception e) {
        request.setAttribute("error", "Lỗi khi tạo user: " + e.getMessage());
        getCreateUser(request, response);
    }
}

private void postUpdateUser(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    try {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String country = request.getParameter("country");
        String role = request.getParameter("role");
        String password = request.getParameter("password");
        String dob = request.getParameter("dob"); // Giữ nguyên yyyy-MM-dd

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên không được để trống!");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email không hợp lệ!");
        }

        boolean status = request.getParameter("status") != null;
        User user = new User(id, name, email, country, role, status, password, dob);
        service.updateUser(user);
        getListUsers(request, response);
    } catch (IllegalArgumentException e) {
        request.setAttribute("error", e.getMessage());
        getUpdateUser(request, response);
    } catch (Exception e) {
        request.setAttribute("error", "Lỗi khi cập nhật user: " + e.getMessage());
        getUpdateUser(request, response);
    }
}

    private void postDeleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            service.deleteUser(id);
            getListUsers(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("msg", e.getMessage());
            getListUsers(request, response);
        } catch (Exception e) {
            request.setAttribute("msg", "Lỗi khi xoá user: " + e.getMessage());
            getListUsers(request, response);
        }
    }
}