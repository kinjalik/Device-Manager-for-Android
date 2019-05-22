package com.example.devicemanagement.Network;

import com.example.devicemanagement.Entities.Device;
import com.example.devicemanagement.Entities.DeviceProperty;
import com.example.devicemanagement.Entities.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BackendApi {
    @GET("/users/{login}")
    Call<User> getUserWithLogin(@Path("login") String login);

    @GET("/users/{id}")
    Call<User> getUserWithId(@Path("id") int login);

    @GET("/users/{login}")
    Call<User> getUserWithCredentials(@Path("login") String login, @Query("password") String password);

    @POST("/users")
    Call<User> registerUser(@Body User data);

    @GET("/users/{id}/devices")
    Call<Device[]> getUsersDevices(@Path("id") int id);

    @POST("/users/{id}/devices")
     Call<Device> addUserDevice(@Path("id") int id, @Body Device dev);

    @PUT("/users/{uid}/devices/{did}")
     Call<DeviceProperty> updateUserDevice(@Path("uid") int userIs, @Path("did") int deviceId, @Body Device dev);


    @DELETE("/users/{uid}/devices/{did}")
     Call<Device> removeUserDevice(@Path("uid") int userId, @Path("did") int deviceId);

    @GET("/users/{uid}/devices/{did}/props")
     Call<DeviceProperty[]> getUserDeviceProps(@Path("uid") int userId, @Path("did") int deviceId);

    @POST("/users/{uid}/devices/{did}/props")
     Call<DeviceProperty> addUserDeviceProp(@Path("uid") int userId, @Path("did") int deviceId, @Body DeviceProperty dp);

    @PUT("/users/{uid}/devices/{did}/props/{pid}")
     Call<DeviceProperty> updateUserDeviceProp(@Path("uid") int userIs, @Path("did") int deviceId, @Path("pid") int propId, @Body DeviceProperty dp);

    @DELETE("/users/{uid}/devices/{did}/props/{pid}")
     Call<DeviceProperty> removeUserDeviceProp(@Path("uid") int userId, @Path("did") int deviceId, @Path("pid") int propId);
}
