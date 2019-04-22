package graduation.cwz.service.impl;

import graduation.cwz.dao.MessageDao;
import graduation.cwz.entity.Message;
import graduation.cwz.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageDao messageDao;

    @Override
    public List<Message> getMessageList() {
        try {
            return messageDao.getMessageList();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void addMessage(String intro, String content) {
        try {
            messageDao.addMessage(intro, content);
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
}
