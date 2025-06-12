package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import model.User;

@WebFilter(filterName = "AuthenFilter3", urlPatterns = {"/*"})
public class AuthenFilter3 implements Filter {

    // Role constants
    private static final String US = "user";
    private static final String AD = "admin";
    // Login page
    private static final String LOGIN_PAGE = "login.jsp";
    private static final boolean DEBUG = true;

    private static final Set<String> ADMIN_FUNC = new HashSet<>();
    private static final Set<String> USER_FUNC = new HashSet<>();

    private static final Set<String> STATIC_RESOURCES = new HashSet<>(Arrays.asList(
            ".css", ".js", ".jpg", ".jpeg", ".png", ".gif", ".woff", ".svg"
    ));

    private FilterConfig filterConfig = null;

    public AuthenFilter3() {
        // Resources accessible by admin
        ADMIN_FUNC.addAll(Arrays.asList(
                "createUser.jsp",
                "deleteUser.jsp",
                "listUser.jsp",
                "selectUser.jsp",
                "updateUser.jsp",
                "createProduct.jsp",
                "deleteProduct.jsp",
                "updateProduct.jsp",
                "listProduct.jsp",
                "products",
                "users",
                "logout",
                "productListCart.jsp",
                "cart",
                                "cart.jsp",

                LOGIN_PAGE,
                "unauthorizedPage.jsp"
        ));

        // Resources accessible by normal user
        USER_FUNC.addAll(Arrays.asList(
                "userInf.jsp",
                "listProduct.jsp",
                "products",
                "logout",
                "productListCart.jsp",
                "cart",
                "cart.jsp",
                LOGIN_PAGE,
                "unauthorizedPage.jsp"
        ));
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        if (DEBUG) {
            System.out.println("AuthenFilter initialized.");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            String uri = req.getRequestURI();

            // Cho phép truy cập các tài nguyên tĩnh và trang login
            if (isStaticResource(uri) || uri.endsWith(LOGIN_PAGE) || uri.endsWith("/login")) {
                chain.doFilter(request, response);
                return;
            }

            // Lấy tên tài nguyên
            int index = uri.lastIndexOf("/");
            String resource = uri.substring(index + 1);

            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                res.sendRedirect(LOGIN_PAGE);
                return;
            }

            User user = (User) session.getAttribute("user");
            String role = user.getRole();

            // Phân quyền truy cập theo role
            if (US.equals(role.toLowerCase()) && USER_FUNC.contains(resource)) {
                chain.doFilter(request, response);
            } else if (AD.equals(role.toLowerCase()) && ADMIN_FUNC.contains(resource)) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect("unauthorizedPage.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();  // Nên dùng logger thực tế
            ((HttpServletResponse) response).sendRedirect(LOGIN_PAGE);
        }
    }

    @Override
    public void destroy() {
        // Clean up nếu cần
    }

    private boolean isStaticResource(String uri) {
        for (String ext : STATIC_RESOURCES) {
            if (uri.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
}
