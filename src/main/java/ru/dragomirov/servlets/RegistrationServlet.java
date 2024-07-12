package ru.dragomirov.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.dao.HibernateUserCrudDAO;
import ru.dragomirov.entities.Session;
import ru.dragomirov.entities.User;
import ru.dragomirov.utils.constants.WebPageConstants;
import ru.dragomirov.utils.request.AuthenticationRequest;
import ru.dragomirov.exception.InvalidParameterException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "RegistrationServlet", urlPatterns = "/registration")
public class RegistrationServlet extends BaseServlet {
    private HibernateUserCrudDAO hibernateUserCrudDAO;
    private HibernateSessionCrudDAO hibernateSessionCrudDAO;
    @Override
    public void init(){
        this.hibernateUserCrudDAO = new HibernateUserCrudDAO();
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        templateEngine.process(WebPageConstants.REGISTRATION_PAGE_X.getValue(), webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(req);

        if (authenticationRequest.loginIsValid()) {
            throw new InvalidParameterException("Error: Parameter login is invalid");
        }

        if (authenticationRequest.passwordIsValid()) {
            throw new InvalidParameterException("Error: Parameter password is invalid");
        }

        switch (authenticationRequest.getButton()) {
            case "registration":
                Optional<User> user = hibernateUserCrudDAO.findByLogin(authenticationRequest.getLogin());

                if (user.isPresent()) {
                    HttpErrorHandlingServlet.handleError(409, resp,
                            "Ошибка: пользователь с таким логином уже существует");
                }
                User newUser = new User(authenticationRequest.getLogin(), authenticationRequest.getPassword());
                hibernateUserCrudDAO.create(newUser);

                LocalDateTime nowTime = LocalDateTime.now();
                LocalDateTime futureTime = nowTime.plusHours(1);
                UUID sessionId = UUID.randomUUID();
                Session session = new Session(sessionId.toString(), newUser.getId(), futureTime);
                hibernateSessionCrudDAO.create(session);
                resp.sendRedirect(WebPageConstants.LOGIN_PAGE_X.getValue());
                break;
            case "login":
                resp.sendRedirect(WebPageConstants.LOGIN_PAGE_X.getValue());
                break;
        }
    }
}
