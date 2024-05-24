package ru.dragomirov.dao;

import ru.dragomirov.entities.User;
import ru.dragomirov.utils.HibernateSessionManagerUtil;

import java.util.List;
import java.util.Optional;

public class HibernateUserCrudDAO implements CrudDAO<User, Integer> {
    @Override
    public void create(User entity) {
        HibernateSessionManagerUtil.performTransaction(session ->
                session.save(entity));
    }

    @Override
    public List<User> findAll() {
        return HibernateSessionManagerUtil.performSessionQuery(session ->
                session.createQuery("FROM User", User.class).list(),
                "Произошла ошибка при выполнении метода 'findAll'(HibernateUserCrudDAO)");
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(HibernateSessionManagerUtil.performSessionQuery(session ->
                session.get(User.class, id),
                "Произошла ошибка при выполнении метода 'findById'(HibernateUserCrudDAO)"));
    }

    @Override
    public void update(User entity) {
        HibernateSessionManagerUtil.performTransaction(session ->
                session.update(entity));
    }

    @Override
    public void delete(Integer id) {
        HibernateSessionManagerUtil.performTransaction(session ->
                session.delete(id));
    }
}
