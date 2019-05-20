package graduation.cwz.dao.impl;

import graduation.cwz.dao.SearchDao;
import graduation.cwz.entity.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SearchDaoImpl implements SearchDao {
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
                    "where sh.user.userName=:userName order by date desc");
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
    public List<SearchHistory> getAllRecordList() {
        List<SearchHistory> list;
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from SearchHistory ");
            list = query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return list;
    }

    @Override
    public List<SearchHistory> getAllRecordListByName(String userName) {
        List<SearchHistory> list;
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from SearchHistory sh " +
                    "where sh.user.userName=:userName");
            query.setParameter("userName", userName);
            list = query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return list;
    }

    @Override
    public List<SearchHistory> getPreEmbeddedRecords() {
        List<SearchHistory> list;
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from SearchHistory sh " +
                    "where sh.isPreEmbedded=:isPreEmbedded");
            query.setParameter("isPreEmbedded", true);
            list = query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return list;
    }

    @Override
    public void addRecord(String record, User user, String date, String searchTarget) {
        try (Session session = sessionFactory.openSession()) {
            SearchHistory searchHistory = new SearchHistory(record, user, date, searchTarget);
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

    @Override
    public void updatePreEmbeddedStatus(int id, boolean isPreEmbedded) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("update SearchHistory sh set sh.isPreEmbedded=:isPreEmbedded " +
                    "where sh.id=:id");
            query.setParameter("isPreEmbedded", isPreEmbedded);
            query.setParameter("id", id);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void updateHaveNewResultStatus(int id, String haveNewResult) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("update SearchHistory sh set sh.haveNewResult=:haveNewResult " +
                    "where sh.id=:id");
            query.setParameter("haveNewResult", haveNewResult);
            query.setParameter("id", id);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<SearchResult> getSearchResultList(Map<String, Object> map) {
        List<SearchResult> list;
        int start = (Integer) map.get("start");
        int size = (Integer) map.get("size");
        int recordId = (int)map.get("recordId");
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from SearchResult sr " +
                    "where sr.record.id=:recordId");
            query.setParameter("recordId", recordId);
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
    public void addSearchResult(SearchHistory record, Message message, String intro, String content) {
        try (Session session = sessionFactory.openSession()) {
            SearchResult searchResult = new SearchResult(record, message, intro, content);
            Transaction transaction = session.beginTransaction();
            session.save(searchResult);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<OnlineSearchResult> getOnlineSearchResultList(Map<String, Object> map) {
        List<OnlineSearchResult> list;
        int start = (Integer) map.get("start");
        int size = (Integer) map.get("size");
        int recordId = (int)map.get("recordId");
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from OnlineSearchResult osr " +
                    "where osr.record.id=:recordId");
            query.setParameter("recordId", recordId);
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
    public void addOnlineSearchResult(SearchHistory record, String content, String url) {
        try (Session session = sessionFactory.openSession()) {
            OnlineSearchResult onlineSearchResult = new OnlineSearchResult(record, content, url);
            Transaction transaction = session.beginTransaction();
            session.save(onlineSearchResult);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Url> getUrlList(Map<String, Object> map) {
        List<Url> list;
        int start = (Integer) map.get("start");
        int size = (Integer) map.get("size");
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from Url");
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
    public void addUrl(String name, String url) {
        try (Session session = sessionFactory.openSession()) {
            Url URL = new Url(name, url);
            Transaction transaction = session.beginTransaction();
            session.save(URL);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void modifyUrl(int urlId, String name, String url) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("update Url u set u.name=:name,u.url=:url " +
                    "where u.id=:urlId");
            query.setParameter("name", name);
            query.setParameter("url", url);
            query.setParameter("urlId", urlId);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void delUrl(int urlId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("delete from Url u " +
                    "where u.id=:urlId");
            query.setParameter("urlId", urlId);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
