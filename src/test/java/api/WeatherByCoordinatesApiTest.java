package api;

import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.dragomirov.dao.HibernateLocationCrudDAO;
import ru.dragomirov.dto.request.WeatherByCoordinatesRequestDTO;
import ru.dragomirov.entity.Location;
import ru.dragomirov.service.HttpClientService;
import ru.dragomirov.service.MyLocationsService;
import ru.dragomirov.util.WeatherApiUrlBuilderUtil;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherByCoordinatesApiTest {
    static final String TEST_JSON_FILE = "src/test/resources/weather_forecast_by_coordinates.json";
    static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?lat=44.8851635&lon=40.5894674&appid=&units=metric";
    WeatherApiUrlBuilderUtil weatherApiUrlBuilderUtil;
    HttpClientService httpClientService;
    MyLocationsService service;
    HibernateLocationCrudDAO hibernateLocationCrudDAO;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        hibernateLocationCrudDAO = new HibernateLocationCrudDAO();

        httpClientService = mock(HttpClientService.class);
        weatherApiUrlBuilderUtil = mock(WeatherApiUrlBuilderUtil.class);
        service = new MyLocationsService(weatherApiUrlBuilderUtil, httpClientService);

        String jsonWeather = Files.readString(Path.of(TEST_JSON_FILE));

        when(weatherApiUrlBuilderUtil.buildLatLonCityWeatherApiUrl(any(BigDecimal.class), any(BigDecimal.class), any(String.class)))
                .thenReturn(API_URL);

        HttpResponse mockResponse = createMockResponse(jsonWeather);
        when(httpClientService.executeRequest(anyString())).thenReturn(mockResponse);
    }

    @Test
    @DisplayName("should return weather data about temperature actual")
    void getWeatherDataForLocations_shouldWeatherDataAboutTemperatureActual_inApi() {
        Location location = new Location();
        location.setLatitude(new BigDecimal("44.8851635"));
        location.setLongitude(new BigDecimal("40.5894674"));
        hibernateLocationCrudDAO.create(location);

        List<WeatherByCoordinatesRequestDTO> locationWeatherData = service.getWeatherDataForLocations(Collections.singletonList(location));

        assertNotNull(locationWeatherData);
        assertFalse(locationWeatherData.isEmpty());
        assertEquals(BigDecimal.valueOf(25), locationWeatherData.get(0).main.temperatureActual);
    }

    @SneakyThrows
    @Test
    @DisplayName("should handle 500 internal server error")
    void getWeatherByCity_shouldHandle500Error_inApi() {
        Location location = new Location();
        location.setLatitude(new BigDecimal("44.8851635"));
        location.setLongitude(new BigDecimal("40.5894674"));
        hibernateLocationCrudDAO.create(location);

        HttpResponse mockResponse = createMockResponseWithStatus(500);
        when(httpClientService.executeRequest(anyString())).thenReturn(mockResponse);

        assertThrows(RuntimeException.class, () -> service.getWeatherDataForLocations(Collections.singletonList(location)));
    }

    @SneakyThrows
    HttpResponse createMockResponseWithStatus(int statusCode) {
        HttpResponse mockResponse = mock(HttpResponse.class);
        StatusLine mockStatusLine = mock(StatusLine.class);

        when(mockStatusLine.getStatusCode()).thenReturn(statusCode);
        when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);

        return mockResponse;
    }

    @SneakyThrows
    HttpResponse createMockResponse(String json) {
        // add mock
        HttpResponse mockResponse = mock(HttpResponse.class);
        StatusLine mockStatusLine = mock(StatusLine.class);
        HttpEntity mockEntity = mock(HttpEntity.class);

        // Setting up the behavior for StatusLine
        when(mockStatusLine.getStatusCode()).thenReturn(200);
        when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);

        // setting up the behavior for HttpEntity
        when(mockEntity.getContent()).thenReturn(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
        when(mockResponse.getEntity()).thenReturn(mockEntity);

        return mockResponse;
    }
}
