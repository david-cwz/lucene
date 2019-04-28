package graduation.cwz.dao.impl;

import graduation.cwz.dao.SearchHistoryDao;
import graduation.cwz.entity.SearchHistory;
import graduation.cwz.entity.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SearchHistoryDaoImpl implements SearchHistoryDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<SearchHistory> getRecordList(Map<String, Object> map) {
        List<SearchHistory> list;
        int start = (Integer) map.get("start");
        int size = (Integer) map.get("size");
        String userName = (String)map.get("userName");
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from SearchHistory sh " +
                    "where sh.user.userName=:userName");
            query.setParameter("userName", userName);
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
    public void addRecord(String record, User user, String date) {
        try (Session session = sessionFactory.openSession()) {
            SearchHistory searchHistory = new SearchHistory(record, user, date);
            Transaction transaction = session.beginTransaction();
            session.save(searchHistory);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void delRecord(int deleteId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("delete from SearchHistory sh " +
                    "where sh.id=:deleteId");
            query.setParameter("deleteId", deleteId);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
