package ru.dragomirov.service;

import jakarta.servlet.http.HttpServletResponse;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.dao.HibernateUserCrudDAO;
import ru.dragomirov.entity.Session;
import ru.dragomirov.entity.User;
import ru.dragomirov.exception.authentication.LoginException;
import ru.dragomirov.util.constant.WebPageConstant;
import ru.dragomirov.util.AuthenticationRequest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class RegistrationService {
    private HibernateUserCrudDAO hibernateUserCrudDAO;
    private HibernateSessionCrudDAO hibernateSessionCrudDAO;

    public RegistrationService() {
        this.hibernateUserCrudDAO = new HibernateUserCrudDAO();
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
    }

    public void processAuthenticationRequest(AuthenticationRequest authenticationRequest, HttpServletResponse resp) throws Exception {
        switch (authenticationRequest.getButton()) {
            case "registration":
                handleRegistration(authenticationRequest, resp);
                break;

            case "login":
                handleLogin(resp);
                break;

            default:
                throw new IllegalArgumentException("Unknown button action: " + authenticationRequest.getButton());
        }
    }

    private void handleRegistration(AuthenticationRequest authenticationRequest, HttpServletResponse resp) throws Exception {
        Optional<User> user = hibernateUserCrudDAO.findByLogin(authenticationRequest.getLogin());

        if (user.isPresent()) {
            throw new LoginException("User with such a login already exists");
        }

        User newUser = createUser(authenticationRequest);
        createSession(newUser);
        resp.sendRedirect(WebPageConstant.LOGIN_PAGE_X.getValue());
    }

    private void handleLogin(HttpServletResponse resp) throws Exception {
        resp.sendRedirect(WebPageConstant.LOGIN_PAGE_X.getValue());
    }

    private User createUser(AuthenticationRequest authenticationRequest) {
        User newUser = new User(authenticationRequest.getLogin(), authenticationRequest.getPassword());
        hibernateUserCrudDAO.create(newUser);
        return newUser;
    }

    private void createSession(User newUser) {
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime futureTime = nowTime.plusHours(1);
        UUID sessionId = UUID.randomUUID();
        Session session = new Session(sessionId.toString(), newUser.getId(), futureTime);
        hibernateSessionCrudDAO.create(session);
    }
}
