package ru.dragomirov.utils;

import org.hibernate.cfg.Configuration;

public class ConfigUtil {
    private static final Configuration configuration = new Configuration().configure();

    public static String getProperty(String key) {
        return configuration.getProperty(key);
    }

    public static int getIntProperty(String key) {
        return Integer.parseInt(getProperty(key));
    }
}