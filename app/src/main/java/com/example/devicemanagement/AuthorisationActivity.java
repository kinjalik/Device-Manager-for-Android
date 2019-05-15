package com.example.devicemanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.devicemanagement.Entities.User;

public class AuthorisationActivity extends AppCompatActivity {
    private static final String LOG_TAG = "AUTH_A";

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
                        Intent transit = new Intent(AuthorisationActivity.this, MainActivity.class);
                        startActivity(transit);
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
            Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            
            if (login.isEmpty() && email.isEmpty() && name.isEmpty() && surname.isEmpty() &&
            password.isEmpty() && rePassword.isEmpty()) {
                vb.vibrate(500);
                Toast.makeText(AuthorisationActivity.this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
                return;
            } else if (!password.equals(rePassword)) {
                vb.vibrate(500);
                Toast.makeText(AuthorisationActivity.this, "Passwords must be identical. ", Toast.LENGTH_SHORT).show();
                return;
            }

            User u = new User();
            u.setLogin(login)
                    .setEmail(email)
                    .setName(name)
                    .setSurname(name)
                    .setPassword(password);

            // ToDo: Сделать проверку данных на непустоту, а так же на правильность почты, надежность пароля

            authorisation.register(u, new Callback<User>() {
                @Override
                public void onResult(User res) {
                    if (res == null) {
                        Toast.makeText(AuthorisationActivity.this, "Something gone wrong...", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i(LOG_TAG, "Registered successfully. Quiting...");
                        Toast.makeText(AuthorisationActivity.this, "Registered succesfully.", Toast.LENGTH_SHORT).show();
                        Intent transit = new Intent(AuthorisationActivity.this, MainActivity.class);
                        startActivity(transit);
                    }
                }
            });
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
