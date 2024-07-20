import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import ru.dragomirov.exception.InvalidParameterException;
import ru.dragomirov.servlet.SearchCityWeatherServlet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherApiTest {
    @Test
    @SneakyThrows
    void shouldThrow4xxExceptionForInvalidCityParameter() {
        SearchCityWeatherServlet servlet = new SearchCityWeatherServlet();

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        when(mockRequest.getParameter("city")).thenReturn("");

        try {
            servlet.doGet(mockRequest, mockResponse);
        } catch (InvalidParameterException e) {
            assertEquals("Parameter city is invalid", e.getMessage());
        }
    }
}
