package ru.dragomirov.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.dragomirov.service.SaveLocationService;

import java.io.IOException;

@WebServlet(name = "SaveLocationServlet", urlPatterns = "/save-location")
public class SaveLocationServlet extends BaseServlet {
    private SaveLocationService saveLocationService;

    @Override
    public void init() {
        this.saveLocationService = new SaveLocationService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String otherUuid = (String) getServletContext().getAttribute("myUuid");
        String city = req.getParameter("city");
        String latitude = req.getParameter("latitude");
        String longitude = req.getParameter("longitude");

        saveLocationService.saveLocation(otherUuid, city, latitude, longitude);
        resp.sendRedirect("/");
    }
}
