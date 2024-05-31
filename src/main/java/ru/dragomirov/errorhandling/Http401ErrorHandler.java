package ru.dragomirov.errorhandling;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class Http401ErrorHandler implements ErrorHandler {
    @Override
    public void httpErrors(HttpServletResponse resp, String errorMessage) throws IOException {
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String errorResponse = "{\"message\": \"" + errorMessage + "\"}";
        resp.getWriter().write(errorResponse);
    }
}
