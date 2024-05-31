package ru.dragomirov.servlets;

import jakarta.servlet.http.HttpServletResponse;
import ru.dragomirov.errorhandling.ErrorHandler;
import ru.dragomirov.errorhandling.Http400ErrorHandler;
import ru.dragomirov.errorhandling.Http404ErrorHandler;
import ru.dragomirov.errorhandling.Http409ErrorHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpErrorHandlingServlet {
    private final Map<Integer, ErrorHandler> errorHandlers = new HashMap<>();

    {
        errorHandlers.put(400, new Http400ErrorHandler());
        errorHandlers.put(404, new Http404ErrorHandler());
        errorHandlers.put(409, new Http409ErrorHandler());
    }

    public void handleError(int errorCode, HttpServletResponse resp, String errorMessage) throws IOException {
        ErrorHandler errorHandler = errorHandlers.get(errorCode);
        if (errorHandler != null){
            errorHandler.httpErrors(resp, errorMessage);
        } else {
            System.err.println("Обработчик ошибок не найден для кода ошибки: " + errorCode);
        }
    }
}