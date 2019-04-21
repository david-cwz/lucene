package graduation.cwz.dao.impl;

import graduation.cwz.dao.SearchHistoryDao;
import graduation.cwz.entity.SearchHistory;
import graduation.cwz.entity.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SearchHistoryDaoImpl implements SearchHistoryDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<SearchHistory> getRecordList() {
        List<SearchHistory> list = null;
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from SearchHistory");
            list = query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return list;
    }

    @Override
    public List<SearchHistory> getRecordListByUser(String username) {
        List<SearchHistory> list = null;
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from SearchHistory sh " +
                    "where sh.user.username=:username");
            query.setParameter("username", username);
            list = query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return list;
    }

    @Override
    public void addRecord(String record, String username) {
        try (Session session = sessionFactory.openSession()) {
            SearchHistory searchHistory = new SearchHistory(record, new User(username));
            Transaction transaction = session.beginTransaction();
            session.save(searchHistory);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void delRecord(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("delete from SearchHistory sh " +
                    "where sh.id=:id");
            query.setParameter("id", id);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
