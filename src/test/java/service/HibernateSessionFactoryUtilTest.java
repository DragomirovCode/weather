package service;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import ru.dragomirov.util.HibernateSessionFactoryUtil;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HibernateSessionFactoryUtilTest {
    @Test
    public void getSessionFactory_shouldConnect_inDatabase() {
        SessionFactory sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();
        assertNotNull(sessionFactory);
    }
}
