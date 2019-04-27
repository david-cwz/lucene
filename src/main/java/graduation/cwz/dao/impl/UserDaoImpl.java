package graduation.cwz.dao.impl;

import graduation.cwz.dao.UserDao;
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
public class UserDaoImpl implements UserDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<User> getUserList(Map<String, Object> map) {
        List<User> list;
        int start = (Integer) map.get("start");
        int size = (Integer) map.get("size");
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from User");
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
    public List<User> getAllUserList() {
        List<User> list = null;
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from User");
            list = query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return list;
    }

    @Override
    public void addUser(String username, String password) {
        try (Session session = sessionFactory.openSession()) {
            User user = new User(username, password);
            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public void delUser(String deleteName) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("delete from User u " +
                    "where u.userName=:deleteName");
            query.setParameter("deleteName", deleteName);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void modifyInfo(String userName, String password, String oldName) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("update User u set u.userName=:userName, u.password=:password " +
                    "where u.userName=:oldName");
            query.setParameter("userName", userName);
            query.setParameter("password", password);
            query.setParameter("oldName", oldName);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
