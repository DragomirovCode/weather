package ru.dragomirov.service;

import org.apache.http.HttpResponse;
import ru.dragomirov.dto.request.WeatherByLocationRequestDTO;
import ru.dragomirov.exception.api.WeatherApiCallException;
import ru.dragomirov.util.CityNameValidatorUtil;
import ru.dragomirov.util.WeatherApiUrlBuilder;
import ru.dragomirov.util.constant.ApiKeyConstant;

import java.io.IOException;
import java.util.List;

public class SearchCityWeatherService {
    private final WeatherApiUrlBuilder weatherApiUrlBuilder;
    private final CityNameValidatorUtil cityNameValidatorUtil;
    private final HttpClientService httpClientService;
    private final WeatherApiResponseProcessor responseProcessor;

    public SearchCityWeatherService(WeatherApiUrlBuilder weatherApiUrlBuilder,
                                    HttpClientService httpClientService) {
        this.weatherApiUrlBuilder = weatherApiUrlBuilder;
        this.cityNameValidatorUtil = new CityNameValidatorUtil();
        this.httpClientService = httpClientService;
        this.responseProcessor = new WeatherApiResponseProcessor();
    }

    public List<WeatherByLocationRequestDTO> getWeatherByCity(String cityName) throws WeatherApiCallException {
        cityNameValidatorUtil.validate(cityName);
        String apiUrl = buildApiUrl(cityName);

        try {
            HttpResponse response = httpClientService.executeRequest(apiUrl);
            return responseProcessor.process(response);
        } catch (IOException e) {
            throw new WeatherApiCallException("Error accessing the API");
        }
    }

    private String buildApiUrl(String cityName) {
        String apiKey = ApiKeyConstant.API_KEY_CONSTANT.getValue();
        return weatherApiUrlBuilder.buildCityWeatherApiUrl(cityName, apiKey);
    }
}
