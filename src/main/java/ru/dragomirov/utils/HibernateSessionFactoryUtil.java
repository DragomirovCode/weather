package ru.dragomirov.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.dragomirov.exception.DatabaseOperationException;

public class HibernateSessionFactoryUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration().configure();
            sessionFactory = configuration.buildSessionFactory();
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("an error occurred in the database");
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
