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
        String id = req.getParameter("id");
        String city = req.getParameter("city");
        String latitude = req.getParameter("latitude");
        String longitude = req.getParameter("longitude");

        Location location = new Location();
        location.setId(Integer.parseInt(id));
        location.setName(city);
        location.setLatitude(BigDecimal.valueOf(Double.parseDouble(latitude)));
        location.setLongitude(BigDecimal.valueOf(Double.parseDouble(longitude)));

        String otherUuid = (String) getServletContext().getAttribute("myUuid");

        if (otherUuid == null || otherUuid.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Пользователя с таким uuid не существует");
            return;
        }

        Optional<Session> session = hibernateSessionCrudDAO.findById(otherUuid);

        location.setUserId(session.get().getUserId());

        hibernateLocationCrudDAO.create(location);

        resp.sendRedirect("/?uuid=" + session.get().getId());
    }
}
