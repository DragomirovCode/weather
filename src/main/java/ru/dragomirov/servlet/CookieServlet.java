package ru.dragomirov.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.entity.Session;
import ru.dragomirov.exception.SessionExpiredException;
import ru.dragomirov.service.CookieService;
import ru.dragomirov.util.constant.WebPageConstant;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "CookieServlet", urlPatterns = "")
public class CookieServlet extends BaseServlet {
    private HibernateSessionCrudDAO hibernateSessionCrudDAO;
    private CookieService cookieService;

    @Override
    public void init(){
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
        this.cookieService = new CookieService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String uuid = (String) getServletContext().getAttribute("myUuid");

       cookieService.validateAndHandleSession(uuid, req, resp);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/my");
        dispatcher.forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uuid = (String) getServletContext().getAttribute("myUuid");
        String button = req.getParameter("exit");

        Optional<Session> session = hibernateSessionCrudDAO.findById(uuid);

        if (session.isEmpty()) {
            throw new SessionExpiredException("Session has expired");
        }

        switch (button) {
            case "exit":
                resp.sendRedirect(WebPageConstant.LOGIN_PAGE_X.getValue());
                HttpSession exitSession = req.getSession(false);
                exitSession.removeAttribute("user");
                hibernateSessionCrudDAO.delete(String.valueOf(session.get().getId()));
                break;
        }
    }
}
