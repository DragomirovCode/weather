package ru.dragomirov.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.dragomirov.entities.Locations;
import ru.dragomirov.entities.Users;

public class HibernateSessionFactoryUtil {
    private final static HikariDataSource HIKARI_DATA_SOURCE;
    private static final SessionFactory sessionFactory;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite::resource:db/database.db");
        config.setDriverClassName("org.sqlite.JDBC");
        config.setUsername("");
        config.setPassword("");
        HIKARI_DATA_SOURCE = new HikariDataSource(config);

        // Создание конфигурации Hibernate
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLiteDialect");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.addAnnotatedClass(Users.class);
        configuration.addAnnotatedClass(Session.class);
        configuration.addAnnotatedClass(Locations.class);

        // Использование источника данных HikariCP вместо стандартного подключения Hibernate
        configuration.getProperties().put("hibernate.connection.datasource", HIKARI_DATA_SOURCE);
        sessionFactory = configuration.buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
