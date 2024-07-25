package ru.dragomirov.dao;

import ru.dragomirov.entity.Location;
import ru.dragomirov.util.HibernateSessionManagerUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class HibernateLocationCrudDAO implements LocationDAO {
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
                new IllegalArgumentException("Location c id " + id + " не найдено"));
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

    @Override
    public Optional<Location> findByLocationName(String name) {
        return Optional.ofNullable((Location) HibernateSessionManagerUtil.performSessionQuery(session ->
                        session.createQuery("FROM  Location WHERE name = :name")
                                .setParameter("name", name)
                                .uniqueResult(),
                "Произошла ошибка при выполнении метода 'findByLocationName'(HibernateLocationCrudDAO)"));
    }

    @Override
    public Optional<Location> findByLocationLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude, int userId) {
        return Optional.ofNullable((Location) HibernateSessionManagerUtil.performSessionQuery(session ->
                        session.createQuery("FROM Location  WHERE latitude = :latitude AND longitude = :longitude AND userId = :userId")
                                .setParameter("latitude", latitude)
                                .setParameter("longitude", longitude)
                                .setParameter("userId", userId)
                                .uniqueResult(),
                "Произошла ошибка при выполнении метода 'findByLocationLatitudeAndLongitude'(HibernateLocationCrudDAO)"
        ));
    }

    @Override
    public Optional<Location> findByLocationLatitudeAndLongitudeAndUserId(BigDecimal latitude, BigDecimal longitude, int userId, String name) {
        return Optional.ofNullable((Location) HibernateSessionManagerUtil.performSessionQuery(session ->
                        session.createQuery(
                                        "FROM Location  WHERE latitude = :latitude AND longitude = :longitude AND userId = :userId AND name = :name")
                                .setParameter("latitude", latitude)
                                .setParameter("longitude", longitude)
                                .setParameter("userId", userId)
                                .setParameter("name", name)
                                .uniqueResult(),
                "Произошла ошибка при выполнении метода 'findByLocationLatitudeAndLongitude'(HibernateLocationCrudDAO)"
        ));
    }
}
