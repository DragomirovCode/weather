package ru.dragomirov.service;

import org.apache.http.HttpResponse;
import ru.dragomirov.dto.request.WeatherByLocationRequestDTO;
import ru.dragomirov.exception.api.WeatherApiCallException;
import ru.dragomirov.util.CityNameValidatorUtil;
import ru.dragomirov.util.WeatherApiUrlBuilderUtil;
import ru.dragomirov.util.constant.ApiKeyConstant;

import java.io.IOException;
import java.util.List;

public class SearchCityWeatherService {
    private final WeatherApiUrlBuilderUtil weatherApiUrlBuilderUtil;
    private final CityNameValidatorUtil cityNameValidatorUtil;
    private final HttpClientService httpClientService;
    private final WeatherApiResponseProcessorService responseProcessor;

    public SearchCityWeatherService(WeatherApiUrlBuilderUtil weatherApiUrlBuilderUtil,
                                    HttpClientService httpClientService) {
        this.weatherApiUrlBuilderUtil = weatherApiUrlBuilderUtil;
        this.cityNameValidatorUtil = new CityNameValidatorUtil();
        this.httpClientService = httpClientService;
        this.responseProcessor = new WeatherApiResponseProcessorService();
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
        String apiKey = ApiKeyConstant.WEATHER_API_KEY.getValue();
        return weatherApiUrlBuilderUtil.buildCityWeatherApiUrl(cityName, apiKey);
    }
}
