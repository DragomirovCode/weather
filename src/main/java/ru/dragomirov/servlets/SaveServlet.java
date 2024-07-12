package ru.dragomirov.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.dragomirov.dao.HibernateLocationCrudDAO;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.entities.Location;
import ru.dragomirov.entities.Session;
import ru.dragomirov.exception.NotFoundException;
import ru.dragomirov.exception.SessionExpiredException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@WebServlet(name = "SaveServlet", urlPatterns = "/save")
public class SaveServlet extends BaseServlet {
    private HibernateLocationCrudDAO hibernateLocationCrudDAO;
    private HibernateSessionCrudDAO hibernateSessionCrudDAO;

    @Override
    public void init() {
        this.hibernateLocationCrudDAO = new HibernateLocationCrudDAO();
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String otherUuid = (String) getServletContext().getAttribute("myUuid");
        String city = req.getParameter("city");
        String latitude = req.getParameter("latitude");
        String longitude = req.getParameter("longitude");

        if (otherUuid == null || otherUuid.isEmpty()) {
            throw new NotFoundException("Uuid has expired");
        }

        Optional<Session> session = hibernateSessionCrudDAO.findById(otherUuid);

        if (session.isEmpty()) {
            throw new SessionExpiredException("Session has expired");
        }

        Optional<Location> locationOptional = hibernateLocationCrudDAO.findByLocationLatitudeAndLongitudeAndUserId(
                new BigDecimal(latitude), new BigDecimal(longitude), session.get().getUserId(), city);

        if (locationOptional.isEmpty()) {
            Location location =
                    new Location(city, new BigDecimal(latitude), new BigDecimal(longitude), session.get().getUserId());
            hibernateLocationCrudDAO.create(location);
            resp.sendRedirect("/?uuid=" + session.get().getId());
        } else {
            resp.sendRedirect("/?uuid=" + session.get().getId());
        }
    }
}
