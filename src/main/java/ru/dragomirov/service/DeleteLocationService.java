package ru.dragomirov.service;

import ru.dragomirov.dao.HibernateLocationCrudDAO;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.entity.Location;
import ru.dragomirov.entity.Session;
import ru.dragomirov.exception.NotFoundException;
import ru.dragomirov.exception.SessionExpiredException;

import java.math.BigDecimal;

public class DeleteLocationService {
    private final HibernateLocationCrudDAO hibernateLocationCrudDAO;
    private final HibernateSessionCrudDAO hibernateSessionCrudDAO;

    public DeleteLocationService() {
        this.hibernateLocationCrudDAO = new HibernateLocationCrudDAO();
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
    }

    public void deleteLocation(String uuid, String latitudeStr, String longitudeStr) {
        Session session = validateSession(uuid);
        Location location = validateLatLon(session, latitudeStr, longitudeStr);
        hibernateLocationCrudDAO.delete(location.getId());
    }

    private Session validateSession(String uuid) {
        return hibernateSessionCrudDAO.findById(uuid).orElseThrow(() -> new SessionExpiredException("Session has expired"));
    }

    private Location validateLatLon(Session session, String latitudeStr, String longitudeStr) {
        BigDecimal latitude = new BigDecimal(latitudeStr);
        BigDecimal longitude = new BigDecimal(longitudeStr);
        return hibernateLocationCrudDAO.findByLocationLatitudeAndLongitude(latitude, longitude, session.getUserId())
                .orElseThrow(() -> new NotFoundException("Location not found"));
    }
}
