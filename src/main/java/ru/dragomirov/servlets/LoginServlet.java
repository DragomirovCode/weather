package ru.dragomirov.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.dao.HibernateUserCrudDAO;
import ru.dragomirov.entities.Session;
import ru.dragomirov.entities.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
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
            req.getRequestDispatcher("/login.html").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Ошибка при загрузке страницы.");
            req.getRequestDispatcher("/errors/serverError.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
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
                case "login":
                    Optional<User> user = hibernateUserCrudDAO.findByLoginAndPassword(login, password);
                    if (user.isPresent()) {
                        LocalDateTime nowTime = LocalDateTime.now();
                        LocalDateTime futureTime = nowTime.plusSeconds(30);
                        Optional<Session> uuid = hibernateSessionCrudDAO.findByUserId(user.get().getId());
                        HttpSession session = req.getSession();
                        session.setAttribute("user",uuid.get().getUserId());

                        Session sessionUpdateTime = new Session(uuid.get().getId(), user.get().getId(), futureTime);
                        hibernateSessionCrudDAO.update(sessionUpdateTime);

                        Cookie cookie = new Cookie("uuid", uuid.get().getId());
                        cookie.setMaxAge(40);

                        resp.sendRedirect("/?uuid=" + uuid.get().getId());
                    } else {
                        resp.setStatus(HttpServletResponse.SC_CONFLICT);
                        resp.getWriter().write("Ошибка: пользователя с таким логином или паролем не существует");
                    }
                    break;
                case "registration":
                    resp.sendRedirect("/registration");
                    break;
            }
        } catch (Exception e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
