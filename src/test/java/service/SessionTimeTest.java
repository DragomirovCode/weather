package service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.entity.Session;
import ru.dragomirov.service.CookieTimeService;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class SessionTimeTest {
    HttpServletResponse resp;
    HttpServletRequest req;

    @BeforeEach
    void setUp() {
        resp = mock(HttpServletResponse.class);
        req = mock(HttpServletRequest.class);
    }

    @SneakyThrows
    @Test
    @DisplayName("validate and handle session should empty list in database")
    void validateAndHandleSession_shouldEmptyList_inDatabase() {
        HibernateSessionCrudDAO hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
        CookieTimeService timeService = new CookieTimeService();
        UUID uuid = UUID.randomUUID();

        hibernateSessionCrudDAO.create(new Session(String.valueOf(uuid), 1, LocalDateTime.now()));

        timeService.validateAndHandleSession(String.valueOf(uuid), req, resp);

        assertTrue(hibernateSessionCrudDAO.findAll().isEmpty());
    }
}
