package com.example.devicemanagement.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.devicemanagement.Network.Authorisation;
import com.example.devicemanagement.Callback;
import com.example.devicemanagement.Network.NetworkService;
import com.example.devicemanagement.R;
import com.example.devicemanagement.SharedPreferencesNames;

public class InitActivity extends AppCompatActivity {

    private String LOG_TAG = "INIT_A";

    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettings = getSharedPreferences(SharedPreferencesNames.APP_PREFERENCES, 0);

        Log.i(LOG_TAG, "Checking authorisation...");
        Authorisation auth = Authorisation.getInstance(getApplicationContext());

        boolean isAuthed;
        auth.isAuthorised(new Callback<Boolean>() {
            @Override
            public void onResult(Boolean res) {
                    if (res) {
                        Log.i(LOG_TAG, "User authorised, moving to MainActivity");
                        Intent transit = new Intent(InitActivity.this, MainActivity.class);

                        String passwd = mSettings.getString(SharedPreferencesNames.APP_PREFERENCES_PASSWORD, "");
                        NetworkService.setPassword(passwd);

                        startActivity(transit);
                    } else {
                        Log.i(LOG_TAG, "Verification failed, calling auth screen");
                        Intent transit = new Intent(InitActivity.this, AuthorisationActivity.class);
                        startActivity(transit);
                    }
            }
        });

    }

}
