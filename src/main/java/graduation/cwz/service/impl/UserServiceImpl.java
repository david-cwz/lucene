package graduation.cwz.service.impl;

import graduation.cwz.dao.UserDao;
import graduation.cwz.entity.User;
import graduation.cwz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public List<User> getUser() {
        return userDao.getUser();
    }
}
