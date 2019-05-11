package com.example.devicemanagement;

import android.support.annotation.NonNull;
import android.util.Log;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    private static final String BASE_URL = "https://techno-park-backend.herokuapp.com/";
    private Retrofit mRetrofit;
    private NetworkService() {
        /*
          Intercepting HTTP-requests for logging
         */
        // Creating interceptor's middleware on verbose log level
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String s) {
                Log.v("HTTP", s);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // Creating HTTP-client and using interceptor
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        /*
          Creating Retrofit2 instance, that uses GSON as JSON-coder and previously created HTTP-client
         */
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    // Singleton pattern
    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public BackendApi getApi() {
        return mRetrofit.create(BackendApi.class);
    }
}
