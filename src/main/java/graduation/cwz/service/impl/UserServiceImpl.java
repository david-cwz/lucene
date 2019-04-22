package graduation.cwz.service.impl;

import graduation.cwz.dao.UserDao;
import graduation.cwz.entity.User;
import graduation.cwz.model.UserData;
import graduation.cwz.service.ConfigurationService;
import graduation.cwz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private ConfigurationService configurationService;

    @Override
    public List<User> getUserList() {
        return userDao.getUserList();
    }

    @Override
    public void register(UserData userData) throws Exception {
        try {
            if (userData == null || userData.getPassword() == null || userData.getUsername() == null) {
                throw new Exception("注册信息缺失");
            }
            String username = userData.getUsername();
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
    public void changePassword(String username, String newPassword){
        try {
            userDao.changePassword(username, newPassword);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public String login(UserData userData) throws Exception {
        try {
            if (userData == null || userData.getPassword() == null || userData.getUsername() == null) {
                throw new Exception("登陆信息缺失");
            }
            String username = userData.getUsername();
            String password = userData.getPassword();
            List<User> list = userDao.getUserList();
            for (User user : list) {
                if (user.getUername().equals(username)) {
                    if (user.getPassword().equals(password)) {
                        configurationService.updateCurrentUser(username);
                        return "success";
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
