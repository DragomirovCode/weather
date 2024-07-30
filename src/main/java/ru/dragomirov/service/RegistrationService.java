package ru.dragomirov.service;

import jakarta.servlet.http.HttpServletResponse;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.dao.HibernateUserCrudDAO;
import ru.dragomirov.entity.Session;
import ru.dragomirov.entity.User;
import ru.dragomirov.util.constant.WebPageConstant;
import ru.dragomirov.util.AuthenticationRequestUtil;

import java.time.LocalDateTime;
import java.util.UUID;

public class RegistrationService {
    private HibernateUserCrudDAO hibernateUserCrudDAO;
    private HibernateSessionCrudDAO hibernateSessionCrudDAO;

    public RegistrationService() {
        this.hibernateUserCrudDAO = new HibernateUserCrudDAO();
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
    }

    public void processAuthenticationRequest(AuthenticationRequestUtil authenticationRequestUtil, HttpServletResponse resp) throws Exception {
        switch (authenticationRequestUtil.getButton()) {
            case "registration":
                handleRegistration(authenticationRequestUtil, resp);
                break;

            case "login":
                handleLogin(resp);
                break;

            default:
                throw new IllegalArgumentException("Unknown button action: " + authenticationRequestUtil.getButton());
        }
    }

    private void handleRegistration(AuthenticationRequestUtil authenticationRequestUtil, HttpServletResponse resp) throws Exception {
        User newUser = createUser(authenticationRequestUtil);
        createSession(newUser);
        resp.sendRedirect(WebPageConstant.LOGIN_PAGE_X.getValue());
    }

    private void handleLogin(HttpServletResponse resp) throws Exception {
        resp.sendRedirect(WebPageConstant.LOGIN_PAGE_X.getValue());
    }

    private User createUser(AuthenticationRequestUtil authenticationRequestUtil) {
        User newUser = new User(authenticationRequestUtil.getLogin(), authenticationRequestUtil.getPassword());
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
