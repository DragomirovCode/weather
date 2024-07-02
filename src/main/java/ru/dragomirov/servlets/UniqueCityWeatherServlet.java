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
import ru.dragomirov.dto.request.LocationRequestDTO;
import ru.dragomirov.utils.Utils;
import ru.dragomirov.utils.constants.ApiKeyConstant;

import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet(name = "UniqueCityWeatherServlet", urlPatterns = "/unique-search-city-weather")
public class UniqueCityWeatherServlet extends BaseServlet {
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
            HttpErrorHandlingServlet.handleError(400, resp, "Отсутствует нужное поле формы");
            return;
        }

        if (regex.matcher(cityName).find()) {
            HttpErrorHandlingServlet.handleError(400, resp,
                    "Не должно содержать спец.символы и цифры");
            return;
        }

        String apiKey = ApiKeyConstant.API_KEY_CONSTANT.getValue();
        String apiUrl = utils.buildUniqueCityWeatherApiUrl(cityName, apiKey);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        // Создание объекта запроса
        HttpGet request = new HttpGet(apiUrl);
        // Выполнение запроса
        HttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() == 404) {
            HttpErrorHandlingServlet.handleError(404, resp, "Город не найден");
            return;
        }

        // Преобразование тела ответа в строку
        String jsonStr = EntityUtils.toString(response.getEntity());
        // Десериализация строки JSON в Java-объект
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        LocationRequestDTO requestDTO = gson.fromJson(jsonStr, LocationRequestDTO.class);

        String jsonResponse = gson.toJson(requestDTO);
        resp.getWriter().write(jsonResponse);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
