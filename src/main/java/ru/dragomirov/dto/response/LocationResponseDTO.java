package ru.dragomirov.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LocationResponseDTO {
    public int id;
    public String name;
    public BigDecimal latitude;
    public BigDecimal longitude;
}