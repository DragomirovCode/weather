package ru.dragomirov.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.dragomirov.dao.HibernateUserCrudDAO;
import ru.dragomirov.entities.User;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "RegistrationServlet", urlPatterns = "/registration")
public class RegistrationServlet extends HttpServlet {
    private HibernateUserCrudDAO hibernateUserCrudDAO;
    @Override
    public void init(){
        this.hibernateUserCrudDAO = new HibernateUserCrudDAO();
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        try {
            String login = req.getParameter("login");
            String password = req.getParameter("password");

            if (login.isEmpty() || password.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Ошибка: логин и пароль должны быть указаны");
                return;
            }

            Optional<User> user = hibernateUserCrudDAO.findByLogin(login);

            if (user.isPresent()) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getWriter().write("Ошибка: пользователь с таким логином уже существует");
            }

            User newUser = new User(login, password);
            hibernateUserCrudDAO.create(newUser);
            resp.sendRedirect("/login");
        } catch (Exception e){
            System.err.println("Произошла ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
