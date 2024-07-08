package ru.dragomirov.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.dragomirov.dao.HibernateLocationCrudDAO;
import ru.dragomirov.entities.Location;

import java.math.BigDecimal;

@WebServlet(name = "SaveServlet", urlPatterns = "/save")
public class SaveServlet extends BaseServlet {
    private HibernateLocationCrudDAO hibernateLocationCrudDAO;

    @Override
    public void init() {
        this.hibernateLocationCrudDAO = new HibernateLocationCrudDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");
        String city = req.getParameter("city");
        String latitude = req.getParameter("latitude");
        String longitude = req.getParameter("longitude");

        Location location = new Location();
        location.setId(Integer.parseInt(id));
        location.setName(city);
        location.setLatitude(BigDecimal.valueOf(Double.parseDouble(latitude)));
        location.setLongitude(BigDecimal.valueOf(Double.parseDouble(longitude)));
        location.setUserId(1);

        hibernateLocationCrudDAO.create(location);
    }
}
