package graduation.cwz.dao;

public interface ConfigurationDao {
    String getCurrentUser();
    void addConfig(String username);
    void updateConfig(String newUsername);
    void clear(); //清空配置表
}
