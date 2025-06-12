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
    private static final String DELETE_ORDER = "DELETE FROM ORDERS WHERE id = ?";

    @Override
    public void insertOrder(Order orderObj) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_ORDER)) {
            ps.setInt(1, orderObj.getUserId());
            ps.setDouble(2, orderObj.getTotalPrice());
            ps.setString(3, orderObj.getStatus());
            ps.executeUpdate();
        }
    }

    @Override
    public Order getOrderById(int id) {
        Order order = null;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ORDER_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setTotalPrice(rs.getDouble("total_price"));
                    order.setStatus(rs.getString("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public boolean deleteOrder(int id) throws SQLException {
        boolean deleted;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_ORDER)) {
            ps.setInt(1, id);
            deleted = ps.executeUpdate() > 0;
        }
        return deleted;
    }

    @Override
    public int createOrder(Order order) {
        int generatedId = -1;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getUserId());
            ps.setDouble(2, order.getTotalPrice());
            ps.setString(3, order.getStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    @Override
    public void addOrderDetail(int orderId, int productId, int quantity, Double price) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_ORDERDetails)) {
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            if (price != null) {
                ps.setDouble(4, price);
            } else {
                ps.setNull(4, java.sql.Types.DECIMAL);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
