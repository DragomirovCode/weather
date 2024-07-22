package ru.dragomirov.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.entity.Session;
import ru.dragomirov.exception.SessionExpiredException;
import ru.dragomirov.util.constant.WebPageConstant;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class CookieService {
    private HibernateSessionCrudDAO hibernateSessionCrudDAO;

    public CookieService() {
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
    }

    private LocalDateTime nowTime() {
        return LocalDateTime.now();
    }

    public void validateAndHandleSession(String uuid, HttpServletRequest req, HttpServletResponse resp) {
        Optional<Session> session = validateSession(uuid);
        if (session.isPresent()) {
            LocalDateTime now = nowTime();
            handleSessionExpiration(session.get(), now, req, resp);
            printRemainingTime(Duration.between(now, session.get().getExpiresAt()));
        } else {
            throw new SessionExpiredException("Session has expired");
        }
    }

    private Optional<Session> validateSession(String uuid) {
        return hibernateSessionCrudDAO.findById(uuid);
    }

    private void handleSessionExpiration(Session session, LocalDateTime now, HttpServletRequest req, HttpServletResponse resp) {
        try {
            if (session.getExpiresAt().isBefore(now)) {
                resp.sendRedirect(WebPageConstant.LOGIN_PAGE_X.getValue());
                HttpSession exitSession = req.getSession(false);
                if (exitSession != null) {
                    exitSession.removeAttribute("user");
                }
                hibernateSessionCrudDAO.delete(String.valueOf(session.getId()));
            }
        } catch (Exception e) {
            throw new SessionExpiredException("Failed to handle session expiration");
        }
    }

    private void printRemainingTime(Duration duration) {
        long minutes = duration.toSeconds();
        System.out.println(minutes);
    }
}
