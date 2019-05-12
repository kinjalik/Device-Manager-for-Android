package com.example.devicemanagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AuthorisationActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_INITED = "inited";

    private Authorisation authorisation ;
    private SharedPreferences mSettings;

    private boolean loginScreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authorisation = Authorisation.getInstance(this);
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

    /*
        BUTTONS LOGIC
     */

    // Login button
    View.OnClickListener loginButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText loginField = findViewById(R.id.s_login__form__login);
            EditText passwdField = findViewById(R.id.s_login__form__password);
            String login = loginField.getText().toString();
            String passwd = passwdField.getText().toString();

            authorisation.authorise(login, passwd, new Callback<Boolean>() {
                @Override
                public void onResult(Boolean res) {
                    if (res) {
                        Toast.makeText(AuthorisationActivity.this, "You successfully authorised", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AuthorisationActivity.this, "Credentials are wrong. Try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };

    // Register button
    View.OnClickListener registerButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText loginField = findViewById(R.id.s_register__form_login);
            EditText emailField = findViewById(R.id.s_register__form_email);
            EditText nameField = findViewById(R.id.s_register__form_name);
            EditText surnameField = findViewById(R.id.s_register__form_surname);
            EditText passwordField = findViewById(R.id.s_register__form_password);
            EditText rePasswordField = findViewById(R.id.s_register__form_repassword);
            
            String login = loginField.getText().toString(); 
            String email = emailField.getText().toString();
            String name = nameField.getText().toString();
            String surname = surnameField.getText().toString();
            String password = passwordField.getText().toString();
            String rePassword = rePasswordField.getText().toString();
            
            if (!password.equals(rePassword)) {
                Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vb.vibrate(1000);
                Toast.makeText(AuthorisationActivity.this, "Passwords must be identical. ", Toast.LENGTH_SHORT).show();
            }
            // ToDo: Проверка на пустоту полей
            // ToDo: Доделать логику регшистрации
        }
    };

    // Switch screen button: login <-> register
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

            Button registerBtn = findViewById(R.id.s_register__form_submit);
            registerBtn.setOnClickListener(registerButtonListener);

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
        moveTaskToBack(true);
    }
}
