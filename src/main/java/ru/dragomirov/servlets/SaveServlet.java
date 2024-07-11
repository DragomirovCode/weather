package ru.dragomirov.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.dragomirov.dao.HibernateLocationCrudDAO;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.entities.Location;
import ru.dragomirov.entities.Session;

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
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Пользователя с таким uuid не существует");
            return;
        }

        Optional<Session> session = hibernateSessionCrudDAO.findById(otherUuid);

        Optional<Location> locationOptional = hibernateLocationCrudDAO.findByLocationLatitudeAndLongitudeAndUserId(
                new BigDecimal(latitude), new BigDecimal(longitude), session.get().getUserId());

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
