package graduation.cwz.service;

import graduation.cwz.entity.Message;
import graduation.cwz.model.MessageData;

import java.util.List;
import java.util.Map;

public interface MessageService {
    List<Message> getMessageList(Map<String, Object> map);
    List<Message> getAllMessageList();
    void addMessage(MessageData messageData);
    void delMessage(int deleteId);
    int countMessage();
    Message getMessageById(int messageId);
}
