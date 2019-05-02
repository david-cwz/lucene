package graduation.cwz.service;

import graduation.cwz.entity.User;
import graduation.cwz.model.UserData;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<User> getUserList(Map<String, Object> map);
    void register(UserData userData);
    void delUser(String deleteName);
    void modifyPassword(String userName, String password);
    void modifyEmail(String userName, String email);
    String login(UserData userData);
    User getUserByName(String userName);
    int countUser();
}
