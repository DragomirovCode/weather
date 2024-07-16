import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import ru.dragomirov.dao.HibernateUserCrudDAO;
import ru.dragomirov.entities.User;
import ru.dragomirov.exception.authentication.LoginException;
import ru.dragomirov.services.RegistrationService;
import ru.dragomirov.utils.request.AuthenticationRequest;

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
}
