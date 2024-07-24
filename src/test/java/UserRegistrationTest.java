import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserRegistrationTest {
    @SneakyThrows
    @Test
    public void shouldAddUserInDatabase(){
        // Создаем mock объекты
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        // Настраиваем mock объекты
        when(req.getParameter("login")).thenReturn("test");
        when(req.getParameter("password")).thenReturn("1");
        when(req.getParameter("button")).thenReturn("registration");

        // Создаем AuthenticationRequest и RegistrationService
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(req);
        RegistrationService registrationService = new RegistrationService();

        // Устанавливаем параметры
        authenticationRequest.setLogin("test");
        authenticationRequest.setPassword("1");
        authenticationRequest.setButton("registration");

        // Вызываем метод, который хотим протестировать
        registrationService.processAuthenticationRequest(authenticationRequest, resp);

        // Проверяем, что был выполнен редирект на страницу логина
        verify(resp).sendRedirect("/login");
    }

    @Test
    public void shouldThrowLoginException() {
        // Создаем mock объекты
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(req);
        RegistrationService registrationService = new RegistrationService();
        HibernateUserCrudDAO hibernateUserCrudDAO = new HibernateUserCrudDAO();
        User user = new User();
        user.setLogin("test");
        user.setPassword("1");
        hibernateUserCrudDAO.create(user);

        // Устанавливаем параметры
        authenticationRequest.setLogin("test");
        authenticationRequest.setPassword("1");
        authenticationRequest.setButton("registration");

        // Проверяем, что выбрасывается исключение при вызове метода
        assertThrows(LoginException.class, () -> {
            registrationService.processAuthenticationRequest(authenticationRequest, resp);
        });
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
        System.out.println("После: ... " );

        // Проверяем, что выбрасывается исключение при вызове метода
        Optional<Session> finalSession = session;
        assertThrows(NoSuchElementException.class, () -> {
            finalSession.get().getId();
        });
    }
}
