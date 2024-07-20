package ru.dragomirov.util;
import org.hibernate.Session;
import ru.dragomirov.exception.DatabaseOperationException;

/**
 * HibernateSessionManagerUtil используется для управления сессиями Hibernate.
 * Он предоставляет функциональные интерфейсы и методы для выполнения запросов к базе данных и транзакций.
 *
 * @SessionQuery<T> используется для выполнения операции с сеансом Hibernate и возвращает результат типа T.
 *
 * @TransactionOperation используется для выполнения транзакционных операций без возвращения результата.
 *
 * @performSessionQuery(SessionQuery<T> sessionQuery, String errorMessage)
 * используется для выполнения запроса к базе данных с использованием сеанса Hibernate.
 *
 * @performTransaction(TransactionOperation transactionOperation)
 * используется для выполнения транзакции с использованием сеанса Hibernate.
 * Он начинает транзакцию, выполняет операцию и затем фиксирует транзакцию.
 */
public class HibernateSessionManagerUtil {
    public interface SessionQuery<T> {
        T execute(Session session);
    }

    public interface TransactionOperation {
        void execute(Session session);
    }

    public static <T> T performSessionQuery(SessionQuery<T> sessionQuery, String errorMessage) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return sessionQuery.execute(session);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(errorMessage);
            throw new DatabaseOperationException("An error occurred on the server side");
        }
    }

    public static void performTransaction(TransactionOperation transactionOperation, String errorMessage) {
        Session session = null;
        try {
            session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            session.beginTransaction();
            transactionOperation.execute(session);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            System.out.println(errorMessage);
            throw new DatabaseOperationException("An error occurred on the server side");
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}