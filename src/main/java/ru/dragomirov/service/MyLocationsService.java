package ru.dragomirov.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import ru.dragomirov.dao.HibernateLocationCrudDAO;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.dto.request.WeatherByCoordinatesRequestDTO;
import ru.dragomirov.entity.Location;
import ru.dragomirov.entity.Session;
import ru.dragomirov.exception.SessionExpiredException;
import ru.dragomirov.exception.api.WeatherApiCallException;
import ru.dragomirov.util.WeatherApiUrlBuilderUtil;
import ru.dragomirov.util.constant.ApiKeyConstant;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class MyLocationsService {
    private final HibernateLocationCrudDAO hibernateLocationCrudDAO;
    private final HibernateSessionCrudDAO hibernateSessionCrudDAO;
    private final WeatherApiUrlBuilderUtil weatherApiUrlBuilderUtil;
    private final Gson gson;
    private final HttpClientService httpClientService;

    public MyLocationsService(WeatherApiUrlBuilderUtil weatherApiUrlBuilderUtil, HttpClientService httpClientService) {
        this.weatherApiUrlBuilderUtil = weatherApiUrlBuilderUtil;
        this.hibernateLocationCrudDAO = new HibernateLocationCrudDAO();
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.httpClientService = httpClientService;
    }

    public Session getSession(String uuid) {
        return hibernateSessionCrudDAO.findById(uuid).orElseThrow(() -> new SessionExpiredException("Session has expired"));
    }

    public List<Location> getLocationsByUserId(String userId) {
        return hibernateLocationCrudDAO.findByListLocationUserId(Integer.parseInt(userId));
    }

    private BigDecimal roundingToAnInteger(BigDecimal temperature) {
        return temperature.setScale(0, RoundingMode.DOWN);
    }

    @SneakyThrows
    public List<WeatherByCoordinatesRequestDTO> getWeatherDataForLocations(List<Location> locations) {
        List<WeatherByCoordinatesRequestDTO> locationWeatherData = new ArrayList<>();

        for (Location loc : locations) {
            try {
                String apiUrl = buildApiUrl(loc.getLatitude(), loc.getLongitude());
                HttpResponse response = httpClientService.executeRequest(apiUrl);

                WeatherByCoordinatesRequestDTO requestDTO = fetchWeatherData(response);

                processWeatherData(requestDTO, loc);
                locationWeatherData.add(requestDTO);

            } catch (WeatherApiCallException e) {
                throw new WeatherApiCallException("Error accessing the API");
            }
        }
        return locationWeatherData;
    }

    private WeatherByCoordinatesRequestDTO fetchWeatherData(HttpResponse response) {
        try {
            String jsonStr = EntityUtils.toString(response.getEntity());
            return gson.fromJson(jsonStr, WeatherByCoordinatesRequestDTO.class);
        } catch (WeatherApiCallException | IOException e) {
            throw new WeatherApiCallException("Error accessing the API");
        }
    }

    private void processWeatherData(WeatherByCoordinatesRequestDTO requestDTO, Location loc) {
        requestDTO.setName(loc.getName());
        requestDTO.coordinates.setLatitude(loc.getLatitude());
        requestDTO.coordinates.setLongitude(loc.getLongitude());
        requestDTO.main.setTemperatureActual(roundingToAnInteger(requestDTO.getMain().temperatureActual));
        requestDTO.main.setTemperatureFeelsLike(roundingToAnInteger(requestDTO.getMain().temperatureFeelsLike));
    }

    private String buildApiUrl(BigDecimal latitude, BigDecimal longitude) {
        String apiKey = ApiKeyConstant.API_KEY_CONSTANT.getValue();
        return weatherApiUrlBuilderUtil.buildLatLonCityWeatherApiUrl(latitude, longitude, apiKey);
    }
}
