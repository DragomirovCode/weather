package ru.dragomirov.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.thymeleaf.context.WebContext;
import ru.dragomirov.config.TemplateEngineConfig;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.service.CookieTimeService;
import ru.dragomirov.util.constant.WebPageConstant;

import java.io.IOException;

@WebServlet(name = "CookieTimeServlet", urlPatterns = "")
public class CookieTimeServlet extends BaseServlet {
    private HibernateSessionCrudDAO hibernateSessionCrudDAO;
    private CookieTimeService cookieTimeService;

    @Override
    public void init() {
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
        this.cookieTimeService = new CookieTimeService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession httpSession = req.getSession();
        String otherUuid = (String) httpSession.getAttribute("myUuid");

        cookieTimeService.validateAndHandleSession(otherUuid, req, resp);

        WebContext context = TemplateEngineConfig.buildWebContext(req, resp, req.getServletContext());
        context.setVariable("mySession", otherUuid);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/my");
        dispatcher.forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession httpSession = req.getSession();
        String otherUuid = (String) httpSession.getAttribute("myUuid");

        String button = req.getParameter("button");

        switch (button) {
            case "exit" -> {
                resp.sendRedirect(WebPageConstant.MAIN_PAGE_X.getValue());
                HttpSession exitSession = req.getSession(false);
                exitSession.removeAttribute("user");
                hibernateSessionCrudDAO.delete(otherUuid);
            }
            case "login" -> resp.sendRedirect(WebPageConstant.LOGIN_PAGE_X.getValue());
            case "registration" -> resp.sendRedirect(WebPageConstant.REGISTRATION_PAGE_X.getValue());
        }
    }
}
