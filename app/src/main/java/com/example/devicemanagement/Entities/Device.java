package com.example.devicemanagement.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Device {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("owner_id")
    @Expose
    public int ownerId;
    @SerializedName("description")
    @Expose
    public String description;

    public int getId() {
        return id;
    }

    public Device setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Device setName(String name) {
        this.name = name;
        return this;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public Device setOwnerId(int ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Device setDescription(String description) {
        this.description = description;
        return this;
    }
}
