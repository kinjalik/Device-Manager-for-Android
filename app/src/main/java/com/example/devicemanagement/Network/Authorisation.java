package com.example.devicemanagement.Network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.devicemanagement.Callback;
import com.example.devicemanagement.Entities.User;
import com.example.devicemanagement.SharedPreferencesNames;

import retrofit2.Call;
import retrofit2.Response;

public class Authorisation {
    private static String LOG_TAG = "AUTH";

    // Singleton management START
    private static Authorisation instance;

    private static SharedPreferences mSettings;

    public static Authorisation getInstance(Context cntx) {
        Log.i(LOG_TAG, "Authorisation instance called");
        if (instance == null)
            instance = new Authorisation(cntx);

        return instance;
    }

    private Authorisation(Context cntx) {
        mSettings = cntx.getSharedPreferences(SharedPreferencesNames.APP_PREFERENCES, Context.MODE_PRIVATE);

    }

    // Singleton management END
    private final BackendApi api = NetworkService.getInstance().getApi();

    public void authorise(final String login, final String passwd, final Callback<Boolean> callback) {
        getUserWithCreds(login, passwd, new Callback<User>() {
            @Override
            public void onResult(User res) {
                if (res != null && res.isHasPermit()) {
                    Log.i(LOG_TAG, "Provided correct credentials. User authorised.");
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putString(SharedPreferencesNames.APP_PREFERENCES_LOGIN, login);
                    editor.putString(SharedPreferencesNames.APP_PREFERENCES_PASSWORD, passwd);
                    editor.putInt(SharedPreferencesNames.APP_PREFERENCES_ID, res.getId());
                    editor.apply();
                    callback.onResult(true);
                } else {
                    Log.i(LOG_TAG, "Provided wrong credentials. Access denied.");
                    callback.onResult(false);
                }
            }
        });
    }

    /*
        if no credentials in SharedPref or wrong credentials in SharedPref: FALSE
        else: TRUE
     */
    public void isAuthorised(final Callback<Boolean> callback) {
        Log.i(LOG_TAG, "Authorisation check started");

        if (mSettings.contains(SharedPreferencesNames.APP_PREFERENCES_LOGIN) && mSettings.contains(SharedPreferencesNames.APP_PREFERENCES_PASSWORD)) {
            Log.i(LOG_TAG, "Checking credentials in sharedPrefs");

            final String login = mSettings.getString(SharedPreferencesNames.APP_PREFERENCES_LOGIN, "");
            final String passwd = mSettings.getString(SharedPreferencesNames.APP_PREFERENCES_PASSWORD, "");


            final boolean[] isSuccess = new boolean[1];
            getUserWithCreds(login, passwd, new Callback<User>() {
                @Override
                public void onResult(User res) {
                    if (res == null || !res.isHasPermit()) {
                        clearCredentials();
                        callback.onResult(false);
                    }
                    else {
                        callback.onResult(true);
                    }
                }
            });



        } else {
            Log.i(LOG_TAG, "No credentials in SharedPref.");
            callback.onResult(false);
        }
    }

    private void clearCredentials() {
        SharedPreferences.Editor edit = mSettings.edit();
        edit.remove(SharedPreferencesNames.APP_PREFERENCES_LOGIN);
        edit.remove(SharedPreferencesNames.APP_PREFERENCES_ID);
        edit.remove(SharedPreferencesNames.APP_PREFERENCES_PASSWORD);
    }

    public void register(final User u, final Callback<User> callback) {
        api.registerUser(u).enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                mSettings.edit().putString(SharedPreferencesNames.APP_PREFERENCES_LOGIN, u.getLogin()).apply();
                mSettings.edit().putString(SharedPreferencesNames.APP_PREFERENCES_PASSWORD, u.getPassword()).apply();
                mSettings.edit().putInt(SharedPreferencesNames.APP_PREFERENCES_ID, response.body().getId()).apply();
                callback.onResult(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage(), t);
                callback.onResult(null);
            }
        });
    }

    /*
    Assisting functions
     */
    private void getUserWithCreds(String login, String passwd, final Callback<User> callback) {
        api.getUserWithCreds(login, passwd).enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User u = response.body();
                callback.onResult(u);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage(), t);
                callback.onResult(null);
            }
        });
    }

}