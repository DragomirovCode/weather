package ru.dragomirov.requests;

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
}
