package ru.dragomirov.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.dragomirov.exception.authentication.LoginException;
import ru.dragomirov.exception.authentication.PasswordException;
import ru.dragomirov.service.LoginService;
import ru.dragomirov.util.constant.WebPageConstant;
import ru.dragomirov.util.AuthenticationRequestUtil;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends BaseServlet {
    private LoginService loginService;

    @Override
    public void init() {
        this.loginService = new LoginService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        templateEngine.process(WebPageConstant.LOGIN_PAGE_X.getValue(), webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        AuthenticationRequestUtil authenticationRequestUtil = new AuthenticationRequestUtil(req);

        if (authenticationRequestUtil.loginIsValid()) {
            throw new LoginException("Parameter login is invalid");
        }

        if (authenticationRequestUtil.passwordIsValid()) {
            throw new PasswordException("Parameter password is invalid");
        }

        switch (authenticationRequestUtil.getButton()) {
            case "login" -> loginService.handleLogin(authenticationRequestUtil, req, resp);
            case "registration" -> resp.sendRedirect(WebPageConstant.REGISTRATION_PAGE_X.getValue());
        }
    }
}
