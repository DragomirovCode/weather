package ru.dragomirov.utils;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Utils {
    public String buildCityWeatherApiUrl(String city, String key) {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        return "https://api.openweathermap.org/geo/1.0/direct?q=" + encodedCity + "&limit=5&appid=" + key;
    }

    public String buildLatLonCityWeatherApiUrl(BigDecimal lat, BigDecimal lon, String key) {
        return "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + key;
    }
}
