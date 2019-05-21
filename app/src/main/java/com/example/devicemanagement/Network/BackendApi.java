package com.example.devicemanagement.Network;

import com.example.devicemanagement.Entities.Device;
import com.example.devicemanagement.Entities.DeviceProperty;
import com.example.devicemanagement.Entities.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BackendApi {
    @GET("/users/{login}")
    public Call<User> getUserWithLogin(@Path("login") String login);

    @GET("/users/{id}")
    public Call<User> getUserWithId(@Path("id") int login);

    @GET("/users/{login}")
    public Call<User> getUserWithCreds(@Path("login") String login, @Query("password") String password);

    @POST("/users")
    public Call<User> registerUser(@Body User data);

    @GET("/users/{id}/devices")
    public Call<Device[]> getUsersDevices(@Path("id") int id);

    @POST("/users/{id}/devices")
    public Call<Device> addUserDevice(@Path("id") int id, @Body Device dev);

    @DELETE("/users/{uid}/devices/{did}")
    public Call<Device> removeUserDevice(@Path("uid") int userId, @Path("did") int deviceId);

    @GET("/users/{uid}/devices/{did}")
    public Call<Device> getUsersDeviceByid(@Path("uid") int userId, @Path("did") int deviceId);

    @GET("/users/{uid}/devices/{did}/props")
    public Call<DeviceProperty[]> getUserDeviceProps(@Path("uid") int userId, @Path("did") int deviceId);

    @POST("/users/{uid}/devices/{did}/props")
    public Call<DeviceProperty> addUserDeviceProp(@Path("uid") int userId, @Path("did") int deviceId, @Body DeviceProperty dp);

    @DELETE("/users/{uid}/devices/{did}/props/{pid}")
    public Call<DeviceProperty> removeUserDeviceProp(@Path("uid") int userId, @Path("did") int deviceId, @Path("pid") int propId);
}
