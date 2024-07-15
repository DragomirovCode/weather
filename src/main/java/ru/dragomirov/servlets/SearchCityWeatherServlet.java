package ru.dragomirov.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.thymeleaf.context.WebContext;
import ru.dragomirov.config.thymeleaf.TemplateEngineConfig;
import ru.dragomirov.dto.request.WeatherByLocationRequestDTO;
import ru.dragomirov.exception.InvalidParameterException;
import ru.dragomirov.exception.NotFoundException;
import ru.dragomirov.exception.api.WeatherApiException;
import ru.dragomirov.utils.Utils;
import ru.dragomirov.utils.constants.ApiKeyConstant;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@WebServlet(name = "SearchCityWeatherServlet", urlPatterns = "/search-city-weather")
public class SearchCityWeatherServlet extends BaseServlet {
    private Utils utils;
    private final Pattern regex = Pattern.compile("[^a-zA-Z\\s:]");

    @Override
    public void init(){
        this.utils = new Utils();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String cityName = req.getParameter("city");

        if (cityName.isEmpty()) {
            throw new InvalidParameterException("Parameter city is invalid");
        }

        if (regex.matcher(cityName).find()) {
            throw new InvalidParameterException("Is invalid");
        }
        try {
            String apiKey = ApiKeyConstant.API_KEY_CONSTANT.getValue();
            String apiUrl = utils.buildCityWeatherApiUrl(cityName, apiKey);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            // Создание объекта запроса
            HttpGet request = new HttpGet(apiUrl);
            // Выполнение запроса
            HttpResponse response = httpClient.execute(request);

            if (response.getStatusLine().getStatusCode() == 404) {
                throw new NotFoundException("City was not found");
            }

            // Преобразование тела ответа в строку
            String jsonStr = EntityUtils.toString(response.getEntity());
            // Десериализация строки JSON в Java-объект
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            WeatherByLocationRequestDTO[] requestDTOArray = gson.fromJson(jsonStr, WeatherByLocationRequestDTO[].class);
            List<WeatherByLocationRequestDTO> requestDTOList = List.of(requestDTOArray);
            WebContext context = TemplateEngineConfig.buildWebContext(req, resp, req.getServletContext());
            context.setVariable("cityList", requestDTOList);
            templateEngine.process("city-weather", context, resp.getWriter());
        } catch (WeatherApiException e) {
            throw new WeatherApiException("Error accessing the API");
        }
    }
}
