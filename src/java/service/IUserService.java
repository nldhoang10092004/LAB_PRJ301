package service;

import java.util.List;
import model.User;

public interface IUserService {
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(int id);
    User getUserById(int id);
    List<User> getAllUsers();
}