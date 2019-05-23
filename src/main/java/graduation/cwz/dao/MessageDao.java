package graduation.cwz.dao;

import graduation.cwz.entity.Message;

import java.util.List;
import java.util.Map;

public interface MessageDao {
    List<Message> getMessageList(Map<String, Object> map);
    List<Message> getMessageListByUser(Map<String, Object> map, String userName);
    List<Message> getAllMessageList();
    void addMessage(Message message);
    void delMessage(int deleteId);
    void modifyMessage(int id, String intro, String content);
}
