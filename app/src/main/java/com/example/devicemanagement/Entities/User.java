package com.example.devicemanagement.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class User {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("login")
    @Expose
    private String login;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("reg_date")
    @Expose
    private Date regDate;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("hasPermit")
    @Expose
    private boolean hasPermit;
    @SerializedName("password")
    @Expose
    private String password;

    /*
        GETTERS
     */

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Date getRegDate() {
        return regDate;
    }

    public String getEmail() {
        return email;
    }

    public boolean isHasPermit() {
        return hasPermit;
    }

    public String getPassword() {
        return password;
    }

    /*
        SETTERS
     */

    public User setLogin(String login) {
        this.login = login;
        return this;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public User setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    /*
        SETTERS
     */

}
