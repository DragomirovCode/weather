package ru.dragomirov.dao;

import ru.dragomirov.entities.Location;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface LocationDAO extends CrudDAO<Location, Integer> {
    Optional<Location> findByLocationName(String name);
    List<Location> findByListLocationId(int id);
}
