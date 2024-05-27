package ru.dragomirov.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.entities.Session;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "MainServlet", urlPatterns = "")
public class MainServlet extends HttpServlet {
    private HibernateSessionCrudDAO hibernateSessionCrudDAO;

    @Override
    public void init(){
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String uuid = req.getParameter("uuid");

            LocalDateTime now = LocalDateTime.now();

            Optional<Session> session = hibernateSessionCrudDAO.findById(uuid);

            Cookie[] cookies = req.getCookies();

            if (cookies != null){
                for (Cookie cookie: cookies) {
                    if (!cookie.equals(session.get().getUserId())) {
                        HttpSession session1 = req.getSession(false);
                        session1.setAttribute("user", session.get().getId());
                    }
                }
            }

            if (session.get().getExpiresAt().isBefore(now)) {
                resp.sendRedirect("/login");
                HttpSession exitSession = req.getSession(false);
                exitSession.removeAttribute("user");
                return;
            }
            req.getRequestDispatcher("/main.html").forward(req, resp);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String button = req.getParameter("exit");

            switch (button) {
                case "exit":
                    resp.sendRedirect("/login");
                    HttpSession exitSession = req.getSession(false);
                    exitSession.removeAttribute("user");
                    break;
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
