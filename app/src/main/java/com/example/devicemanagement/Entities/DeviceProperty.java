package com.example.devicemanagement.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceProperty {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("device_id")
    @Expose
    public int deviceId;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("value")
    @Expose
    public String value;
}
