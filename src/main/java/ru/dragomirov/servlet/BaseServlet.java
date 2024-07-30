package ru.dragomirov.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import ru.dragomirov.config.TemplateEngineConfig;
import ru.dragomirov.exception.*;
import ru.dragomirov.exception.api.WeatherApiCallException;
import ru.dragomirov.exception.authentication.LoginException;
import ru.dragomirov.exception.authentication.PasswordException;
import ru.dragomirov.exception.authentication.RegistrationException;
import ru.dragomirov.util.constant.WebPageConstant;

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
        } catch (ServletException e) {
            resp.sendRedirect(WebPageConstant.LOGIN_PAGE_X.getValue());
        } catch (InvalidParameterException e) {
            webContext.setVariable("error", e.getMessage());
            templateEngine.process("error/error", webContext, resp.getWriter());
        } catch (DatabaseOperationException e) {
            webContext.setVariable("error", e.getMessage());
            templateEngine.process("error/database-error", webContext, resp.getWriter());
        } catch (WeatherApiCallException e) {
            webContext.setVariable("error", e.getMessage());
            templateEngine.process("error/api-error", webContext, resp.getWriter());
        } catch (EntityExistsException e) {
            resp.sendRedirect(WebPageConstant.MAIN_PAGE_X.getValue());
        } catch (SessionExpiredException e) {
            templateEngine.process("main", webContext, resp.getWriter());
        } catch (RegistrationException e) {
            webContext.setVariable("error", e.getMessage());
            templateEngine.process("error/authentication/registration", webContext, resp.getWriter());
        }
    }
}
