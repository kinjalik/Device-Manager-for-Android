package com.example.devicemanagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AuthorisationActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_INITED = "inited";

    private SharedPreferences mSettings;

    private boolean loginScreen;
    private Button switchLogin;
    private Button switchRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if (mSettings.contains(APP_PREFERENCES_INITED)) {
            loginScreen = true;
        } else {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean(APP_PREFERENCES_INITED, true);
            editor.apply();


            loginScreen = false;
        }

        changeScreen();


    }

    @Override
    public void onBackPressed() {

    }


    View.OnClickListener changeScreenListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeScreen();
        }
    };

    private void changeScreen() {
        if (loginScreen) {
            setContentView(R.layout.register_screen);

            switchRegister = findViewById(R.id.s_register__change_screen);
            switchRegister.setOnClickListener(changeScreenListener);
        } else {
            setContentView(R.layout.login_screen);

            switchLogin = findViewById(R.id.s_login__change_screen);
            switchLogin.setOnClickListener(changeScreenListener);
        }
        loginScreen = !loginScreen;
    }


}
