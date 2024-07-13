package ru.dragomirov.dto.response;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WeatherByCoordinatesResponseDTO {
    public int id;
    public String name;
    @SerializedName("coord")
    public Coordinates coordinates;
    @SerializedName("main")
    public Main main;

    @Getter
    @Setter
    public static class Coordinates {
        @SerializedName("lat")
        public BigDecimal latitude;
        @SerializedName("lon")
        public BigDecimal longitude;
    }

    @Getter
    @Setter
    public static class Main {
        @SerializedName("temp")
        public BigDecimal temperatureActual;
        @SerializedName("temp_max")
        public BigDecimal temperatureMax;
        @SerializedName("temp_min")
        public BigDecimal temperatureMin;
        @SerializedName("feels_like")
        public BigDecimal temperatureFeelsLike;
    }
}
