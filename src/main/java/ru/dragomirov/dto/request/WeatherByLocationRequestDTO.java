package ru.dragomirov.dto.request;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WeatherByLocationRequestDTO {
    public String name;

    public String country;

    public String state;

    @SerializedName("lat")
    public BigDecimal latitude;

    @SerializedName("lon")
    public BigDecimal longitude;
}
