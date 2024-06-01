package ru.dragomirov.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
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

import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "UniqueCityWeatherServlet", urlPatterns = "/search-by-name")
public class UniqueCityWeatherServlet extends HttpServlet {
    private HibernateLocationCrudDAO hibernateLocationCrudDAO;
    @Override
    public void init(){
        this.hibernateLocationCrudDAO = new HibernateLocationCrudDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String cityName = req.getParameter("city");
            String apiKey =  "66b4f9f34b5073497f497f4d4f2be438";
            String apiUrl = buildApiUrl(cityName, apiKey);

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
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private String buildApiUrl(String city, String key) {
        return "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key;
    }
}
