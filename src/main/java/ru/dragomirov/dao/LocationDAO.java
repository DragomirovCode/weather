package ru.dragomirov.dao;

import ru.dragomirov.entities.Location;

import java.util.List;

public interface LocationDAO extends CrudDAO<Location, Integer> {
    List<Location> findByListLocationName(String name);
    List<Location> findByListLocationUserId(int userId);
}
