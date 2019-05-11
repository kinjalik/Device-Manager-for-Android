package com.example.devicemanagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AuthorisationActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_INITED = "inited";

    private SharedPreferences mSettings;

    private boolean loginScreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        /*
        If it is first start of app:
        - Display registration screen
        - Make record in sharedPreferences about first start
        else
        - Display login screen
         */
        if (mSettings.contains(APP_PREFERENCES_INITED)) {
            loginScreen = false;
        } else {
            SharedPreferences.Editor editor = mSettings.edit();
            loginScreen = true;
            editor.putBoolean(APP_PREFERENCES_INITED, true);
            editor.apply();
        }
        changeScreen();
    }

    // Login button logic
    View.OnClickListener loginButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText loginField = findViewById(R.id.s_login__form__login);
            EditText passwdField = findViewById(R.id.s_login__form__password);
            String login = loginField.getText().toString();
            String passwd = passwdField.getText().toString();


        }
    };

    // Switch screens: login <-> register
    View.OnClickListener changeScreenListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeScreen();
        }
    };

    private void changeScreen() {
        if (loginScreen) {
            setContentView(R.layout.register_screen);

            Button switchToLogin = findViewById(R.id.s_register__change_screen);
            switchToLogin.setOnClickListener(changeScreenListener);

            // ToDo: Register button logic
        } else {
            setContentView(R.layout.login_screen);

            Button switchToRegister = findViewById(R.id.s_login__change_screen);
            switchToRegister.setOnClickListener(changeScreenListener);

            Button loginBtn = findViewById(R.id.s_login__sign_in);
            loginBtn.setOnClickListener(loginButtonListener);

        }
        loginScreen = !loginScreen;
    }


    // Assisting code, not contains business-logic

    // Blocks back btn, preventing exiting of login screen
    @Override
    public void onBackPressed() {

    }
}
