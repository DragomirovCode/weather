package ru.dragomirov.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import ru.dragomirov.config.thymeleaf.TemplateEngineConfig;

import java.io.IOException;

public class BaseServlet extends HttpServlet {
    protected TemplateEngine templateEngine;
    protected WebContext webContext;

    @Override
    public void init(ServletConfig config) {
        try {
            super.init(config);
            ServletContext servletContext = config.getServletContext();
            templateEngine = (TemplateEngine) servletContext.getAttribute("templateEngine");
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        webContext = TemplateEngineConfig.buildWebContext(req, resp, getServletContext());
        webContext.setVariable("message", req.getParameter("message"));
        try {
            super.service(req, resp);
        } catch (IOException | ServletException e) {
            HttpErrorHandlingServlet.handleError(500, resp, e.getMessage());
        }
    }
}
