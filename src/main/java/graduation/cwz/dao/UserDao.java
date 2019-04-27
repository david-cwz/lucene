package graduation.cwz.dao;

import graduation.cwz.entity.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    List<User> getUserList(Map<String, Object> map);
    List<User> getAllUserList();
    void addUser(String username, String password);
    void delUser(String deleteName);
    void modifyInfo(String username, String password, String oldName);
}
