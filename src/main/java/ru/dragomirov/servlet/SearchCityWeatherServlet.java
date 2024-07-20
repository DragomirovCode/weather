package ru.dragomirov.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;
import ru.dragomirov.config.thymeleaf.TemplateEngineConfig;
import ru.dragomirov.dto.request.WeatherByLocationRequestDTO;
import ru.dragomirov.exception.api.WeatherApiException;
import ru.dragomirov.service.SearchCityWeatherService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchCityWeatherServlet", urlPatterns = "/search-city-weather")
public class SearchCityWeatherServlet extends BaseServlet {
    private SearchCityWeatherService searchCityWeatherService;

    @Override
    public void init() {
        this.searchCityWeatherService = new SearchCityWeatherService();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String cityName = req.getParameter("city");

        try {
            List<WeatherByLocationRequestDTO> requestDTOList = searchCityWeatherService.getWeatherByCity(cityName);
            WebContext context = TemplateEngineConfig.buildWebContext(req, resp, req.getServletContext());
            context.setVariable("cityList", requestDTOList);
            templateEngine.process("city-weather", context, resp.getWriter());
        } catch (WeatherApiException e) {
            throw new WeatherApiException("Error accessing the API");
        }
    }
}
