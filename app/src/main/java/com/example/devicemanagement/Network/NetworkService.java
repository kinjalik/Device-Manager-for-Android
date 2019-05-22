package com.example.devicemanagement.Network;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.devicemanagement.SharedPreferencesNames;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    private static SharedPreferences sharedPreferences;

    public static void setSharedPreferences(SharedPreferences s) {
        sharedPreferences = s;
    }

    private static final String BASE_URL = "https://techno-park-backend.herokuapp.com/";
    private Retrofit mRetrofit;
    private NetworkService() {
        /*
          Intercepting HTTP-requests for logging
         */
        // Creating interceptor's middleware on verbose log level
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String s) {
                Log.v("HTTP", s);
            }
        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor AuthIntterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                String password = sharedPreferences.getString(SharedPreferencesNames.APP_PREFERENCES_PASSWORD, "");

                if (password != null && !password.equals("")) {
                    HttpUrl url = original.url().newBuilder().addQueryParameter("password", password).build();
                    return chain.proceed(original.newBuilder().url(url).build());
                } else {
                    return chain.proceed(original);
                }
            }
        };

        // Creating HTTP-client and using interceptor
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(AuthIntterceptor)
                .addInterceptor(logInterceptor);

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
