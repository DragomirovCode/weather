package ru.dragomirov.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.entities.Session;
import ru.dragomirov.exception.SessionExpiredException;
import ru.dragomirov.utils.constants.WebPageConstants;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@WebServlet(name = "CookieServlet", urlPatterns = "")
public class CookieServlet extends BaseServlet {
    private HibernateSessionCrudDAO hibernateSessionCrudDAO;

    @Override
    public void init(){
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String uuid = req.getParameter("uuid");

        if (uuid == null) {
            resp.sendRedirect(WebPageConstants.LOGIN_PAGE_X.getValue());
            return;
        }

        getServletContext().setAttribute("myUuid", uuid);

        LocalDateTime now = LocalDateTime.now();

        Optional<Session> session = hibernateSessionCrudDAO.findById(uuid);

        if (session.isEmpty()) {
            throw new SessionExpiredException("Session has expired");
        }

        if (session.get().getExpiresAt().isBefore(now)) {
            resp.sendRedirect(WebPageConstants.LOGIN_PAGE_X.getValue());
            HttpSession exitSession = req.getSession(false);
            exitSession.removeAttribute("user");
            hibernateSessionCrudDAO.delete(String.valueOf(session.get().getId()));
            return;
            }

        // Вычисляем оставшееся время
        Duration duration = Duration.between(now, session.get().getExpiresAt());
        long minutes = duration.toSeconds() ;

        System.out.println(minutes);

        // Включаем логика сервлета /my
        RequestDispatcher dispatcher = req.getRequestDispatcher("/my");
        dispatcher.forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uuid = req.getParameter("uuid");
        String button = req.getParameter("exit");

        Optional<Session> session = hibernateSessionCrudDAO.findById(uuid);

        if (session.isEmpty()) {
            throw new SessionExpiredException("Session has expired");
        }

        switch (button) {
            case "exit":
                resp.sendRedirect(WebPageConstants.LOGIN_PAGE_X.getValue());
                HttpSession exitSession = req.getSession(false);
                exitSession.removeAttribute("user");
                hibernateSessionCrudDAO.delete(String.valueOf(session.get().getId()));
                break;
        }
    }
}
