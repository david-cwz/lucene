package graduation.cwz.service;

import graduation.cwz.entity.User;
import graduation.cwz.model.UserData;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<User> getUserList(Map<String, Object> map);
    void register(UserData userData) throws Exception;
    void delUser(String deleteName) throws Exception;
    void modifyInfo(UserData userData) throws Exception;
    String login(UserData loginData) throws Exception; //登陆成功返回true，否则false
}
