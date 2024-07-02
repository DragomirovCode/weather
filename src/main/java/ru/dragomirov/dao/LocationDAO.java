package ru.dragomirov.dao;

import ru.dragomirov.entities.Location;

import java.util.Optional;

public interface LocationDAO extends CrudDAO<Location, Integer> {
    Optional<Location> findByLocationName(String name);
}
