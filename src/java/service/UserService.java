package service;

import java.sql.SQLException;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;
import userDAO.UserDAO;
import userDAO.IUserDAO;

public class UserService implements IUserService {

    private IUserDAO userDao;

    public UserService() {
        this.userDao = new UserDAO();
    }

    @Override
    public void createUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên người dùng không được để trống!");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email không hợp lệ!");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự!");
        }
        if (user.getDob() != null && !user.getDob().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                Date dobDate = sdf.parse(user.getDob());
                if (dobDate.after(new Date())) {
                    throw new IllegalArgumentException("Ngày sinh không được là tương lai!");
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Định dạng ngày sinh không hợp lệ, phải là yyyy-MM-dd!");
            }
        }
        try {
            userDao.insertUser(user);
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateUser(User user) {
        if (user.getId() <= 0) {
            throw new IllegalArgumentException("ID không hợp lệ!");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên người dùng không được để trống!");
        }
        if (user.getDob() != null && !user.getDob().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                Date dobDate = sdf.parse(user.getDob());
                if (dobDate.after(new Date())) {
                    throw new IllegalArgumentException("Ngày sinh không được là tương lai!");
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Định dạng ngày sinh không hợp lệ, phải là yyyy-MM-dd!");
            }
        }
        try {
            userDao.updateUser(user);
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteUser(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID không hợp lệ!");
        }
        User user = userDao.selectUser(id);
        if (user == null) {
            throw new IllegalArgumentException("User không tồn tại!");
        }
        try {
            userDao.deleteUser(id);
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public User getUserById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID không hợp lệ!");
        }
        return userDao.selectUser(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.selectAllUsers();
    }
}
