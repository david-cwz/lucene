package graduation.cwz.dao.impl;

import graduation.cwz.dao.MessageDao;
import graduation.cwz.entity.Message;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MessageDaoImpl implements MessageDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Message> getMessageList(Map<String, Object> map) {
        List<Message> list;
        int start = (Integer) map.get("start");
        int size = (Integer) map.get("size");
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from Message m ");
            query.setFirstResult(start);
            query.setMaxResults(size);
            list = query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return list;
    }

    @Override
    public List<Message> getAllMessageList() {
        List<Message> list;
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from Message m ");
            list = query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return list;
    }

    @Override
    public void addMessage(String intro, String content) {
        try (Session session = sessionFactory.openSession()) {
            Message message = new Message(intro, content);
            Transaction transaction = session.beginTransaction();
            session.save(message);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void delMessage(int deleteId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("delete from Message m " +
                    "where m.id=:deleteId");
            query.setParameter("deleteId", deleteId);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
