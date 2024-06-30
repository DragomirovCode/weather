package ru.dragomirov.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.dragomirov.entities.Location;
import ru.dragomirov.entities.Session;
import ru.dragomirov.entities.User;

public class HibernateSessionFactoryUtil {
    private final static HikariDataSource HIKARI_DATA_SOURCE;
    private static final SessionFactory sessionFactory;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5433/weather");
        config.setDriverClassName("org.postgresql.Driver");
        config.setUsername("postgres");
        config.setPassword("Sdsafcxvre2341");
        HIKARI_DATA_SOURCE = new HikariDataSource(config);

        // Создание конфигурации Hibernate
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Session.class);
        configuration.addAnnotatedClass(Location.class);

        // Использование источника данных HikariCP вместо стандартного подключения Hibernate
        configuration.getProperties().put("hibernate.connection.datasource", HIKARI_DATA_SOURCE);
        sessionFactory = configuration.buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
