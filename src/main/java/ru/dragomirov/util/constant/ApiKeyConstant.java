package ru.dragomirov.util.constant;

import ru.dragomirov.config.ApiKeyConfig;

public enum ApiKeyConstant {
    API_KEY_CONSTANT(ApiKeyConfig.getProperty("api_key"));
    private final String value;

    ApiKeyConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
