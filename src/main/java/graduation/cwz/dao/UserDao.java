package graduation.cwz.dao;

import graduation.cwz.entity.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    List<User> getUserList(Map<String, Object> map);
    List<User> getAllUserList();
    void addUser(String userName, String password, String email, String role);
    void delUser(String deleteName);
    void modifyPassword(String userName, String password);
    void modifyEmail(String userName, String email);
    void changeToSystem(String userName); //将用户权限升为管理员
}
