package ru.dragomirov.dao;

import ru.dragomirov.entities.User;

import java.util.Optional;

public interface UserDAO extends CrudDAO<User, Integer> {
    Optional<User> findByLogin(String login);
}
