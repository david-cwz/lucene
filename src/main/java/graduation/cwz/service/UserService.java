package graduation.cwz.service;

import graduation.cwz.entity.User;

import java.util.List;

public interface UserService {
    List<User> getUserList();
    void addUser(String username, String password);
    void deleteUser(String username);
    void changePassword(String username, String newPassword);
}
