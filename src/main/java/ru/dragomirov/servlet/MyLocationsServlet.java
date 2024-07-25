package ru.dragomirov.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;
import ru.dragomirov.config.TemplateEngineConfig;
import ru.dragomirov.dto.request.WeatherByCoordinatesRequestDTO;
import ru.dragomirov.entity.Location;
import ru.dragomirov.entity.Session;
import ru.dragomirov.service.MyLocationsService;
import ru.dragomirov.util.WeatherApiUrlBuilder;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MyLocationsServlet", urlPatterns = "/my")
public class MyLocationsServlet extends BaseServlet {
    private MyLocationsService myLocationsService;

    @Override
    public void init() {
        WeatherApiUrlBuilder weatherApiUrlBuilder = new WeatherApiUrlBuilder();
        this.myLocationsService = new MyLocationsService(weatherApiUrlBuilder);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String otherUuid = (String) getServletContext().getAttribute("myUuid");

        Session session = myLocationsService.getSession(otherUuid);

        List<Location> locations = myLocationsService.getLocationsByUserId(String.valueOf(session.getUserId()));
        List<WeatherByCoordinatesRequestDTO> locationWeatherData = myLocationsService.getWeatherDataForLocations(locations);

        WebContext context = TemplateEngineConfig.buildWebContext(req, resp, req.getServletContext());
        context.setVariable("mySession", session);
        context.setVariable("locations", locationWeatherData);
        templateEngine.process("main", context, resp.getWriter());
    }
}
