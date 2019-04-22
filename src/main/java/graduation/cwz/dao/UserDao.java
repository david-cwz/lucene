package graduation.cwz.dao;

import graduation.cwz.entity.User;

import java.util.List;

public interface UserDao {
    List<User> getUserList();
    void addUser(String username, String password);
    void delUser(String deleteName);
    void changePassword(String username, String newPassword);
}
