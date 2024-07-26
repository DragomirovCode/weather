import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.dao.HibernateUserCrudDAO;
import ru.dragomirov.entity.Session;
import ru.dragomirov.entity.User;
import ru.dragomirov.exception.authentication.LoginException;
import ru.dragomirov.service.LoginService;
import ru.dragomirov.service.RegistrationService;
import ru.dragomirov.servlet.CookieTimeServlet;
import ru.dragomirov.util.AuthenticationRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserRegistrationTest {
    private HibernateUserCrudDAO hibernateUserCrudDAO;
    private RegistrationService registrationService;
    private AuthenticationRequest authenticationRequest;
    private HttpServletResponse resp;
    private HttpServletRequest req;

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
    void processAuthenticationRequest_shouldThrowLoginException_NotUniqueUsername() {
        authenticationRequest.setLogin("testLogin");
        authenticationRequest.setPassword("testPassword");
        authenticationRequest.setButton("registration");

        registrationService.processAuthenticationRequest(authenticationRequest, resp);

        assertThrows(LoginException.class, () ->
                registrationService.processAuthenticationRequest(authenticationRequest, resp));
    }

    @Test
    @SneakyThrows
    void shouldHandleExitButton() {
        // Создаем мок-объекты
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpSession httpSession = mock(HttpSession.class);

        // Инициализируем сервлет и другие необходимые объекты
        CookieTimeServlet cookieTimeServlet = new CookieTimeServlet();
        cookieTimeServlet.init();

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(req);
        LoginService loginService = new LoginService();
        HibernateUserCrudDAO hibernateUserCrudDAO = new HibernateUserCrudDAO();
        HibernateSessionCrudDAO hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
        User user = new User();
        user.setLogin("test");
        user.setPassword("1");
        hibernateUserCrudDAO.create(user);

        // Настраиваем мок HttpSession
        when(req.getSession()).thenReturn(httpSession);
        when(req.getSession(false)).thenReturn(httpSession);
        doNothing().when(httpSession).setAttribute(anyString(), any());
        doNothing().when(httpSession).removeAttribute("user");

        // Настраиваем запрос аутентификации
        authenticationRequest.setLogin("test");
        authenticationRequest.setPassword("1");
        authenticationRequest.setButton("login");

        // Обрабатываем логин
        loginService.handleLogin(authenticationRequest, req, resp);

        Optional<Session> session = hibernateSessionCrudDAO.findByUserId(user.getId());
        System.out.println("До: " + session.get().getId());

        // Симулируем нажатие кнопки выхода
        when(req.getParameter("uuid")).thenReturn(session.get().getId());
        when(req.getParameter("exit")).thenReturn("exit");

        // Вызываем метод doPost для проверки удаления атрибута "user"
        cookieTimeServlet.doPost(req, resp);

        session = hibernateSessionCrudDAO.findByUserId(user.getId());
        System.out.println("После: ... ");

        // Проверяем, что выбрасывается исключение при вызове метода
        Optional<Session> finalSession = session;
        assertThrows(NoSuchElementException.class, () -> {
            finalSession.get().getId();
        });
    }
}
