package graduation.cwz.service;

import graduation.cwz.entity.User;
import graduation.cwz.model.UserData;

import java.util.List;

public interface UserService {
    List<User> getUserList();
    void register(UserData userData) throws Exception;
    void delUser(String deleteName) throws Exception;
    void changePassword(String username, String newPassword) throws Exception;
    String login(UserData loginData) throws Exception; //登陆成功返回true，否则false
}
