package ru.dragomirov.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDAO<T, ID> {
    void create(T entity);
    List<T> findAll();
    Optional<T> findById(ID id);
    void update(T entity);
    void delete(ID id);
}
