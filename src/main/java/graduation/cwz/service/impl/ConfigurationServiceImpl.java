package graduation.cwz.service.impl;

import graduation.cwz.dao.ConfigurationDao;
import graduation.cwz.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;

public class ConfigurationServiceImpl implements ConfigurationService {
    @Autowired
    ConfigurationDao configurationDao;

    @Override
    public String getCurrentUser() {
        return null;
    }

    @Override
    public void addConfig(String username) {

    }

    @Override
    public void updateConfig(String newUsername) {

    }

    @Override
    public void clear() {

    }
}
