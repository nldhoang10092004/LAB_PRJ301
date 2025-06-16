package controller;

import java.io.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import model.*;
import service.*;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    private IProductService productService;
    private ICartService cartService;

    @Override
    public void init() {
        productService = new ProductService();
        cartService = new CartService();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy giỏ hàng từ session
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        request.setAttribute("cart", cart);
        request.getRequestDispatcher("cart/cart.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        String action = request.getParameter("action");
        int productId = Integer.parseInt(request.getParameter("productId"));

        switch (action) {
            case "add":
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                Product product = productService.getProductById(productId);
                if (product != null) {
                    cartService.addToCart(cart, product, quantity);
                }
                break;
            case "update":
                quantity = Integer.parseInt(request.getParameter("quantity"));
                cartService.updateCartItem(cart, productId, quantity);
                break;
            case "remove":
                cartService.removeCartItem(cart, productId);
                break;
            case "checkout":
                User currentUser = (User) session.getAttribute("user");
                if (currentUser != null) {
                    cartService.checkout(cart, currentUser.getId());
                }
                cart.clear(); // clear giỏ hàng
                session.setAttribute("cart", cart);
                request.setAttribute("message", "Đặt hàng thành công!");
                // Forward to cart page without prefixing context path to avoid
                // invalid internal path which caused filter exceptions
                request.getRequestDispatcher("/cart/cart.jsp").forward(request, response);
                return;

        }

        session.setAttribute("cart", cart);
        response.sendRedirect("cart/cart.jsp");
    }
}
