package ru.dragomirov.utils.constants;

public enum WebPageConstants {
    LOGIN_PAGE("/login.html"),
    REGISTRATION_PAGE("/registration");
    private final String value;

    WebPageConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
