package ru.dragomirov.util.constant;

import ru.dragomirov.config.ApiKeyConfig;

public enum ApiKeyConstant {
    WEATHER_API_KEY(ApiKeyConfig.getWeatherApiKey());
    private final String value;

    ApiKeyConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
