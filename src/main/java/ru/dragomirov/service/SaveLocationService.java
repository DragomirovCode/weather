package ru.dragomirov.service;

import ru.dragomirov.dao.HibernateLocationCrudDAO;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.entity.Location;
import ru.dragomirov.entity.Session;
import ru.dragomirov.exception.SessionExpiredException;

import java.math.BigDecimal;

public class SaveLocationService {
    private HibernateLocationCrudDAO hibernateLocationCrudDAO;
    private HibernateSessionCrudDAO hibernateSessionCrudDAO;

    public SaveLocationService() {
        this.hibernateLocationCrudDAO = new HibernateLocationCrudDAO();
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
    }

    public void saveLocation(String uuid, String city, String latitudeStr, String longitudeStr) {
        Session session = validateSession(uuid);
        Location newLocation = new Location(city, new BigDecimal(latitudeStr), new BigDecimal(longitudeStr), session.getUserId());
        hibernateLocationCrudDAO.create(newLocation);
    }

    private Session validateSession(String uuid) {
        return hibernateSessionCrudDAO.findById(uuid).orElseThrow(() -> new SessionExpiredException("Session has expired"));
    }
}
