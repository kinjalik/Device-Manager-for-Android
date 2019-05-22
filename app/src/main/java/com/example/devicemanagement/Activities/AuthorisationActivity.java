package com.example.devicemanagement.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.devicemanagement.Adapters.AuthTabsAdapter;
import com.example.devicemanagement.Network.Authorisation;
import com.example.devicemanagement.Callback;
import com.example.devicemanagement.Entities.User;
import com.example.devicemanagement.Network.NetworkService;
import com.example.devicemanagement.R;
import com.example.devicemanagement.SharedPreferencesNames;

import java.util.Objects;

public class AuthorisationActivity extends AppCompatActivity {
    private static final String LOG_TAG = "AUTH_A";

    private Authorisation authorisation ;
    private SharedPreferences mSettings;

    private boolean loginScreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authorisation = Authorisation.getInstance(this);
        mSettings = getSharedPreferences(SharedPreferencesNames.APP_PREFERENCES, Context.MODE_PRIVATE);

        /*
        If it is first start of app:
        - Display registration screen
        - Make record in sharedPreferences about first start
        else
        - Display login screen
         */
        if (mSettings.contains(SharedPreferencesNames.APP_PREFERENCES_INITED)) {
            loginScreen = true;
        } else {
            SharedPreferences.Editor editor = mSettings.edit();
            loginScreen = false;
            editor.putBoolean(SharedPreferencesNames.APP_PREFERENCES_INITED, true);
            editor.apply();
        }

        setContentView(R.layout.auth_screen);
        ViewPager vp = findViewById(R.id.s_auth__view_pager);
        vp.setAdapter(new AuthTabsAdapter(getSupportFragmentManager(), AuthorisationActivity.this));

        TabLayout tl = findViewById(R.id.s_auth__tabs);
        tl.setupWithViewPager(vp);

        if (loginScreen) {
            Objects.requireNonNull(tl.getTabAt(0)).select();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    // Assisting code, not contains business-logic

    // Blocks back btn, preventing exiting of login screen
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
