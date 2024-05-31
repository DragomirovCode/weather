package ru.dragomirov.utils.requests;

import jakarta.servlet.http.HttpServletRequest;

public class LoginRequest {
    private String login;
    private String password;
    private String button;

    public LoginRequest(HttpServletRequest req) {
        this.login = req.getParameter("login");
        this.password = req.getParameter("password");
        this.button = req.getParameter("button");
    }

    public boolean isValid() {
        return login != null && !login.isEmpty() && password != null && !password.isEmpty();
    }
}
