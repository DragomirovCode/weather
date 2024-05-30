package ru.dragomirov.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LocationRequestDTO {
    public int id;
    public String name;
    public BigDecimal latitude;
    public BigDecimal longitude;
}
