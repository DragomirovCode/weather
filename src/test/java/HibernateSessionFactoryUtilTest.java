import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import ru.dragomirov.utils.HibernateSessionFactoryUtil;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HibernateSessionFactoryUtilTest {
    @Test
    public void shouldGetSessionFactory() {
        SessionFactory sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();
        assertNotNull(sessionFactory);
    }
}
