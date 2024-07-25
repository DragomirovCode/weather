package ru.dragomirov.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import ru.dragomirov.dto.request.WeatherByLocationRequestDTO;
import ru.dragomirov.exception.InvalidParameterException;
import ru.dragomirov.exception.NotFoundException;
import ru.dragomirov.exception.api.WeatherApiCallException;
import ru.dragomirov.util.WeatherApiUrlBuilder;
import ru.dragomirov.util.constant.ApiKeyConstant;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class SearchCityWeatherService {
    private WeatherApiUrlBuilder weatherApiUrlBuilder;
    private final Pattern regex = Pattern.compile("[^a-zA-Z\\s:]");

    public SearchCityWeatherService(WeatherApiUrlBuilder weatherApiUrlBuilder) {
        this.weatherApiUrlBuilder = weatherApiUrlBuilder;
    }

    public List<WeatherByLocationRequestDTO> getWeatherByCity(String cityName) throws WeatherApiCallException {
        validateCityName(cityName);
        String apiUrl = buildApiUrl(cityName);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpResponse response = executeRequest(httpClient, apiUrl);
            return processResponse(response);
        } catch (IOException e) {
            throw new WeatherApiCallException("Error accessing the API");
        }
    }

    private void validateCityName(String cityName) {
        if (cityName.isEmpty()) {
            throw new InvalidParameterException("Parameter city is invalid");
        }

        if (regex.matcher(cityName).find()) {
            throw new InvalidParameterException("City name contains invalid characters");
        }
    }

    private String buildApiUrl(String cityName) {
        String apiKey = ApiKeyConstant.API_KEY_CONSTANT.getValue();
        return weatherApiUrlBuilder.buildCityWeatherApiUrl(cityName, apiKey);
    }

    private HttpResponse executeRequest(CloseableHttpClient httpClient, String apiUrl) throws IOException {
        HttpGet request = new HttpGet(apiUrl);
        return httpClient.execute(request);
    }

    private List<WeatherByLocationRequestDTO> processResponse(HttpResponse response) throws IOException, WeatherApiCallException {
        if (response.getStatusLine().getStatusCode() == 404) {
            throw new NotFoundException("City was not found");
        }

        String jsonStr = EntityUtils.toString(response.getEntity());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WeatherByLocationRequestDTO[] requestDTOArray = gson.fromJson(jsonStr, WeatherByLocationRequestDTO[].class);
        return List.of(requestDTOArray);
    }
}
