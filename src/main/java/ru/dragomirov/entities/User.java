package ru.dragomirov.entities;

import javax.persistence.*;

@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;

    public User() {}

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
