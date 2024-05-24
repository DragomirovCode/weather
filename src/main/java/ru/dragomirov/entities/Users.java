package ru.dragomirov.entities;

import javax.persistence.*;

@Entity
@Table(name = "Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;

    public Users() {}

    public Users(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
