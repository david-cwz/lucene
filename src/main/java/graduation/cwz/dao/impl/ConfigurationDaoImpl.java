package graduation.cwz.dao.impl;

import graduation.cwz.dao.ConfigurationDao;
import graduation.cwz.entity.Configuration;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConfigurationDaoImpl implements ConfigurationDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public String getCurrentUser() {
        List<Configuration> list = null;
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from Configuration c ");
            list = query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        if (list == null) {
            return null;
        } else {
            return list.get(0).getCurrentUser();
        }

    }

    @Override
    public void addConfig(String username) {
        try (Session session = sessionFactory.openSession()) {
            Configuration configuration = new Configuration(username);
            Transaction transaction = session.beginTransaction();
            session.save(configuration);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void updateCurrentUser(String newUsername) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("update Configuration c set c.currentUser=:newUsername ");
            query.setParameter("newUsername", newUsername);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void clear() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("delete from Configuration");
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
