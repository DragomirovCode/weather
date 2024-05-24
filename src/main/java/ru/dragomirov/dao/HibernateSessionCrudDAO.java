package ru.dragomirov.dao;

import ru.dragomirov.entities.Session;
import ru.dragomirov.utils.HibernateSessionManagerUtil;

import java.util.List;
import java.util.Optional;

public class HibernateSessionCrudDAO implements CrudDAO<Session, String> {
    @Override
    public void create(Session entity) {
        HibernateSessionManagerUtil.performTransaction(session ->
                session.save(entity));
    }

    @Override
    public List<Session> findAll() {
        return HibernateSessionManagerUtil.performSessionQuery(session ->
                session.createQuery("FROM Session", Session.class).list(),
                "Произошла ошибка при выполнении метода 'findAll'(HibernateSessionCrudDAO)");
    }

    @Override
    public Optional<Session> findById(String id) {
        return Optional.ofNullable(HibernateSessionManagerUtil.performSessionQuery(session ->
                session.get(Session.class, id),
                "Произошла ошибка при выполнении метода 'findById'(HibernateSessionCrudDAO)"));
    }

    @Override
    public void update(Session entity) {
        HibernateSessionManagerUtil.performTransaction(session ->
                session.update(entity));
    }

    @Override
    public void delete(String id) {
        HibernateSessionManagerUtil.performTransaction(session ->
                session.delete(id));
    }
}
