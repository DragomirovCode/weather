package ru.dragomirov.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import ru.dragomirov.exception.authentication.LoginException;
import ru.dragomirov.exception.authentication.PasswordException;
import ru.dragomirov.services.RegistrationService;
import ru.dragomirov.utils.constants.WebPageConstants;
import ru.dragomirov.utils.request.AuthenticationRequest;

import java.io.IOException;

@WebServlet(name = "RegistrationServlet", urlPatterns = "/registration")
public class RegistrationServlet extends BaseServlet {
    private RegistrationService registrationService;
    @Override
    public void init(){
        registrationService = new RegistrationService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        templateEngine.process(WebPageConstants.REGISTRATION_PAGE_X.getValue(), webContext, resp.getWriter());
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
        registrationService.processAuthenticationRequest(authenticationRequest, resp);
    }
}
