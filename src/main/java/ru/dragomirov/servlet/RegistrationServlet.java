package ru.dragomirov.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import ru.dragomirov.exception.authentication.LoginException;
import ru.dragomirov.exception.authentication.PasswordException;
import ru.dragomirov.service.RegistrationService;
import ru.dragomirov.util.constant.WebPageConstant;
import ru.dragomirov.util.AuthenticationRequestUtil;

import java.io.IOException;

@WebServlet(name = "RegistrationServlet", urlPatterns = "/registration")
public class RegistrationServlet extends BaseServlet {
    private RegistrationService registrationService;

    @Override
    public void init() {
        registrationService = new RegistrationService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        templateEngine.process(WebPageConstant.REGISTRATION_PAGE_X.getValue(), webContext, resp.getWriter());
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        AuthenticationRequestUtil authenticationRequestUtil = new AuthenticationRequestUtil(req);

        if (authenticationRequestUtil.loginIsValid()) {
            throw new LoginException("Parameter login is invalid");
        }

        if (authenticationRequestUtil.passwordIsValid()) {
            throw new PasswordException("Parameter password is invalid");
        }
        registrationService.processAuthenticationRequest(authenticationRequestUtil, resp);
    }
}
