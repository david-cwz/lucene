package graduation.cwz.service;

import graduation.cwz.entity.Message;

import java.util.List;

public interface MessageService {
    List<Message> getMessageList();
    void addMessage(String intro, String content);
    void delMessage(int deletId);
    String search(String keyWord);
}
