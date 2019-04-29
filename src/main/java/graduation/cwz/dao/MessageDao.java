package graduation.cwz.dao;

import graduation.cwz.entity.Message;

import java.util.List;
import java.util.Map;

public interface MessageDao {
    List<Message> getMessageList(Map<String, Object> map);
    List<Message> getAllMessageList();
    void addMessage(String intro, String content);
    void delMessage(int deleteId);
}
