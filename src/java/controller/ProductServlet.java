package controller;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Product;
import service.IProductService;
import service.ProductService;
import userDAO.UserDAO;
import model.User;

/**
 *
 * @author LOQ
 */
@WebServlet(name = "ProductServlet", urlPatterns = {"/products"})
public class ProductServlet extends HttpServlet {

    private IProductService productService;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductService();
    }

   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra session và authentication (tương tự UserManagement)
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // Kiểm tra cookie để tự động đăng nhập nếu có
            Cookie[] cookies = request.getCookies();
            String username = null, password = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("username".equals(cookie.getName())) {
                        username = cookie.getValue();
                    }
                    if ("password".equals(cookie.getName())) {
                        password = cookie.getValue();
                    }
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

        // Xử lý các action
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "create":
                getCreateProduct(request, response);
                break;
            case "update":
                getUpdateProduct(request, response);
                break;
            case "delete":
                getDeleteProduct(request, response);
                break;
            case "select":
                selectProduct(request, response);
                break;
            case "cart":
                getListProductsForCart(request, response);
                break;
            default:
                getListProducts(request, response);
                break;
        }
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
        // Kiểm tra session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Xử lý các action POST
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "create":
                postCreateProduct(request, response);
                break;
            case "update":
                postUpdateProduct(request, response);
                break;
            case "delete":
                postDeleteProduct(request, response);
                break;
            case "select":
                selectProduct(request, response);
                break;

            default:
                getListProducts(request, response);
                break;
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Product Management Servlet";
    }

    // Phương thức hiển thị danh sách sản phẩm
    private void getListProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> productList = productService.getAllProducts();
        request.setAttribute("products", productList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("product/listProduct.jsp");
        dispatcher.forward(request, response);
    }

    // Phương thức hiển thị form tạo sản phẩm
    private void getCreateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/product/createProduct.jsp");
    }

    // Phương thức hiển thị form cập nhật sản phẩm
    private void getUpdateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Product product = productService.getProductById(id);
            if (product != null) {
                request.setAttribute("product", product);
                request.getRequestDispatcher("/product/updateProduct.jsp").forward(request, response);
            } else {
                request.setAttribute("msg", "Không tìm thấy sản phẩm!");
                getListProducts(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("msg", "ID không hợp lệ!");
            getListProducts(request, response);
        }
    }

    // Phương thức hiển thị form xác nhận xóa sản phẩm
    private void getDeleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Product product = productService.getProductById(id);
            if (product != null) {
                request.setAttribute("productId", product.getId());
                request.setAttribute("productName", product.getName());
                request.getRequestDispatcher("/product/deleteProduct.jsp").forward(request, response);
            } else {
                request.setAttribute("msg", "Không tìm thấy sản phẩm để xóa!");
                getListProducts(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("msg", "ID không hợp lệ!");
            getListProducts(request, response);
        }
    }

    // Hiển thị danh sách sản phẩm cho trang mua hàng
    private void getListProductsForCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> productList = productService.getAllProducts();
        request.setAttribute("products", productList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/product/productListCart.jsp");
        dispatcher.forward(request, response);
    }

    // Phương thức hiển thị chi tiết sản phẩm
    private void selectProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Product product = productService.getProductById(id);
            if (product != null) {
                request.setAttribute("product", product);
                request.getRequestDispatcher("/productDetail.jsp").forward(request, response);
            } else {
                request.setAttribute("msg", "Không tìm thấy sản phẩm!");
                getListProducts(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("msg", "ID không hợp lệ!");
            getListProducts(request, response);
        }
    }

    // Phương thức xử lý tạo sản phẩm mới
    private void postCreateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("name");
            String priceStr = request.getParameter("price");
            String description = request.getParameter("description");
            String stockStr = request.getParameter("stock");

            // Validation
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên sản phẩm không được để trống!");
            }
            if (priceStr == null || priceStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Giá sản phẩm không được để trống!");
            }
            if (stockStr == null || stockStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Số lượng tồn kho không được để trống!");
            }

            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);

            if (price < 0) {
                throw new IllegalArgumentException("Giá sản phẩm không được âm!");
            }
            if (stock < 0) {
                throw new IllegalArgumentException("Số lượng tồn kho không được âm!");
            }

            Product product = new Product(0, name, price, description, stock, null);
            productService.createProduct(product);
            request.setAttribute("msg", "Tạo sản phẩm thành công!");
            getListProducts(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            getCreateProduct(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi tạo sản phẩm: " + e.getMessage());
            getCreateProduct(request, response);
        }
    }

    // Phương thức xử lý cập nhật sản phẩm
    private void postUpdateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String priceStr = request.getParameter("price");
            String description = request.getParameter("description");
            String stockStr = request.getParameter("stock");

            // Validation
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên sản phẩm không được để trống!");
            }
            if (priceStr == null || priceStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Giá sản phẩm không được để trống!");
            }
            if (stockStr == null || stockStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Số lượng tồn kho không được để trống!");
            }

            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);

            if (price < 0) {
                throw new IllegalArgumentException("Giá sản phẩm không được âm!");
            }
            if (stock < 0) {
                throw new IllegalArgumentException("Số lượng tồn kho không được âm!");
            }

            Product product = new Product(id, name, price, description, stock, null);
            boolean updated = productService.updateProduct(product);
            if (updated) {
                request.setAttribute("msg", "Cập nhật sản phẩm thành công!");
            } else {
                request.setAttribute("msg", "Cập nhật sản phẩm thất bại!");
            }
            getListProducts(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            getUpdateProduct(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi cập nhật sản phẩm: " + e.getMessage());
            getUpdateProduct(request, response);
        }
    }

    // Phương thức xử lý xóa sản phẩm
    private void postDeleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean deleted = productService.deleteProduct(id);
            if (deleted) {
                request.setAttribute("msg", "Xóa sản phẩm thành công!");
            } else {
                request.setAttribute("msg", "Xóa sản phẩm thất bại!");
            }
            getListProducts(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("msg", "ID không hợp lệ!");
            getListProducts(request, response);
        } catch (Exception e) {
            request.setAttribute("msg", "Lỗi khi xóa sản phẩm: " + e.getMessage());
            getListProducts(request, response);
        }
    }
}
