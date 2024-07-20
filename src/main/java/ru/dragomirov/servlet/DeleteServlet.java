package ru.dragomirov.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.dragomirov.dao.HibernateLocationCrudDAO;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.entity.Location;
import ru.dragomirov.entity.Session;
import ru.dragomirov.exception.NotFoundException;
import ru.dragomirov.exception.SessionExpiredException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@WebServlet(name = "DeleteServlet", urlPatterns = "/delete")
public class DeleteServlet extends BaseServlet {
    private HibernateLocationCrudDAO hibernateLocationCrudDAO;
    private HibernateSessionCrudDAO hibernateSessionCrudDAO;

    @Override
    public void init() {
        this.hibernateLocationCrudDAO = new HibernateLocationCrudDAO();
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
    }
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String method = req.getMethod();
        String overrideMethod = req.getParameter("_method");
        if ("POST".equalsIgnoreCase(method) && "PATCH".equalsIgnoreCase(overrideMethod)) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String otherUuid = (String) getServletContext().getAttribute("myUuid");
        String latitudeStr = req.getParameter("latitude");
        String longitudeStr = req.getParameter("longitude");

        Optional<Location> location = hibernateLocationCrudDAO.findByLocationLatitudeAndLongitude(
                new BigDecimal(latitudeStr), new BigDecimal(longitudeStr));

        if (location.isEmpty()) {
            throw new NotFoundException("Location has expired");
        }

        hibernateLocationCrudDAO.delete(location.get().getId());

        Optional<Session> session = hibernateSessionCrudDAO.findById(otherUuid);

        if (session.isEmpty()) {
            throw new SessionExpiredException("Session has expired");
        }

        resp.sendRedirect("/?uuid=" + session.get().getId());
    }
}
