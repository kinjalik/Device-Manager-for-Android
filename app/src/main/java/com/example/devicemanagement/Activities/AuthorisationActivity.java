package com.example.devicemanagement.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.devicemanagement.Network.Authorisation;
import com.example.devicemanagement.Callback;
import com.example.devicemanagement.Entities.User;
import com.example.devicemanagement.Network.NetworkService;
import com.example.devicemanagement.R;
import com.example.devicemanagement.SharedPreferencesNames;

import javax.xml.transform.Result;

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
            loginScreen = false;
        } else {
            SharedPreferences.Editor editor = mSettings.edit();
            loginScreen = true;
            editor.putBoolean(SharedPreferencesNames.APP_PREFERENCES_INITED, true);
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
            final String passwd = passwdField.getText().toString();

            authorisation.authorise(login, passwd, new Callback<Boolean>() {
                @Override
                public void onResult(Boolean res) {
                    if (res) {
                        Toast.makeText(AuthorisationActivity.this, "You successfully authorised", Toast.LENGTH_SHORT).show();
                        Intent transit = new Intent(AuthorisationActivity.this, MainActivity.class);
                        NetworkService.setPassword(passwd);
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

            String emailPattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

            if (login.isEmpty() && email.isEmpty() && name.isEmpty() && surname.isEmpty() &&
                    password.isEmpty() && rePassword.isEmpty()) {
                vb.vibrate(500);
                Snackbar.make(findViewById(R.id.auth_root), "All fields must be filled", Snackbar.LENGTH_LONG).show();
                return;
            } else if (!email.matches(emailPattern)) {
                vb.vibrate(500);
                Snackbar.make(findViewById(R.id.auth_root), "Incorrect email provided!", Snackbar.LENGTH_LONG).show();
                return;
            } else if (!password.equals(rePassword)) {
                vb.vibrate(500);
                Snackbar.make(findViewById(R.id.auth_root), "Passwords must be identical", Snackbar.LENGTH_LONG).show();
                return;
            } else if (password.length() < 8) {
                vb.vibrate(500);
                Snackbar.make(findViewById(R.id.auth_root), "Password must be at least 8 characters long", Snackbar.LENGTH_LONG).show();
                return;
            }

            User u = new User();
            u.setLogin(login)
                    .setEmail(email)
                    .setName(name)
                    .setSurname(name)
                    .setPassword(password);

            authorisation.register(u, new Callback<Authorisation.RegisterResult>() {
                @Override
                public void onResult(Authorisation.RegisterResult res) {
                    if (res == null) {
                        Toast.makeText(AuthorisationActivity.this, "Something gone wrong. Please, call the developer!", Toast.LENGTH_SHORT).show();
                    } else if (res.isSuccess) {
                        Log.i(LOG_TAG, "Registered successfully. Quiting...");
                        Toast.makeText(AuthorisationActivity.this, "Registered succesfully.", Toast.LENGTH_SHORT).show();
                        Intent transit = new Intent(AuthorisationActivity.this, MainActivity.class);
                        startActivity(transit);
                    } else if (res.isConnected) {
                        Log.i(LOG_TAG, "Server sent anything missunderstandable. Check!");
                        Snackbar.make(findViewById(R.id.auth_root), "Account with provided username or password already exists", Snackbar.LENGTH_LONG).show();

                    } else {
                        Log.i(LOG_TAG, "Server is down!");
                        Snackbar.make(findViewById(R.id.auth_root), "Please, check your internet connection", Snackbar.LENGTH_LONG).show();

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
