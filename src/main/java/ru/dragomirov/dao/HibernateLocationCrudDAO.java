package ru.dragomirov.dao;

import ru.dragomirov.entities.Location;
import ru.dragomirov.utils.HibernateSessionManagerUtil;

import java.util.List;
import java.util.Optional;

public class HibernateLocationCrudDAO implements LocationDAO{
    @Override
    public void create(Location entity) {
        HibernateSessionManagerUtil.performTransaction(session ->
                session.save(entity),
                "Произошла ошибка при выполнении метода 'create'(HibernateLocationCrudDAO)");
    }

    @Override
    public List<Location> findAll() {
        return HibernateSessionManagerUtil.performSessionQuery(session ->
                session.createQuery("FROM Location", Location.class).list(),
                "Произошла ошибка при выполнении метода 'findAll'(HibernateLocationCrudDAO)");
    }

    @Override
    public Optional<Location> findById(Integer id) {
        return Optional.ofNullable(HibernateSessionManagerUtil.performSessionQuery(session ->
                session.get(Location.class, id),
                "Произошла ошибка при выполнении метода 'findById'(HibernateLocationCrudDAO)"));
    }

    @Override
    public void update(Location entity) {
        HibernateSessionManagerUtil.performTransaction(session ->
                session.update(entity),
                "Произошла ошибка при выполнении метода 'update'(HibernateLocationCrudDAO)");
    }

    @Override
    public void delete(Integer id) {
        Location locationToDelete = findById(id).orElseThrow(() ->
                new IllegalArgumentException ("Location c id " + id + " не найдено"));
        HibernateSessionManagerUtil.performTransaction(session ->
                session.delete(locationToDelete),
                "Произошла ошибка при выполнении метода 'delete'(HibernateLocationCrudDAO)");
    }

    @Override
    public List<Location> findByListLocationName(String name) {
        return HibernateSessionManagerUtil.performSessionQuery(session ->
                session.createQuery("FROM Location WHERE name = :name")
                        .setParameter("name", name)
                        .list(),
                "Произошла ошибка при выполнении метода 'findByListLocationName'(HibernateLocationCrudDAO)"
        );
    }

    @Override
    public List<Location> findByListLocationUserId(int userId) {
        return HibernateSessionManagerUtil.performSessionQuery(session ->
                session.createQuery("FROM Location  WHERE userId = :userId")
                        .setParameter("userId", userId)
                        .list(),
                "Произошла ошибка при выполнении метода 'findByListLocationId'(HibernateLocationCrudDAO)"

        );
    }
}
