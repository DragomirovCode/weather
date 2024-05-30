package ru.dragomirov.servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import ru.dragomirov.dto.request.LocationRequestDTO;

@WebServlet(name = "RequestJsonServlet", urlPatterns = "/search-by-name")
public class RequestJsonServlet extends HttpServlet {
    @Override
    public void init(){}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            // Создание объекта запроса
            String requestURL =
                    "https://api.openweathermap.org/data/2.5/weather?q=Kurganinsk&appid=66b4f9f34b5073497f497f4d4f2be438";
            HttpGet request = new HttpGet(requestURL);
            // Выполнение запроса
            HttpResponse response = httpClient.execute(request);
            // Преобразование тела ответа в строку
            String jsonStr = EntityUtils.toString(response.getEntity());
            // Десериализация строки JSON в Java-объект
            Gson gson = new Gson();
            LocationRequestDTO requestDTO = gson.fromJson(jsonStr, LocationRequestDTO.class);
            System.out.println(requestDTO.name);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
