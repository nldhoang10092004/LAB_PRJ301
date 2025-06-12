package service;

import java.util.Iterator;
import java.util.List;
import model.CartItem;
import model.Product;

public class CartService implements ICartService {

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
}
