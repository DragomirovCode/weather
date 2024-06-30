package ru.dragomirov.utils;

public class Utils {
    public String buildUniqueCityWeatherApiUrl(String city, String key) {
        return "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key;
    }
}
