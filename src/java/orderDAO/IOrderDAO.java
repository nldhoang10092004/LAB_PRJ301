/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package orderDAO;

import model.Order;
        
import java.sql.SQLException;
/**
 *
 * @author LOQ
 */
public interface IOrderDAO {
    public void insertOrder(Order orderObj) throws SQLException;
    public Order getOrderById(int id);
    public boolean deleteOrder(int id) throws SQLException;
    public int createOrder(Order order);
    public void addOrderDetail(int orderId, int productId, int quantity, Double price);
}
