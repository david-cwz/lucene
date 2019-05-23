package graduation.cwz.service;

import graduation.cwz.entity.Message;
import graduation.cwz.model.MessageData;

import java.util.List;
import java.util.Map;

public interface MessageService {
    List<Message> getMessageList(Map<String, Object> map);
    List<Message> getMessageListByUser(Map<String, Object> map, String userName);
    List<Message> getAllMessageList();
    void addMessage(MessageData messageData);
    void delMessage(int deleteId);
    int countMessage();
    Message getMessageById(int messageId);
    List<MessageData> getMessageDataList(List<Message> messageList);
    void modifyMessage(int id, String intro, String content);
}
