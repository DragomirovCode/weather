package ru.dragomirov.util.constant;

public enum WebPageConstant {
    MAIN_PAGE_X("/"),
    LOGIN_PAGE_X("/login"),
    REGISTRATION_PAGE_X("/registration");
    private final String value;

    WebPageConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
