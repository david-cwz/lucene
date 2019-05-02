package graduation.cwz.service.impl;

import graduation.cwz.dao.UserDao;
import graduation.cwz.entity.User;
import graduation.cwz.model.UserData;
import graduation.cwz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public List<User> getUserList(Map<String, Object> map) {
        try {
            return userDao.getUserList(map);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public List<User> getAllUserList() {
        try {
            return userDao.getAllUserList();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public User getUserByName(String userName) {
        try {
            List<User> list = userDao.getAllUserList();
            for (User user : list) {
                if (userName.equals(user.getUserName())) {
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    @Override
    public int countUser() {
        try {
            return getAllUserList().size();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void register(UserData userData) {
        try {
            String username = userData.getUserName();
            String password = userData.getPassword();
            String email = userData.getEmail();
            userDao.addUser(username, password,email);
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
    public void modifyPassword(String userName, String password) {
        try {
            userDao.modifyPassword(userName, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void modifyEmail(String userName, String email) {
        try {
            userDao.modifyEmail(userName, email);
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
