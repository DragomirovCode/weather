package ru.dragomirov.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import ru.dragomirov.dao.HibernateLocationCrudDAO;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.dto.request.WeatherByCoordinatesRequestDTO;
import ru.dragomirov.entity.Location;
import ru.dragomirov.entity.Session;
import ru.dragomirov.exception.api.WeatherApiCallException;
import ru.dragomirov.util.WeatherApiUrlBuilder;
import ru.dragomirov.util.constant.ApiKeyConstant;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MyLocationsService {
    private final HibernateLocationCrudDAO hibernateLocationCrudDAO;
    private final HibernateSessionCrudDAO hibernateSessionCrudDAO;
    private final WeatherApiUrlBuilder weatherApiUrlBuilder;
    private final Gson gson;
    private final CloseableHttpClient httpClient;

    public MyLocationsService(WeatherApiUrlBuilder weatherApiUrlBuilder) {
        this.weatherApiUrlBuilder = weatherApiUrlBuilder;
        this.hibernateLocationCrudDAO = new HibernateLocationCrudDAO();
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.httpClient = HttpClients.createDefault();
    }

    public Optional<Session> getSession(String uuid) {
        return hibernateSessionCrudDAO.findById(uuid);
    }

    public List<Location> getLocationsByUserId(String userId) {
        return hibernateLocationCrudDAO.findByListLocationUserId(Integer.parseInt(userId));
    }

    private BigDecimal roundingToAnInteger(BigDecimal temperature) {
        return temperature.setScale(0, RoundingMode.DOWN);
    }

    public List<WeatherByCoordinatesRequestDTO> getWeatherDataForLocations(List<Location> locations) {
        List<WeatherByCoordinatesRequestDTO> locationWeatherData = new ArrayList<>();
        String apiKey = ApiKeyConstant.API_KEY_CONSTANT.getValue();

        for (Location loc : locations) {
            try {
                WeatherByCoordinatesRequestDTO requestDTO = fetchWeatherData(loc, apiKey);
                processWeatherData(requestDTO, loc);
                locationWeatherData.add(requestDTO);
            } catch (WeatherApiCallException e) {
                throw new WeatherApiCallException("Error accessing the API");
            }
        }
        return locationWeatherData;
    }

    private WeatherByCoordinatesRequestDTO fetchWeatherData(Location loc, String apiKey) {
        try {
            String apiUrl = weatherApiUrlBuilder.buildLatLonCityWeatherApiUrl(loc.getLatitude(), loc.getLongitude(), apiKey);
            HttpGet request = new HttpGet(apiUrl);
            HttpResponse response = httpClient.execute(request);
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
}
