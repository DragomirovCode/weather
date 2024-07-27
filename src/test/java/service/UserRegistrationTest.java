package service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.dragomirov.dao.HibernateUserCrudDAO;
import ru.dragomirov.entity.User;
import ru.dragomirov.exception.authentication.LoginException;
import ru.dragomirov.service.RegistrationService;
import ru.dragomirov.util.AuthenticationRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
  TODO: the tests need to be run separately
  */
public class UserRegistrationTest {
    HibernateUserCrudDAO hibernateUserCrudDAO;
    RegistrationService registrationService;
    AuthenticationRequest authenticationRequest;
    HttpServletResponse resp;
    HttpServletRequest req;

    @BeforeEach
    void setUp() {
        resp = mock(HttpServletResponse.class);
        req = mock(HttpServletRequest.class);

        hibernateUserCrudDAO = new HibernateUserCrudDAO();
        registrationService = new RegistrationService();
        authenticationRequest = new AuthenticationRequest(req);
    }

    @SneakyThrows
    @Test
    @DisplayName("process authentication request should add user in database")
    void processAuthenticationRequest_shouldAddUser_inDatabase() {
        authenticationRequest.setLogin("testLogin");
        authenticationRequest.setPassword("testPassword");
        authenticationRequest.setButton("registration");

        registrationService.processAuthenticationRequest(authenticationRequest, resp);

        List<User> users = hibernateUserCrudDAO.findAll();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("testLogin", users.get(0).getLogin());
    }

    @SneakyThrows
    @Test
    @DisplayName("process authentication request should throw login exception because not unique username")
    void processAuthenticationRequest_shouldThrowLoginException_notUniqueUsername() {
        authenticationRequest.setLogin("testLogin");
        authenticationRequest.setPassword("testPassword");
        authenticationRequest.setButton("registration");

        registrationService.processAuthenticationRequest(authenticationRequest, resp);

        assertThrows(LoginException.class, () ->
                registrationService.processAuthenticationRequest(authenticationRequest, resp));
    }
}
