package ru.dragomirov.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import ru.dragomirov.exception.api.WeatherApiCallException;

import java.io.IOException;

public class HttpClientService {
    public HttpResponse executeRequest(String apiUrl) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(apiUrl);
            return httpClient.execute(request);
        } catch (WeatherApiCallException e) {
            throw new WeatherApiCallException("Error accessing the API");
        }
    }
}
