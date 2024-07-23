import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.dragomirov.dto.request.WeatherByCoordinatesRequestDTO;
import ru.dragomirov.entity.Location;
import ru.dragomirov.service.MyLocationsService;
import ru.dragomirov.util.WeatherApiUrlBuilder;
import ru.dragomirov.util.constant.ApiKeyConstant;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherByCoordinatesApiTest {
    private WeatherApiUrlBuilder weatherApiUrlBuilder;
    private MyLocationsService service;

    @BeforeEach
    void setUp() {
        weatherApiUrlBuilder = mock(WeatherApiUrlBuilder.class);
        service = new MyLocationsService(weatherApiUrlBuilder);
    }

    @Test
    void shouldReturnWeatherData() {
        Location location = new Location();
        location.setId(1);
        location.setName("Moscow");
        location.setLatitude(BigDecimal.valueOf(55.7504461));
        location.setLongitude(BigDecimal.valueOf(37.6174943));
        location.setUserId(1);

        String expectedUrl = "https://api.openweathermap.org/data/2.5/weather?lat=55.7504461&lon=37.6174943&appid="
                + ApiKeyConstant.API_KEY_CONSTANT.getValue() + "&units=metric";

        when(weatherApiUrlBuilder.buildLatLonCityWeatherApiUrl(any(BigDecimal.class), any(BigDecimal.class), any(String.class)))
                .thenReturn(expectedUrl);

        List<WeatherByCoordinatesRequestDTO> locationWeatherData = service.getWeatherDataForLocations(Collections.singletonList(location));

        assertNotNull(locationWeatherData);
        assertFalse(locationWeatherData.isEmpty());
    }
}
