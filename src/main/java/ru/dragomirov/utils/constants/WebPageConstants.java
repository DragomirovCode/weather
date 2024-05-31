package ru.dragomirov.utils.constants;

public enum WebPageConstants {
    LOGIN_PAGE("/login.html"),
    LOGIN_PAGE_X("/login"),
    REGISTRATION_PAGE("/registration.html"),
    REGISTRATION_PAGE_X("/registration");
    private final String value;

    WebPageConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}