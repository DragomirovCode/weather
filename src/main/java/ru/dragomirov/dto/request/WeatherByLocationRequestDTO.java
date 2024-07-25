package ru.dragomirov.dto.request;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WeatherByLocationRequestDTO {
    private String name;

    private String country;

    private String state;

    @SerializedName("lat")
    private BigDecimal latitude;

    @SerializedName("lon")
    private BigDecimal longitude;
}
