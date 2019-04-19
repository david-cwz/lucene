package graduation.cwz.test;

import graduation.cwz.entity.User;
import graduation.cwz.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MyTest {

    Session session;
    Transaction tx;

    // 单元测试前走
    @Before
    public void Before() {
        session = HibernateUtil.currentSession();
        tx = session.beginTransaction();
    }

    // 单元测试后走
    @After
    public void After() {
        tx.commit();
        HibernateUtil.closeSession();
    }

    @Test
    public void TestOne() {

        User user = new User();
        user.setUername("陈文智");
        user.setPassword("123456");
        System.out.println("3333333333");
        session.save(user);

    }
}