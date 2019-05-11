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

    public boolean isHasPermit() {
        return hasPermit;
    }

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
}
