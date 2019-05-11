package com.example.devicemanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String LOG_TAG = "MAIN_A";

    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(LOG_TAG, "Checking authorisation...");
        Authorisation auth = Authorisation.getInstance(getApplicationContext());

        boolean isAuthed;
        auth.isAuthorised(new Authorisation.Callback<Boolean>() {
            @Override
            public void onResult(Boolean res) {
                    if (res) {
                        Log.i(LOG_TAG, "User authorised"); // ToDo: Add to log next screen
                        Toast.makeText(getApplicationContext(), "\"You are logged in. Other functions not implemented.\"", Toast.LENGTH_SHORT).show();
                        // ToDo: Make transition to next screen
                    } else {
                        Log.i(LOG_TAG, "Verification failed, calling auth screen");
                        Intent transition = new Intent(MainActivity.this, AuthorisationActivity.class);
                        startActivity(transition);
                    }
            }
        });


        setContentView(R.layout.bsod);
    }

}
