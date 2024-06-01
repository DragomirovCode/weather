package ru.dragomirov.dao;

import ru.dragomirov.entities.Session;
import ru.dragomirov.utils.HibernateSessionManagerUtil;

import java.util.List;
import java.util.Optional;

public class HibernateSessionCrudDAO implements SessionDAO {
    @Override
    public void create(Session entity) {
        HibernateSessionManagerUtil.performTransaction(session ->
                session.save(entity),
                "Произошла ошибка при выполнении метода 'create'(HibernateSessionCrudDAO)");
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
                session.update(entity),
                "Произошла ошибка при выполнении метода 'update'(HibernateSessionCrudDAO)");
    }

    @Override
    public void delete(String id) {
        Session sessionToDelete = findById(id).orElseThrow(() ->
                new IllegalArgumentException ("Session c id " + id + " не найдено"));
        HibernateSessionManagerUtil.performTransaction(session ->
                session.delete(sessionToDelete),
                "Произошла ошибка при выполнении метода 'delete'(HibernateSessionCrudDAO)");
    }

    @Override
    public Optional<Session> findByUserId(int userId) {
        return Optional.ofNullable((Session) HibernateSessionManagerUtil.performSessionQuery(session ->
                session.createQuery(
                        "FROM Session WHERE userId = :userId")
                        .setParameter("userId", userId)
                        .uniqueResult(),
                "Произошла ошибка при выполнении метода 'findByUserId'(HibernateSessionCrudDAO)"
        ));
    }
}
