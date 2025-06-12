/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package orderDAO;

import java.sql.SQLException;
import model.Order;

/**
 *
 * @author LOQ
 */
public class OrderDAO implements IOrderDAO {

    private static final String SELECT_ORDER_BY_ID = "SELECT * FROM ORDERS WHERE id = ?";
    private static final String INSERT_ORDER = "INSERT INTO ORDERS (USER_id, total_price, status) values (?, ?, ?)";
    private static final String INSERT_ORDERDetails = "INSERT INTO ORDERDETAILS (order_id, product_id, quantity, price) values (?, ?, ?, ?)";

    @Override
    public void insertOrder(Order orderObj) throws SQLException {
        Order order = null;
    }

    @Override
    public Order getOrderById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean deleteOrder(int id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int createOrder(Order order) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void addOrderDetail(int orderId, int productId, int quantity, Double price) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
