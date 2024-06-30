package ru.dragomirov.dto.request;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LocationRequestDTO {
    public int id;
    public String name;
    @SerializedName("coord")
    public Coordinates coordinates;

    public static class Coordinates {
        @SerializedName("lat")
        public BigDecimal latitude;
        @SerializedName("lon")
        public BigDecimal longitude;
    }
}
