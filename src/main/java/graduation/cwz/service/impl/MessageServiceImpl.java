package graduation.cwz.service.impl;

import graduation.cwz.dao.MessageDao;
import graduation.cwz.entity.Message;
import graduation.cwz.entity.User;
import graduation.cwz.model.MessageData;
import graduation.cwz.service.MessageService;
import graduation.cwz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageDao messageDao;
    @Autowired
    UserService userService;

    @Override
    public List<Message> getMessageList(Map<String, Object> map) {
        try {
            return messageDao.getMessageList(map);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Message> getMessageListByUser(Map<String, Object> map, String userName) {
        try {
            return messageDao.getMessageListByUser(map, userName);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Message> getAllMessageList() {
        try {
            return messageDao.getAllMessageList();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void addMessage(MessageData messageData) {
        try {
            Message message;
            User user = userService.getUserByName(messageData.getUserName());
            if (messageData.isAnonymity()) {
                message = new Message(messageData.getIntro(), messageData.getContent(), user, null, messageData.getDate());
            } else {
                message = new Message(messageData.getIntro(), messageData.getContent(), user, user.getEmail(), messageData.getDate());
            }
            messageDao.addMessage(message);
            synchronized (SearchServiceImpl.newMessageList) {
                SearchServiceImpl.newMessageList.add(message);
                SearchServiceImpl.newMessageList.notify();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void delMessage(int deleteId) {
        try {
            messageDao.delMessage(deleteId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public int countMessage() {
        try {
            return getAllMessageList().size();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Message getMessageById(int messageId) {
        try {
            List<Message> list = getAllMessageList();
            for (Message message : list) {
                if (messageId == message.getId()) {
                    return message;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    @Override
    public List<MessageData> getMessageDataList(List<Message> messageList) {
        List<MessageData> messageDataList = new ArrayList<>();
        try {
            for (Message message : messageList) {
                MessageData messageData = new MessageData();
                messageData.setId(message.getId());
                messageData.setIntro(message.getIntro());
                messageData.setContent(message.getContent());
                messageData.setUserName(message.getUser().getUserName());
                messageData.setEmail(message.getEmail());
                messageData.setDate(message.getDate());
                messageDataList.add(messageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return messageDataList;
    }

    @Override
    public void modifyMessage(int id, String intro, String content) {
        try {
            messageDao.modifyMessage(id, intro, content);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
