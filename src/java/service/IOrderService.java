package service;

import model.Order;

public interface IOrderService {
    void insertOrder(Order orderObj);
    Order getOrderById(int id);
    boolean deleteOrder(int id);
    int createOrder(Order order);
    void addOrderDetail(int orderId, int productId, int quantity, Double price);
}
