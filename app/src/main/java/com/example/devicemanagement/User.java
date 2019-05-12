package com.example.devicemanagement;

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

    /*
        SETTERS
     */

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
