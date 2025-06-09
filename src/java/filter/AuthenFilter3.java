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
    private static final String US = "us";
    private static final String AD = "ad";
    private static final String LOGIN_PAGE = "login2.jsp";
    private static final boolean DEBUG = true;

    private static final Set<String> ADMIN_FUNC = new HashSet<>();
    private static final Set<String> USER_FUNC = new HashSet<>();

    private static final Set<String> STATIC_RESOURCES = new HashSet<>(Arrays.asList(
            ".css", ".js", ".jpg", ".jpeg", ".png", ".gif", ".woff", ".svg"
    ));

    private FilterConfig filterConfig = null;

    public AuthenFilter3(){
        // Các tài nguyên cho admin
        ADMIN_FUNC.add("admin2.jsp");
        ADMIN_FUNC.add("searchuser");
        ADMIN_FUNC.add(LOGIN_PAGE);

        // Các tài nguyên cho user thông thường
        USER_FUNC.add("userInf.jsp");
        USER_FUNC.add(LOGIN_PAGE);
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
            if (isStaticResource(uri) || uri.endsWith(LOGIN_PAGE)) {
                chain.doFilter(request, response);
                return;
            }

            // Lấy tên tài nguyên
            int index = uri.lastIndexOf("/");
            String resource = uri.substring(index + 1);

            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("username") == null) {
                res.sendRedirect(LOGIN_PAGE);
                return;
            }

            User user = (User) session.getAttribute("username");
            String role = user.getRole();

            // Phân quyền truy cập theo role
            if (US.equals(role) && USER_FUNC.contains(resource)) {
                chain.doFilter(request, response);
            } else if (AD.equals(role) && ADMIN_FUNC.contains(resource)) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(LOGIN_PAGE);
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