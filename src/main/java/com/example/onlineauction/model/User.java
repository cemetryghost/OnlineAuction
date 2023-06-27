package com.example.onlineauction.model;

import com.example.onlineauction.constants.Role;
import com.example.onlineauction.constants.Status;


import java.time.LocalDate;

public class User {
    private int idusers;
    private String name, surname, login, password;
    private LocalDate birth_date;
    private Role role;
    private Status status;

    public User() {
    }

    public User(String name, String surname, String login, String password, LocalDate birth_date, Role role, Status status) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.birth_date = birth_date;
        this.role = role;
        this.status = status;
    }

    public int getId() {
        return idusers;
    }
    public void setId(int id) {
        this.idusers = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public LocalDate getBirth_date() {
        return birth_date;
    }
    public void setBirth_date(LocalDate birth_date) {
        this.birth_date = birth_date;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public boolean isBlocked() {
        return status == Status.BLOCK;
    }

}

