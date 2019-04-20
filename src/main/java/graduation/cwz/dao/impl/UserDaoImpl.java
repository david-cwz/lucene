package graduation.cwz.dao.impl;

import graduation.cwz.dao.UserDao;
import graduation.cwz.entity.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<User> getUser() {
        Session session = sessionFactory.openSession();
        String hql = "from User";
        Query query = session.createQuery(hql);
        List<User> list=query.list();
        session.close();
        return list;
    }
}
