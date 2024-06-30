package ru.dragomirov.dto.response;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LocationResponseDTO {
    public int id;
    public String name;
    @SerializedName("lat")
    public BigDecimal latitude;
    @SerializedName("lon")
    public BigDecimal longitude;
}
