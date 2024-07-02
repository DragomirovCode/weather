package ru.dragomirov.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Utils {
    public String buildUniqueCityWeatherApiUrl(String city, String key) {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        return "https://api.openweathermap.org/data/2.5/weather?q=" + encodedCity + "&appid=" + key;
    }
}
