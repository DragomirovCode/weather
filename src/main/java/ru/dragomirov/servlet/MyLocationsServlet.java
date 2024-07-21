package ru.dragomirov.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;
import ru.dragomirov.config.TemplateEngineConfig;
import ru.dragomirov.dto.request.WeatherByCoordinatesRequestDTO;
import ru.dragomirov.entity.Location;
import ru.dragomirov.entity.Session;
import ru.dragomirov.exception.SessionExpiredException;
import ru.dragomirov.service.MyLocationsService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "MyLocationsServlet", urlPatterns = "/my")
public class MyLocationsServlet extends BaseServlet {
    private MyLocationsService myLocationsService;

    @Override
    public void init() {
        this.myLocationsService = new MyLocationsService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String otherUuid = (String) getServletContext().getAttribute("myUuid");

        if (otherUuid == null || otherUuid.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Пользователя с таким uuid не существует");
            return;
        }

        Optional<Session> session = myLocationsService.getSession(otherUuid);

        if (session.isEmpty()) {
            throw new SessionExpiredException("Session has expired");
        }

        List<Location> locations = myLocationsService.getLocationsByUserId(String.valueOf(session.get().getUserId()));
        List<WeatherByCoordinatesRequestDTO> locationWeatherData = myLocationsService.getWeatherDataForLocations(locations);

        WebContext context = TemplateEngineConfig.buildWebContext(req, resp, req.getServletContext());
        context.setVariable("locations", locationWeatherData);
        templateEngine.process("main", context, resp.getWriter());
    }
}