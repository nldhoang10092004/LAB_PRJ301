package service;

import java.util.Iterator;
import java.util.List;
import model.CartItem;
import model.Order;
import model.Product;
import orderDAO.OrderDAO;

public class CartService implements ICartService {

    // 🔴 Sửa: Khai báo biến instance cho OrderDAO
    private OrderDAO orderDAO;

    public CartService() {
        // ✅ Sửa: Lưu OrderDAO vào biến instance
        orderDAO = new OrderDAO();
    }

    @Override
    public void addToCart(List<CartItem> cart, Product product, int quantity) {
        if (cart == null || product == null || quantity <= 0) {
            return;
        }
        for (CartItem item : cart) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        cart.add(new CartItem(product, quantity));
    }

    @Override
    public void updateCartItem(List<CartItem> cart, int productId, int quantity) {
        if (cart == null) {
            return;
        }
        Iterator<CartItem> iterator = cart.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProduct().getId() == productId) {
                if (quantity <= 0) {
                    iterator.remove();
                } else {
                    item.setQuantity(quantity);
                }
                return;
            }
        }
    }

    @Override
    public void removeCartItem(List<CartItem> cart, int productId) {
        if (cart == null) {
            return;
        }
        cart.removeIf(item -> item.getProduct().getId() == productId);
    }

    @Override
    public void checkout(List<CartItem> cart, int userId) {
        if (cart == null || cart.isEmpty()) {
            return;
        }

        double total = cart.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(total);
        order.setStatus("PENDING");

        // ✅ Sử dụng biến orderDAO đã khởi tạo
        int orderId = orderDAO.createOrder(order);

        for (CartItem item : cart) {
            orderDAO.addOrderDetail(orderId, item.getProduct().getId(), item.getQuantity(), item.getProduct().getPrice());
        }
    }
}
