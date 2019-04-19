package graduation.cwz.utils;

/**
 * Description:
 * @author  VipMao
 * @version  1.0
 */

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * 该工具类提供了一个属性：SessionFactory sessionFactory 并创建了sessionFactory 将它设置成static
 * 这样其他程序就可以直接通过此工具类引用 提供了二个方法: 1：通过线程创建Session-->currentSession()
 * 2：关闭Session-->closeSession() 需要在主类中手动关闭sessionFactory
 */
public class HibernateUtil {
    public static final SessionFactory sessionFactory;
    // 创建sessionFactory
    static {
        try {

            //读取hibernate.cfg.xml文件
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
            //2. 根据服务注册类创建一个元数据资源集，同时构建元数据并生成应用一般唯一的的session工厂
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // ThreadLocal可以隔离多个线程的数据共享，因此不再需要对线程同步
    public static final ThreadLocal<Session> session = new ThreadLocal<Session>();

    // 创建Session
    public static Session currentSession() throws HibernateException {
        // 通过线程对象.get()方法安全创建Session
        Session s = session.get();
        // 如果该线程还没有Session,则创建一个新的Session
        if (s == null) {
            s = sessionFactory.openSession();
            // 将获得的Session变量存储在ThreadLocal变量session里
            session.set(s);
        }
        return s;
    }

    // 关闭Session
    public static void closeSession() throws HibernateException {
        Session s = session.get();
        if (s != null)
            s.close();
        session.set(null);
    }
}