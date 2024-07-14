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
import org.thymeleaf.context.WebContext;
import ru.dragomirov.config.thymeleaf.TemplateEngineConfig;
import ru.dragomirov.dao.HibernateLocationCrudDAO;
import ru.dragomirov.dao.HibernateSessionCrudDAO;
import ru.dragomirov.dto.request.WeatherByCoordinatesRequestDTO;
import ru.dragomirov.entities.Location;
import ru.dragomirov.entities.Session;
import ru.dragomirov.exception.SessionExpiredException;
import ru.dragomirov.utils.Utils;
import ru.dragomirov.utils.constants.ApiKeyConstant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "MyLocationsServlet", urlPatterns = "/my")
public class MyLocationsServlet extends BaseServlet {
    private HibernateLocationCrudDAO hibernateLocationCrudDAO;
    private HibernateSessionCrudDAO hibernateSessionCrudDAO;
    private Utils utils;

    @Override
    public void init() {
        this.hibernateLocationCrudDAO = new HibernateLocationCrudDAO();
        this.hibernateSessionCrudDAO = new HibernateSessionCrudDAO();
        this.utils = new Utils();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String otherUuid = (String) getServletContext().getAttribute("myUuid");

        if (otherUuid == null || otherUuid.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Пользователя с таким uuid не существует");
            return;
        }

        Optional<Session> session = hibernateSessionCrudDAO.findById(otherUuid);

        if (session.isEmpty()) {
            throw new SessionExpiredException("Session has expired");
        }

        List<Location> location = hibernateLocationCrudDAO.findByListLocationUserId(session.get().getUserId());

        String apiKey = ApiKeyConstant.API_KEY_CONSTANT.getValue();

        CloseableHttpClient httpClient = HttpClients.createDefault();

        List<WeatherByCoordinatesRequestDTO> locationWeatherData = new ArrayList<>();
        for (Location loc : location) {
            String apiUrl = utils.buildLatLonCityWeatherApiUrl(loc.getLatitude(), loc.getLongitude(), apiKey);
            HttpGet request = new HttpGet(apiUrl);
            HttpResponse response = httpClient.execute(request);
            String jsonStr = EntityUtils.toString(response.getEntity());
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            WeatherByCoordinatesRequestDTO requestDTO = gson.fromJson(jsonStr, WeatherByCoordinatesRequestDTO.class);
            requestDTO.setName(loc.getName());
            requestDTO.coordinates.setLatitude(loc.getLatitude());
            requestDTO.coordinates.setLongitude(loc.getLongitude());
            locationWeatherData.add(requestDTO);
        }
        WebContext context = TemplateEngineConfig.buildWebContext(req, resp, req.getServletContext());
        context.setVariable("locations", locationWeatherData);
        templateEngine.process("main", context, resp.getWriter());
    }
}