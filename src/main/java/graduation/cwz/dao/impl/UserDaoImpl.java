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


@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<User> getUserList() {
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
                    "where u.username=:deleteName");
            query.setParameter("deleteName", deleteName);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void changePassword(String username, String newPassword) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("update User u set u.password=:newPassword " +
                    "where u.username=:username");
            query.setParameter("newPassword", newPassword);
            query.setParameter("username", username);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
