package ru.dragomirov.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.thymeleaf.context.WebContext;
import ru.dragomirov.config.TemplateEngineConfig;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.entity.Session;
import ru.dragomirov.service.CookieTimeService;
import ru.dragomirov.util.constant.WebPageConstant;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "CookieTimeServlet", urlPatterns = "")
public class CookieTimeServlet extends BaseServlet {
    private HibernateSessionCrudDAO hibernateSessionCrudDAO;
    private CookieTimeService cookieTimeService;

    @Override
    public void init(){
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
        this.cookieTimeService = new CookieTimeService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String uuid = (String) getServletContext().getAttribute("myUuid");

        Optional<Session> session = hibernateSessionCrudDAO.findById(uuid);

        cookieTimeService.validateAndHandleSession(uuid, req, resp);

        WebContext context = TemplateEngineConfig.buildWebContext(req, resp, req.getServletContext());
        context.setVariable("mySession", session);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/my");
        dispatcher.forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uuid = (String) getServletContext().getAttribute("myUuid");
        String button = req.getParameter("button");

        if (button.equals("login")) {
            resp.sendRedirect(WebPageConstant.LOGIN_PAGE_X.getValue());
        } else if (button.equals("registration")) {
            resp.sendRedirect(WebPageConstant.REGISTRATION_PAGE_X.getValue());
        }

        if (button.equals("exit")) {
            resp.sendRedirect(WebPageConstant.MAIN_PAGE_X.getValue());
            HttpSession exitSession = req.getSession(false);
            exitSession.removeAttribute("user");
            Optional<Session> session = hibernateSessionCrudDAO.findById(uuid);
            hibernateSessionCrudDAO.delete(String.valueOf(session.get().getId()));
        }
    }
}