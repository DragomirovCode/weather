package ru.dragomirov.config.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.thymeleaf.TemplateEngine;
import ru.dragomirov.config.TemplateEngineConfig;

@WebListener
public class TemplateListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        TemplateEngine templateEngine = TemplateEngineConfig.buildTemplateEngine(servletContext);

        servletContext.setAttribute("templateEngine", templateEngine);
    }
}
