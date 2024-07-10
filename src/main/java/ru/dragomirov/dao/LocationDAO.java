package ru.dragomirov.dao;

import ru.dragomirov.entities.Location;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface LocationDAO extends CrudDAO<Location, Integer> {
    List<Location> findByListLocationName(String name);
    List<Location> findByListLocationUserId(int userId);
    Optional<Location> findByLocationName(String name);
    Optional<Location> findByLocationLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude);
    Optional<Location> findByLocationLatitudeAndLongitudeAndUserId(BigDecimal latitude, BigDecimal longitude, int userId);
}
