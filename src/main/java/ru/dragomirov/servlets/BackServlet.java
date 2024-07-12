package ru.dragomirov.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.entities.Session;
import ru.dragomirov.exception.SessionExpiredException;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "BackServlet", urlPatterns = "/back")
public class BackServlet extends BaseServlet {
    private HibernateSessionCrudDAO hibernateSessionCrudDAO;

    @Override
    public void init() {
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String otherUuid = (String) getServletContext().getAttribute("myUuid");
        Optional<Session> session = hibernateSessionCrudDAO.findById(otherUuid);
        if (session.isEmpty()) {
            throw new SessionExpiredException("Session has expired");
        }
        resp.sendRedirect("/?uuid=" + session.get().getId());
    }
}
