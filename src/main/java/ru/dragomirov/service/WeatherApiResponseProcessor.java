package ru.dragomirov.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import ru.dragomirov.dto.request.WeatherByLocationRequestDTO;
import ru.dragomirov.exception.NotFoundException;

import java.io.IOException;
import java.util.List;

public class WeatherApiResponseProcessor {
    public List<WeatherByLocationRequestDTO> process(HttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() == 404) {
            throw new NotFoundException("City was not found");
        }

        String jsonStr = EntityUtils.toString(response.getEntity());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        WeatherByLocationRequestDTO[] requestDTOArray = gson.fromJson(jsonStr, WeatherByLocationRequestDTO[].class);
        return List.of(requestDTOArray);
    }
}
