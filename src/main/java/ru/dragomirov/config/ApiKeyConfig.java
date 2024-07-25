package ru.dragomirov.config;

import org.hibernate.cfg.Configuration;

public class ApiKeyConfig {
    private static final Configuration configuration = new Configuration().configure();

    public static String getProperty(String key) {
        return configuration.getProperty(key);
    }

    public static String getStringProperty(String key) {
        return String.valueOf(key);
    }
}
