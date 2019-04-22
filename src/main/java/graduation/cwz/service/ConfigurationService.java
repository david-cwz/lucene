package graduation.cwz.service;

public interface ConfigurationService {
    String getCurrentUser();
    void addConfig(String username);
    void updateCurrentUser(String newUsername);
    void clear(); //清空配置表
}
