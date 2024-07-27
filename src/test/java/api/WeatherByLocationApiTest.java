package api;

import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.dragomirov.dto.request.WeatherByLocationRequestDTO;
import ru.dragomirov.exception.InvalidParameterException;
import ru.dragomirov.service.HttpClientService;
import ru.dragomirov.service.SearchCityWeatherService;
import ru.dragomirov.util.WeatherApiUrlBuilderUtil;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherByLocationApiTest {
    static final String TEST_JSON_FILE = "src/test/resources/weather_forecast_by_location.json";
    static final String API_URL = "https://api.openweathermap.org/geo/1.0/direct?q=Kurganinsk&limit=5&appid=";
    SearchCityWeatherService service;
    WeatherApiUrlBuilderUtil weatherApiUrlBuilderUtil;
    HttpClientService httpClientService;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        weatherApiUrlBuilderUtil = mock(WeatherApiUrlBuilderUtil.class);
        httpClientService = mock(HttpClientService.class);
        service = new SearchCityWeatherService(weatherApiUrlBuilderUtil, httpClientService);

        String jsonWeather = Files.readString(Path.of(TEST_JSON_FILE));

        when(weatherApiUrlBuilderUtil.buildCityWeatherApiUrl(anyString(), anyString())).thenReturn(API_URL);

        HttpResponse mockResponse = createMockResponse(jsonWeather);
        when(httpClientService.executeRequest(anyString())).thenReturn(mockResponse);
    }

    @Test
    @DisplayName("should return weather data for valid city in API")
    void getWeatherByCity_shouldWeatherDataForValidCity_inApi() {
        List<WeatherByLocationRequestDTO> weatherData = service.getWeatherByCity("Kurganinsk");

        assertNotNull(weatherData);
        assertFalse(weatherData.isEmpty());
        assertEquals("Kurganinsk", weatherData.get(0).name);
    }

    @Test
    @DisplayName("should throw invalid parameter exception in api")
    void getWeatherByCity_shouldThrowInvalidParameterException_inApi() {
        assertThrows(InvalidParameterException.class, () -> service.getWeatherByCity(""));
    }

    @SneakyThrows
    @Test
    @DisplayName("should handle 500 internal server error")
    void getWeatherByCity_shouldHandle500Error_inApi() {
        HttpResponse mockResponse = createMockResponseWithStatus(500);
        when(httpClientService.executeRequest(anyString())).thenReturn(mockResponse);

        assertThrows(RuntimeException.class, () -> service.getWeatherByCity("Kurganinsk"));
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
