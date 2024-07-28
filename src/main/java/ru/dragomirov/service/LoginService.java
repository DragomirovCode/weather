package ru.dragomirov.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.dao.HibernateUserCrudDAO;
import ru.dragomirov.entity.Session;
import ru.dragomirov.entity.User;
import ru.dragomirov.exception.authentication.LoginException;
import ru.dragomirov.config.CookieTimeConfig;
import ru.dragomirov.util.AuthenticationRequestUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class LoginService {
    private HibernateUserCrudDAO hibernateUserCrudDAO;
    private HibernateSessionCrudDAO hibernateSessionCrudDAO;
    private final int cookieMaxAge = Integer.parseInt(CookieTimeConfig.getProperty("cookie.max_age"));

    public LoginService() {
        this.hibernateUserCrudDAO = new HibernateUserCrudDAO();
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
    }

    public void handleLogin(AuthenticationRequestUtil authenticationRequestUtil, HttpServletRequest req, HttpServletResponse resp) throws IOException, LoginException {
        Optional<User> user = findUser(authenticationRequestUtil.getLogin(), authenticationRequestUtil.getPassword());
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
        LocalDateTime futureTime = nowTime.plusSeconds(cookieMaxAge);
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
        httpSession.setAttribute("myUuid", session.getId());

        Session sessionUpdateTime = new Session(session.getId(), session.getUserId(), session.getExpiresAt());
        hibernateSessionCrudDAO.update(sessionUpdateTime);

        Cookie cookie = new Cookie("uuid", session.getId());
        cookie.setMaxAge(cookieMaxAge);
        resp.addCookie(cookie);

        resp.sendRedirect("/");
    }
}
