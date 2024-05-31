package ru.dragomirov.utils.constants;

public enum WebConstants {
    LOGIN_PAGE("/login.html"),
    USER_ATTRIBUTE("user"),
    UUID_COOKIE_NAME("uuid");
    private final String value;

    WebConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
