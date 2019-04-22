package graduation.cwz.service.impl;

import graduation.cwz.dao.ConfigurationDao;
import graduation.cwz.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {
    @Autowired
    ConfigurationDao configurationDao;

    @Override
    public String getCurrentUser() {
        try {
            return configurationDao.getCurrentUser();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void addConfig(String username) {
        try {
            configurationDao.addConfig(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void updateCurrentUser(String newUsername) {
        try {
            configurationDao.updateCurrentUser(newUsername);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void clear() {
        try {
            configurationDao.clear();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
