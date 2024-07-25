package ru.dragomirov.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;
import ru.dragomirov.config.TemplateEngineConfig;
import ru.dragomirov.dto.request.WeatherByLocationRequestDTO;
import ru.dragomirov.exception.api.WeatherApiCallException;
import ru.dragomirov.service.SearchCityWeatherService;
import ru.dragomirov.util.WeatherApiUrlBuilder;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchCityWeatherServlet", urlPatterns = "/search-city-weather")
public class SearchCityWeatherServlet extends BaseServlet {
    private SearchCityWeatherService searchCityWeatherService;
    private WeatherApiUrlBuilder weatherApiUrlBuilder;

    @Override
    public void init() {
        this.weatherApiUrlBuilder = new WeatherApiUrlBuilder();
        this.searchCityWeatherService = new SearchCityWeatherService(this.weatherApiUrlBuilder);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String cityName = req.getParameter("city");
        String uuid = (String) getServletContext().getAttribute("myUuid");
        System.out.println("UUID: " + uuid);

        try {
            List<WeatherByLocationRequestDTO> requestDTOList = searchCityWeatherService.getWeatherByCity(cityName);
            WebContext context = TemplateEngineConfig.buildWebContext(req, resp, req.getServletContext());
            context.setVariable("mySession", uuid);
            context.setVariable("cityList", requestDTOList);
            templateEngine.process("city-weather", context, resp.getWriter());
        } catch (WeatherApiCallException e) {
            throw new WeatherApiCallException("Error accessing the API");
        }
    }
}
