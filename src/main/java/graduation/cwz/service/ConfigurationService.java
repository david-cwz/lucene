package graduation.cwz.service;

public interface ConfigurationService {
    String getCurrentUser();
    void addConfig(String username);
    void updateConfig(String newUsername);
    void clear(); //清空配置表
}
