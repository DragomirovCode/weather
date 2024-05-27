package ru.dragomirov.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.dao.HibernateUserCrudDAO;
import ru.dragomirov.entities.Session;
import ru.dragomirov.entities.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "RegistrationServlet", urlPatterns = "/registration")
public class RegistrationServlet extends HttpServlet {
    private HibernateUserCrudDAO hibernateUserCrudDAO;
    private HibernateSessionCrudDAO hibernateSessionCrudDAO;
    @Override
    public void init(){
        this.hibernateUserCrudDAO = new HibernateUserCrudDAO();
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.getRequestDispatcher("/registration.html").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Ошибка при загрузке страницы.");
            req.getRequestDispatcher("/errors/serverError.jsp").forward(req, resp);
        }
    }

    //TODO: Не может перейти пока не заполнишь данные
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        try {
            String login = req.getParameter("login");
            String password = req.getParameter("password");
            String button = req.getParameter("button");

            if (login.isEmpty() || password.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Ошибка: логин и пароль должны быть указаны");
                return;
            }

            switch (button) {
                case "registration":
                    Optional<User> user = hibernateUserCrudDAO.findByLogin(login);

                    if (user.isPresent()) {
                        resp.setStatus(HttpServletResponse.SC_CONFLICT);
                        resp.getWriter().write("Ошибка: пользователь с таким логином уже существует");
                    }

                    User newUser = new User(login, password);
                    hibernateUserCrudDAO.create(newUser);

                    LocalDateTime nowTime = LocalDateTime.now();
                    LocalDateTime futureTime = nowTime.plusSeconds(30);
                    UUID sessionId = UUID.randomUUID();
                    Session session = new Session(sessionId.toString() ,newUser.getId(), futureTime);
                    hibernateSessionCrudDAO.create(session);
                    resp.sendRedirect("/login");
                    break;
                case "login":
                    resp.sendRedirect("/login");
                    break;
            }
        } catch (Exception e){
            System.err.println("Произошла ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
