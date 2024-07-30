package ru.dragomirov.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Sessions")
public class Session {
    @Id
    @Column(unique = true)
    private String id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    public Session(String id, int userId, LocalDateTime expiresAt) {
        this.id = id;
        this.userId = userId;
        this.expiresAt = expiresAt;
    }
}
