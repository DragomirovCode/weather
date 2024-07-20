package ru.dragomirov.util.constant;

public enum WebPageConstant {
    MAIN_PAGE("/main.html"),
    MAIN_PAGE_X("/main"),
    LOGIN_PAGE("/login.html"),
    LOGIN_PAGE_X("/login"),
    REGISTRATION_PAGE("/registration.html"),
    REGISTRATION_PAGE_X("/registration");
    private final String value;

    WebPageConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
