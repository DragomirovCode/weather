package ru.dragomirov.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.dao.HibernateUserCrudDAO;
import ru.dragomirov.entities.Session;
import ru.dragomirov.entities.User;
import ru.dragomirov.utils.requests.LoginRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends BaseServlet {
    private HibernateUserCrudDAO hibernateUserCrudDAO;
    private HibernateSessionCrudDAO hibernateSessionCrudDAO;

    @Override
    public void init() {
        this.hibernateUserCrudDAO = new HibernateUserCrudDAO();
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LoginRequest loginRequest = new LoginRequest(req);

        if (!loginRequest.isValid()) {
            HttpErrorHandlingServlet.handleError(400, resp,
                    "Ошибка: логин и пароль должны быть указаны");
            return;
        }

        switch (loginRequest.getButton()) {
            case "login":
                Optional<User> user = hibernateUserCrudDAO.findByLoginAndPassword(loginRequest.getLogin(), loginRequest.getPassword());
                if (user.isPresent()) {
                    LocalDateTime nowTime = LocalDateTime.now();
                    LocalDateTime futureTime = nowTime.plusHours(1);
                    Optional<Session> uuid = hibernateSessionCrudDAO.findByUserId(user.get().getId());
                    HttpSession session = req.getSession();
                    session.setAttribute("user", uuid.get().getUserId());

                    Session sessionUpdateTime = new Session(uuid.get().getId(), user.get().getId(), futureTime);
                    hibernateSessionCrudDAO.update(sessionUpdateTime);

                    Cookie cookie = new Cookie("uuid", uuid.get().getId());
                    cookie.setMaxAge(40);
                    resp.addCookie(cookie);

                    resp.sendRedirect("/?uuid=" + uuid.get().getId());
                } else {
                    HttpErrorHandlingServlet.handleError(401, resp,
                            "Ошибка: пользователя с таким логином или паролем не существует");
                }
                break;
            case "registration":
                resp.sendRedirect("/registration");
                break;
        }
    }
}
