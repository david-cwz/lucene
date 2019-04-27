package graduation.cwz.service.impl;

import graduation.cwz.dao.UserDao;
import graduation.cwz.entity.User;
import graduation.cwz.model.UserData;
import graduation.cwz.service.ConfigurationService;
import graduation.cwz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private ConfigurationService configurationService;

    @Override
    public List<User> getUserList(Map<String, Object> map) {
        return userDao.getUserList(map);
    }

    public List<User> getAllUserList() {
        return userDao.getAllUserList();
    }

    @Override
    public void register(UserData userData) throws Exception {
        try {
            if (userData == null || userData.getPassword() == null || userData.getUserName() == null) {
                throw new Exception("注册信息缺失");
            }
            String username = userData.getUserName();
            String password = userData.getPassword();
            userDao.addUser(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void delUser(String deleteName){
        try {
            userDao.delUser(deleteName);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void modifyInfo(UserData userData){
        try {
            userDao.modifyInfo(userData.getUserName(), userData.getPassword(), userData.getOldName());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public String login(UserData userData) {
        try {
            if (userData == null || userData.getPassword() == null || userData.getUserName() == null) {
                return "登陆信息缺失";
            }
            String username = userData.getUserName();
            String password = userData.getPassword();
            List<User> list = userDao.getAllUserList();
            for (User user : list) {
                if (user.getUserName().equals(username)) {
                    if (user.getPassword().equals(password)) {
                        configurationService.updateCurrentUser(username);
                        return "";
                    } else {
                        return "wrong password";
                    }
                }
            }
            return "wrong username";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
