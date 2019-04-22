package graduation.cwz.dao;

import graduation.cwz.entity.Message;

import java.util.List;

public interface MessageDao {
    List<Message> getMessageList();
    void addMessage(String intro, String content);
    void delMessage(int deleteId);

}
