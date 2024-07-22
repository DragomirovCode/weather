package ru.dragomirov.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.dragomirov.service.DeleteLocationService;

import java.io.IOException;

@WebServlet(name = "DeleteLocationServlet", urlPatterns = "/delete-location")
public class DeleteLocationServlet extends BaseServlet {
    private DeleteLocationService deleteLocationService;

    @Override
    public void init() {
        this.deleteLocationService = new DeleteLocationService();
    }
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String method = req.getMethod();
        String overrideMethod = req.getParameter("_method");
        if ("POST".equalsIgnoreCase(method) && "PATCH".equalsIgnoreCase(overrideMethod)) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String otherUuid = (String) getServletContext().getAttribute("myUuid");
        String latitudeStr = req.getParameter("latitude");
        String longitudeStr = req.getParameter("longitude");

        deleteLocationService.deleteLocation(otherUuid, latitudeStr, longitudeStr);

        resp.sendRedirect("/");
    }
}
