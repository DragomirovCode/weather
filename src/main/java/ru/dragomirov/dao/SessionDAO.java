package ru.dragomirov.dao;

import ru.dragomirov.entities.Session;

import java.util.Optional;

public interface SessionDAO extends CrudDAO<Session, String> {
    Optional<Session> findByUserId(int userId);
}
