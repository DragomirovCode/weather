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
    void shouldThrowExceptionForInvalidCityParameter_Two() {
        assertThrows(InvalidParameterException.class, () -> service.getWeatherByCity("123"));
    }

    @Test
    void shouldReturnWeatherDataForValidCity() {
        when(weatherApiUrlBuilder.buildCityWeatherApiUrl(anyString(), anyString()))
                .thenReturn(
                "https://api.openweathermap.org/geo/1.0/direct?q=" + "Moscow" + "&limit=5&appid=" +
                        ApiKeyConstant.API_KEY_CONSTANT.getValue()
        );

        List<WeatherByLocationRequestDTO> weatherData = service.getWeatherByCity("Moscow");

        assertNotNull(weatherData);
        assertFalse(weatherData.isEmpty());
    }
}