package ru.dragomirov.config;

public class ApiKeyConfig {
    public static String getWeatherApiKey() {
        return System.getenv("WEATHER_API_KEY");
    }
}
