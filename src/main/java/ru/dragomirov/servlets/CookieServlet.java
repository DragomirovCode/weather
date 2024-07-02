package ru.dragomirov.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.entities.Session;
import ru.dragomirov.utils.constants.WebPageConstants;

import java.io.IOException;
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

        LocalDateTime now = LocalDateTime.now();

        Optional<Session> session = hibernateSessionCrudDAO.findById(uuid);

        if (session.get().getExpiresAt().isBefore(now)) {
            resp.sendRedirect(WebPageConstants.LOGIN_PAGE_X.getValue());
            HttpSession exitSession = req.getSession(false);
            exitSession.removeAttribute("user");
            return;
            }
        req.getRequestDispatcher(WebPageConstants.MAIN_PAGE.getValue()).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String button = req.getParameter("exit");
        switch (button) {
            case "exit":
                resp.sendRedirect(WebPageConstants.LOGIN_PAGE_X.getValue());
                HttpSession exitSession = req.getSession(false);
                exitSession.removeAttribute("user");
                break;
        }
    }
}
