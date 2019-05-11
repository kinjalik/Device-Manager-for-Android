package com.example.devicemanagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

class Authorisation {
    private static String LOG_TAG = "AUTH";

    // Singleton management START
    private static Authorisation instance;

    private static SharedPreferences mSettings;

    static Authorisation getInstance(Context cntx) {
        Log.i(LOG_TAG, "Authorisation instance called");
        if (instance == null)
            instance = new Authorisation(cntx);

        return instance;
    }

    private Authorisation(Context cntx) {
        mSettings = cntx.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

    }

    // Singleton management END

    private static final String APP_PREFERENCES = "settings";
    private static final String APP_PREFERENCES_LOGIN = "login";
    private static final String APP_PREFERENCES_PASSWORD = "password";

    /*
      if
     */
    public void authorise(String login, String passwd, final Callback<User> callback) {
        mSettings.edit().putString(APP_PREFERENCES_LOGIN, login);
        mSettings.edit().putString(APP_PREFERENCES_PASSWORD, passwd);
        isAuthorised(new Callback<Boolean>() {
            @Override
            public void onResult(Boolean res) {
                // ToDo: ПЕРВЫМ ДЕЛОМ Вынести получение пользователя в отдельную функцию из isAuthorised
                // ToDo: ПЕРВЫМ ДЕЛОМ Реализовать проверку данных и, если они правильные, их сохранение
            }
        });
    }

    /*
        if no credentials in SharedPref or wrong credentials in SharedPref: FALSE
        else: TRUE
     */
    public void isAuthorised(final Callback<Boolean> callback) {
        Log.i(LOG_TAG, "Authorisation check started");

        // ToDo: Remove credentials from SharedPreferences if they are wrong
        if (mSettings.contains(APP_PREFERENCES_LOGIN) && mSettings.contains(APP_PREFERENCES_PASSWORD)) {
            Log.i(LOG_TAG, "Checking credentials in sharedPrefs");

            final String login = mSettings.getString(APP_PREFERENCES_LOGIN, "");
            final String passwd = mSettings.getString(APP_PREFERENCES_PASSWORD, "");


            final boolean[] isSuccess = new boolean[1];

            NetworkService.getInstance().getApi().getUserWithCreds(login, passwd).enqueue(new retrofit2.Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User u = response.body();
                    if (u != null)
                        callback.onResult(u.isHasPermit());
                    else
                        callback.onResult(false);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e(LOG_TAG, t.getMessage(), t);
                    callback.onResult(false);
                }
            });
        } else {
            Log.i(LOG_TAG, "No credentials in SharedPref.");
            callback.onResult(false);
        }
    }

    interface Callback<T> {
        void onResult(T res);
    }
}