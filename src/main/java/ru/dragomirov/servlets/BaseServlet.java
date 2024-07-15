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
import ru.dragomirov.exception.DatabaseOperationException;
import ru.dragomirov.exception.EntityExistsException;
import ru.dragomirov.exception.InvalidParameterException;
import ru.dragomirov.exception.SessionExpiredException;
import ru.dragomirov.exception.api.WeatherApiException;
import ru.dragomirov.exception.authentication.LoginException;
import ru.dragomirov.exception.authentication.PasswordException;
import ru.dragomirov.utils.constants.WebPageConstants;

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
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        webContext = TemplateEngineConfig.buildWebContext(req, resp, getServletContext());
        try {
            super.service(req, resp);
        } catch (LoginException | PasswordException e) {
            webContext.setVariable("error", e.getMessage());
            templateEngine.process("error/authentication/authentication-error", webContext, resp.getWriter());
        } catch (SessionExpiredException | EntityExistsException | ServletException e) {
            resp.sendRedirect(WebPageConstants.LOGIN_PAGE_X.getValue());
        } catch (InvalidParameterException | WeatherApiException e) {
            webContext.setVariable("error", e.getMessage());
            templateEngine.process("error/error", webContext, resp.getWriter());
        } catch (DatabaseOperationException e) {
            webContext.setVariable("error", e.getMessage());
            templateEngine.process("error/database-error", webContext, resp.getWriter());
        }
    }
}
