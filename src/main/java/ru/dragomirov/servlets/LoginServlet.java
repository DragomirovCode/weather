package ru.dragomirov.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lombok.SneakyThrows;
import ru.dragomirov.exception.authentication.LoginException;
import ru.dragomirov.exception.authentication.PasswordException;
import ru.dragomirov.services.LoginService;
import ru.dragomirov.utils.constants.WebPageConstants;
import ru.dragomirov.utils.request.AuthenticationRequest;

import java.io.IOException;

@WebServlet(name = "LoginServlet" , urlPatterns = "/login")
public class LoginServlet extends BaseServlet {
    private LoginService loginService;

    @Override
    public void init() {
        this.loginService = new LoginService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        templateEngine.process(WebPageConstants.LOGIN_PAGE_X.getValue(), webContext, resp.getWriter());
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(req);

        if (authenticationRequest.loginIsValid()) {
            throw new LoginException("Parameter login is invalid");
        }

        if (authenticationRequest.passwordIsValid()) {
            throw new PasswordException("Parameter password is invalid");
        }

        switch (authenticationRequest.getButton()) {
            case "login":
                loginService.handleLogin(authenticationRequest, req, resp);
                break;
            case "registration":
                resp.sendRedirect(WebPageConstants.REGISTRATION_PAGE_X.getValue());
                break;
        }
    }
}
