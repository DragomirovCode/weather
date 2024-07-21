package ru.dragomirov.exception.api;

public class WeatherApiCallException extends RuntimeException {
    public WeatherApiCallException(String message) {
        super(message);
    }
}
