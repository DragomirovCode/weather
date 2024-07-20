package ru.dragomirov.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "latitude")
    private BigDecimal latitude;

    @Column(name = "longitude")
    private BigDecimal longitude;

    public Location(String name, BigDecimal latitude, BigDecimal longitude, int userId) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
    }
}