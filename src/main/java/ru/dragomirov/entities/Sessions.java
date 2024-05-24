package ru.dragomirov.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "Sessions")
public class Sessions {
    @Id
    private String id;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}
