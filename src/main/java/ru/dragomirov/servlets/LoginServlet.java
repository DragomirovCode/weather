package ru.dragomirov.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.dao.HibernateUserCrudDAO;
import ru.dragomirov.entities.Session;
import ru.dragomirov.entities.User;
import ru.dragomirov.exception.authentication.LoginException;
import ru.dragomirov.exception.authentication.PasswordException;
import ru.dragomirov.utils.constants.WebPageConstants;
import ru.dragomirov.utils.request.AuthenticationRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "LoginServlet" , urlPatterns = "/login")
public class LoginServlet extends BaseServlet {
    private HibernateUserCrudDAO hibernateUserCrudDAO;
    private HibernateSessionCrudDAO hibernateSessionCrudDAO;

    @Override
    public void init() {
        this.hibernateUserCrudDAO = new HibernateUserCrudDAO();
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        templateEngine.process(WebPageConstants.LOGIN_PAGE_X.getValue(), webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(req);

        if (authenticationRequest.loginIsValid()) {
            throw new LoginException("Parameter login is invalid");
        }

        if (authenticationRequest.passwordIsValid()) {
            throw new PasswordException("Parameter password is invalid");
        }

        switch (authenticationRequest.getButton()) {
            case "login":
                Optional<User> user = hibernateUserCrudDAO.findByLoginAndPassword(authenticationRequest.getLogin(), authenticationRequest.getPassword());
                if (user.isPresent()) {
                    LocalDateTime nowTime = LocalDateTime.now();
                    LocalDateTime futureTime = nowTime.plusHours(1);
                    Optional<Session> sessionId = hibernateSessionCrudDAO.findByUserId(user.get().getId());
                    Session newSession;
                    if (sessionId.isEmpty()) {
                        UUID sessionIdUUID = UUID.randomUUID();
                        newSession = new Session(sessionIdUUID.toString(), user.get().getId(), futureTime);
                        hibernateSessionCrudDAO.create(newSession);
                    } else {
                        newSession = new Session(sessionId.get().getId(), user.get().getId(), futureTime);
                        hibernateSessionCrudDAO.update(newSession);
                    }
                    HttpSession session = req.getSession();

                    session.setAttribute("user", newSession.getUserId());

                    Session sessionUpdateTime = new Session(newSession.getId(), user.get().getId(), futureTime);
                    hibernateSessionCrudDAO.update(sessionUpdateTime);

                    Cookie cookie = new Cookie("uuid", newSession.getId());
                    cookie.setMaxAge(3600);
                    resp.addCookie(cookie);

                    resp.sendRedirect("/?uuid=" + newSession.getId());
                } else {
                    throw new LoginException("User with such a login or password does not exist");
                }
                break;
            case "registration":
                resp.sendRedirect(WebPageConstants.REGISTRATION_PAGE_X.getValue());
                break;
        }
    }
}
