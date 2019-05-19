package graduation.cwz.service.impl;

import graduation.cwz.dao.MessageDao;
import graduation.cwz.entity.Message;
import graduation.cwz.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageDao messageDao;

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
    public List<Message> getAllMessageList() {
        try {
            return messageDao.getAllMessageList();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void addMessage(String intro, String content) {
        try {
            messageDao.addMessage(intro, content);
            synchronized (SearchServiceImpl.newMessageList) {
                SearchServiceImpl.newMessageList.add(new Message(intro, content));
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

}
