package ru.dragomirov.services;

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
import ru.dragomirov.entities.Location;
import ru.dragomirov.entities.Session;
import ru.dragomirov.exception.api.WeatherApiException;
import ru.dragomirov.utils.WeatherApiUrlBuilder;
import ru.dragomirov.utils.constants.ApiKeyConstant;

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

    public MyLocationsService() {
        this.hibernateLocationCrudDAO = new HibernateLocationCrudDAO();
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
        this.weatherApiUrlBuilder = new WeatherApiUrlBuilder();
    }

    public Optional<Session> getSession(String uuid) {
        return hibernateSessionCrudDAO.findById(uuid);
    }

    public List<Location> getLocationsByUserId(String userId) {
        return hibernateLocationCrudDAO.findByListLocationUserId(Integer.parseInt(userId));
    }

    private BigDecimal roundingToAnInteger(BigDecimal temperature) {
        BigDecimal valueTem = new BigDecimal(String.valueOf(temperature));
        return valueTem.setScale(0, RoundingMode.DOWN);
    }

    public List<WeatherByCoordinatesRequestDTO> getWeatherDataForLocations(List<Location> locations) throws IOException {
        List<WeatherByCoordinatesRequestDTO> locationWeatherData = new ArrayList<>();
        String apiKey = ApiKeyConstant.API_KEY_CONSTANT.getValue();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            for (Location loc : locations) {
                try {
                    String apiUrl = weatherApiUrlBuilder.buildLatLonCityWeatherApiUrl(loc.getLatitude(), loc.getLongitude(), apiKey);
                    HttpGet request = new HttpGet(apiUrl);
                    HttpResponse response = httpClient.execute(request);

                    String jsonStr = EntityUtils.toString(response.getEntity());
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    WeatherByCoordinatesRequestDTO requestDTO = gson.fromJson(jsonStr, WeatherByCoordinatesRequestDTO.class);
                    requestDTO.setName(loc.getName());
                    requestDTO.coordinates.setLatitude(loc.getLatitude());
                    requestDTO.coordinates.setLongitude(loc.getLongitude());

                    requestDTO.main.setTemperatureActual(roundingToAnInteger(requestDTO.getMain().temperatureActual));
                    requestDTO.main.setTemperatureFeelsLike(roundingToAnInteger(requestDTO.getMain().temperatureFeelsLike));

                    locationWeatherData.add(requestDTO);
                } catch (WeatherApiException e) {
                    throw new WeatherApiException("Error accessing the API");
                }
            }
        }
        return locationWeatherData;
    }
}
