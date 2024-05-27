package ru.dragomirov.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Sessions")
public class Session {
    @Id
    private String id;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    public Session() {}

    public Session(String id, int userId, LocalDateTime expiresAt) {
        this.id = id;
        this.userId = userId;
        this.expiresAt = expiresAt;
    }
}
