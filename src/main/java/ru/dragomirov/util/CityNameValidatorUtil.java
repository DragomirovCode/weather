package ru.dragomirov.util;

import ru.dragomirov.exception.InvalidParameterException;

import java.util.regex.Pattern;

public class CityNameValidatorUtil {
    private final Pattern regex = Pattern.compile("[^a-zA-Z\\s:]");

    public void validate(String cityName) {
        if (cityName.isEmpty()) {
            throw new InvalidParameterException("Parameter city is invalid");
        }
        if (regex.matcher(cityName).find()) {
            throw new InvalidParameterException("City name contains invalid characters");
        }
    }
}
