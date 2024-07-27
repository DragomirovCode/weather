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
import ru.dragomirov.util.AuthenticationRequestUtil;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
  TODO: the tests need to be run separately
  */
public class UserRegistrationTest {
    HibernateUserCrudDAO hibernateUserCrudDAO;
    RegistrationService registrationService;
    AuthenticationRequestUtil authenticationRequestUtil;
    HttpServletResponse resp;
    HttpServletRequest req;

    @BeforeEach
    void setUp() {
        resp = mock(HttpServletResponse.class);
        req = mock(HttpServletRequest.class);

        hibernateUserCrudDAO = new HibernateUserCrudDAO();
        registrationService = new RegistrationService();
        authenticationRequestUtil = new AuthenticationRequestUtil(req);
    }

    @SneakyThrows
    @Test
    @DisplayName("process authentication request should add user in database")
    void processAuthenticationRequest_shouldAddUser_inDatabase() {
        authenticationRequestUtil.setLogin("testLogin");
        authenticationRequestUtil.setPassword("testPassword");
        authenticationRequestUtil.setButton("registration");

        registrationService.processAuthenticationRequest(authenticationRequestUtil, resp);

        List<User> users = hibernateUserCrudDAO.findAll();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("testLogin", users.get(0).getLogin());
    }

    @SneakyThrows
    @Test
    @DisplayName("process authentication request should throw login exception because not unique username")
    void processAuthenticationRequest_shouldThrowLoginException_notUniqueUsername() {
        authenticationRequestUtil.setLogin("testLogin");
        authenticationRequestUtil.setPassword("testPassword");
        authenticationRequestUtil.setButton("registration");

        registrationService.processAuthenticationRequest(authenticationRequestUtil, resp);

        assertThrows(LoginException.class, () ->
                registrationService.processAuthenticationRequest(authenticationRequestUtil, resp));
    }
}
