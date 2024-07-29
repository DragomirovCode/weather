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
@Table(
        name = "Locations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"latitude", "longitude", "user_id"})
)
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "user_id")
    private int userId;

    @Column(precision = 40, scale = 35)
    private BigDecimal latitude;

    @Column(precision = 49, scale = 35)
    private BigDecimal longitude;

    public Location(String name, BigDecimal latitude, BigDecimal longitude, int userId) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
    }
}
