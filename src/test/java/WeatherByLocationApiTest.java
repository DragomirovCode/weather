import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.dragomirov.dto.request.WeatherByLocationRequestDTO;
import ru.dragomirov.exception.InvalidParameterException;
import ru.dragomirov.service.SearchCityWeatherService;
import ru.dragomirov.util.WeatherApiUrlBuilder;
import ru.dragomirov.util.constant.ApiKeyConstant;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherByLocationApiTest {
    private SearchCityWeatherService service;
    private WeatherApiUrlBuilder weatherApiUrlBuilder;

    @BeforeEach
    void setUp() {
        weatherApiUrlBuilder = mock(WeatherApiUrlBuilder.class);
        service = new SearchCityWeatherService(weatherApiUrlBuilder);
    }

    @Test
    void shouldThrowExceptionForInvalidCityParameter_One() {
        assertThrows(InvalidParameterException.class, () -> service.getWeatherByCity(""));
    }

    @Test
    @DisplayName("should return weather data for valid city in API")
    void getWeatherByCity_shouldWeatherDataForValidCity_inApi() {
        List<WeatherByLocationRequestDTO> weatherData = service.getWeatherByCity("Kurganinsk");

        assertNotNull(weatherData);
        assertFalse(weatherData.isEmpty());
        assertEquals("Kurganinsk", weatherData.get(0).name);
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
