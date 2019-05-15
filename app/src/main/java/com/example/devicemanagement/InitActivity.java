package com.example.devicemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class InitActivity extends AppCompatActivity {

    private String LOG_TAG = "INIT_A";

    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(LOG_TAG, "Checking authorisation...");
        Authorisation auth = Authorisation.getInstance(getApplicationContext());

        boolean isAuthed;
        auth.isAuthorised(new Callback<Boolean>() {
            @Override
            public void onResult(Boolean res) {
                    if (res) {
                        Log.i(LOG_TAG, "User authorised"); // ToDo: Add to log next screen
                        Toast.makeText(getApplicationContext(), "\"You are logged in. Other functions not implemented.\"", Toast.LENGTH_SHORT).show();
                        Intent transit = new Intent(InitActivity.this, MainActivity.class);
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
