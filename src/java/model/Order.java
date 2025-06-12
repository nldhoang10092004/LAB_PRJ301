package model;

public class Order {

    private int id;
    private int userId;
    private double totalPrice;
    private String status;

    public Order() {
    }

    public Order(int id, int userId, double totalPrice, String status) {
        this.id = id;
        this.userId = userId;

        this.totalPrice = totalPrice;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", userId=" + userId + ", totalPrice=" + totalPrice + ", status=" + status + '}';
    }
//    // Phương thức để ánh xạ kết quả từ ResultSet thành đối tượng Order
//
//    public static Order fromResultSet(ResultSet rs) throws SQLException {
//        Order order = new Order();
//        order.setId(rs.getInt("id"));
//        order.setUserId(rs.getInt("user_id"));
//        order.setTotalPrice(rs.getDouble("total_price"));
//        order.setStatus(rs.getString("status"));
//        return order;
//    }
}
