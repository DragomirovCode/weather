package ru.dragomirov.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import ru.dragomirov.dao.HibernateLocationCrudDAO;
import ru.dragomirov.dto.request.LocationRequestDTO;
import ru.dragomirov.dto.response.LocationResponseDTO;
import ru.dragomirov.entities.Location;
import ru.dragomirov.utils.MappingUtil;
import ru.dragomirov.utils.Utils;
import ru.dragomirov.utils.constants.ApiKeyConstant;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "UniqueCityWeatherServlet", urlPatterns = "/unique-search-city-weather")
public class UniqueCityWeatherServlet extends BaseServlet {
    private HibernateLocationCrudDAO hibernateLocationCrudDAO;
    private Utils utils;

    @Override
    public void init(){
        this.hibernateLocationCrudDAO = new HibernateLocationCrudDAO();
        this.utils = new Utils();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String cityName = req.getParameter("city");
        String apiKey = ApiKeyConstant.API_KEY_CONSTANT.getValue();
        String apiUrl = utils.buildUniqueCityWeatherApiUrl(cityName, apiKey);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        // Создание объекта запроса
        HttpGet request = new HttpGet(apiUrl);
        // Выполнение запроса
        HttpResponse response = httpClient.execute(request);
        // Преобразование тела ответа в строку
        String jsonStr = EntityUtils.toString(response.getEntity());
        // Десериализация строки JSON в Java-объект
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        LocationRequestDTO requestDTO = gson.fromJson(jsonStr, LocationRequestDTO.class);

        Location location = MappingUtil.locationToEntity(requestDTO);
        hibernateLocationCrudDAO.create(location);

        List<Location> locationList = hibernateLocationCrudDAO.findAll();
        List<LocationResponseDTO> responseDTOList = locationList.stream()
                    .map(MappingUtil::locationToDTO).collect(Collectors.toList());

        String jsonResponse = gson.toJson(responseDTOList);
        resp.getWriter().write(jsonResponse);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
