package ru.dragomirov.errorhandling;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class Http409ErrorHandler implements ErrorHandler {
    @Override
    public void httpErrors(HttpServletResponse resp, String errorMessage) throws IOException {
        resp.setStatus(HttpServletResponse.SC_CONFLICT);
        String errorResponse = "{\"message\": \"" + errorMessage + "\"}";
        resp.getWriter().write(errorResponse);
    }
}
