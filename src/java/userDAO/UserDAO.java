/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package userDAO;

import dao.DBConnection;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import java.sql.SQLException;
import java.util.List;
import model.User;
import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class UserDAO implements IUserDAO {

    private static final String LOGIN = "SELECT UsersID from [Users] where name=? and password=?";
    private static final String LOGIN1 = "SELECT id, name, role FROM [Users] WHERE name = ? AND password = ?";
    private static final String INSERT_USER = "INSERT INTO Users (name, email, country, role, status, password, dob) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM Users Where id = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM Users";
    private static final String UPDATE_USER = "UPDATE Users SET name = ?, email = ?, country = ?, role = ?, status = ?, password = ?, dob = ? WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM Users WHERE id = ?";

    public static User checkLogin(String username, String password) throws SQLException {
        User us = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try (Connection con = DBConnection.getConnection()) {
            if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
                throw new SQLException("Tên đăng nhập hoặc mật khẩu không hợp lệ!");
            }
            String sql = LOGIN1;
            ptm = con.prepareStatement(sql);
            ptm.setString(1, username);
            ptm.setString(2, password);
            rs = ptm.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String role = rs.getString("role");
                us = new User(id, name, "", role);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
        }
        return us;
    }

    @Override
    public void insertUser(User user) {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(INSERT_USER);
            ps.setInt(1, user.getId());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getCountry());
            ps.setString(4, user.getRole());
            ps.setBoolean(5, user.isStatus());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getDob());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User selectUser(int id) {
        User user = null;
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(SELECT_USER_BY_ID)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String username = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                String role = rs.getString("role");
                boolean status = rs.getBoolean("status");
                String password = rs.getString("password");
                String dob = rs.getString("dob");
                user = new User(id, username, email, country, role, status, password, dob);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> selectAllUsers() {
        List<User> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(SELECT_ALL_USERS);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                String role = rs.getString("role");
                boolean status = rs.getBoolean("status");
                String password = rs.getString("password");
                String dob = rs.getString("dob");
                list.add(new User(id, username, email, country, role, status, password, dob));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean deleteUser(int id) {
        boolean delete = false;
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(DELETE_USER);
            ps.setInt(1, id);
            delete = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return delete;
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        boolean updated;
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(UPDATE_USER)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getCountry());
            ps.setString(4, user.getRole());
            ps.setBoolean(5, user.isStatus());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getDob());
            ps.setInt(8, user.getId());
            updated = ps.executeUpdate() > 0;
        }
        return updated;
    }

    public static void main(String[] args) {
        UserDAO c = new UserDAO();
        try {
            User u = checkLogin("Chi Pheo", "abc@123");
            System.out.println(u.toString());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
