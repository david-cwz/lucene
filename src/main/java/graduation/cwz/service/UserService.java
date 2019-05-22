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
    void modifyUserInfo(String userName, String email, String currentUser);
    String login(UserData userData);
    User getUserByName(String userName);
    UserData getUserDataByName(String userName);
    List<UserData> getUserDataList(List<User> userList);
    int countUser();
    void changeToSystem(String userName); //将用户权限升为管理员
}
