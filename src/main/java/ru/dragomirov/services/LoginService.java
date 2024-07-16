package ru.dragomirov.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.dao.HibernateUserCrudDAO;
import ru.dragomirov.entities.Session;
import ru.dragomirov.entities.User;
import ru.dragomirov.exception.authentication.LoginException;
import ru.dragomirov.utils.request.AuthenticationRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class LoginService {
    private HibernateUserCrudDAO hibernateUserCrudDAO;
    private HibernateSessionCrudDAO hibernateSessionCrudDAO;

    public LoginService() {
        this.hibernateUserCrudDAO = new HibernateUserCrudDAO();
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
    }
    public void handleLogin(AuthenticationRequest authenticationRequest, HttpServletRequest req, HttpServletResponse resp) throws IOException, LoginException {
        Optional<User> user = findUser(authenticationRequest.getLogin(), authenticationRequest.getPassword());
        if (user.isPresent()) {
            Session session = createOrUpdateSession((long) user.get().getId());
            setupHttpSessionAndCookie(session, req, resp);
        } else {
            throw new LoginException("User with such a login or password does not exist");
        }
    }

    private Optional<User> findUser(String login, String password) {
        return hibernateUserCrudDAO.findByLoginAndPassword(login, password);
    }

    private Session createOrUpdateSession(Long userId) {
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime futureTime = nowTime.plusHours(1);
        Optional<Session> sessionId = hibernateSessionCrudDAO.findByUserId(Math.toIntExact(userId));
        Session newSession;
        if (sessionId.isEmpty()) {
            UUID sessionIdUUID = UUID.randomUUID();
            newSession = new Session(sessionIdUUID.toString(), Math.toIntExact(userId), futureTime);
            hibernateSessionCrudDAO.create(newSession);
        } else {
            newSession = new Session(sessionId.get().getId(), Math.toIntExact(userId), futureTime);
            hibernateSessionCrudDAO.update(newSession);
        }
        return newSession;
    }

    private void setupHttpSessionAndCookie(Session session, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("user", session.getUserId());

        Session sessionUpdateTime = new Session(session.getId(), session.getUserId(), session.getExpiresAt());
        hibernateSessionCrudDAO.update(sessionUpdateTime);

        Cookie cookie = new Cookie("uuid", session.getId());
        cookie.setMaxAge(3600);
        resp.addCookie(cookie);

        resp.sendRedirect("/?uuid=" + session.getId());
    }
}
