package service;

import java.util.List;
import model.CartItem;
import model.Product;

public interface ICartService {
    void addToCart(List<CartItem> cart, Product product, int quantity);
    void updateCartItem(List<CartItem> cart, int productId, int quantity);
    void removeCartItem(List<CartItem> cart, int productId);
}
