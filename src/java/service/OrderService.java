package service;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Order;
import orderDAO.IOrderDAO;
import orderDAO.OrderDAO;

public class OrderService implements IOrderService {
    private IOrderDAO orderDao;

    public OrderService() {
        this.orderDao = new OrderDAO();
    }

    @Override
    public void insertOrder(Order orderObj) {
        try {
            orderDao.insertOrder(orderObj);
        } catch (SQLException ex) {
            Logger.getLogger(OrderService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Order getOrderById(int id) {
        return orderDao.getOrderById(id);
    }

    @Override
    public boolean deleteOrder(int id) {
        try {
            return orderDao.deleteOrder(id);
        } catch (SQLException ex) {
            Logger.getLogger(OrderService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public int createOrder(Order order) {
        return orderDao.createOrder(order);
    }

    @Override
    public void addOrderDetail(int orderId, int productId, int quantity, Double price) {
        orderDao.addOrderDetail(orderId, productId, quantity, price);
    }
}
