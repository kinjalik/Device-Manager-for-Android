package com.example.devicemanagement.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devicemanagement.Activities.AuthorisationActivity;
import com.example.devicemanagement.Activities.MainActivity;
import com.example.devicemanagement.Callback;
import com.example.devicemanagement.Entities.User;
import com.example.devicemanagement.Network.Authorisation;
import com.example.devicemanagement.R;

public class RegisterFragment extends Fragment   {
    public static String LOG_TAG = "F_REGISTER";

    Authorisation authorisation;

    public static RegisterFragment getInstance() {
        return new RegisterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        Button regBtn = view.findViewById(R.id.s_register__form_submit);
        regBtn.setOnClickListener(mRegBtnClickListener);

        return view;
    }

    View.OnClickListener mRegBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText loginField = getActivity().findViewById(R.id.s_register__form_login);
            EditText emailField = getActivity().findViewById(R.id.s_register__form_email);
            EditText nameField = getActivity().findViewById(R.id.s_register__form_name);
            EditText surnameField = getActivity().findViewById(R.id.s_register__form_surname);
            EditText passwordField = getActivity().findViewById(R.id.s_register__form_password);
            EditText rePasswordField = getActivity().findViewById(R.id.s_register__form_repassword);

            String login = loginField.getText().toString();
            String email = emailField.getText().toString();
            String name = nameField.getText().toString();
            String surname = surnameField.getText().toString();
            String password = passwordField.getText().toString();
            String rePassword = rePasswordField.getText().toString();
            Vibrator vb = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

            String emailPattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

            if (login.isEmpty() && email.isEmpty() && name.isEmpty() && surname.isEmpty() &&
                    password.isEmpty() && rePassword.isEmpty()) {
                vb.vibrate(500);
                Snackbar.make(getActivity().findViewById(R.id.auth_root), "All fields must be filled", Snackbar.LENGTH_LONG).show();
                return;
            } else if (!email.matches(emailPattern)) {
                vb.vibrate(500);
                Snackbar.make(getActivity().findViewById(R.id.auth_root), "Incorrect email provided!", Snackbar.LENGTH_LONG).show();
                return;
            } else if (!password.equals(rePassword)) {
                vb.vibrate(500);
                Snackbar.make(getActivity().findViewById(R.id.auth_root), "Passwords must be identical", Snackbar.LENGTH_LONG).show();
                return;
            } else if (password.length() < 8) {
                vb.vibrate(500);
                Snackbar.make(getActivity().findViewById(R.id.auth_root), "Password must be at least 8 characters long", Snackbar.LENGTH_LONG).show();
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
                        Snackbar.make(getActivity().findViewById(R.id.auth_root), "Something gone wrong. Call developer IMMEDIATELY!!11!", Snackbar.LENGTH_LONG).show();
                    } else if (res.isSuccess) {
                        Log.i(LOG_TAG, "Registered successfully. Quiting...");
                        Snackbar.make(getActivity().findViewById(R.id.auth_root), "You successfully registered", Snackbar.LENGTH_LONG).show();
                        Intent transit = new Intent(getContext(), MainActivity.class);
                        startActivity(transit);
                    } else if (res.isConnected) {
                        Log.i(LOG_TAG, "Server sent anything missunderstandable. Check!");
                        Snackbar.make(getActivity().findViewById(R.id.auth_root), "Account with provided username or password already exists", Snackbar.LENGTH_LONG).show();
                    } else {
                        Log.i(LOG_TAG, "Server is down!");
                        Snackbar.make(getActivity().findViewById(R.id.auth_root), "Please, check your internet connection", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public RegisterFragment() {
        authorisation = Authorisation.getInstance(getContext());
    }

//    @Override
//    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        switch (v.getId()) {
//            case R.id.s_register__form_login:
//                getActivity().findViewById(R.id.s_register__form_email).requestFocus();
//                break;
//            case R.id.s_register__form_email:
//                getActivity().findViewById(R.id.s_register__form_name).requestFocus();
//                break;
//            case R.id.s_register__form_name:
//                getActivity().findViewById(R.id.s_register__form_surname).requestFocus();
//                break;
//            case R.id.s_register__form_surname:
//                getActivity().findViewById(R.id.s_register__form_password).requestFocus();
//                break;
//            case R.id.s_register__form_password:
//                getActivity().findViewById(R.id.s_register__form_repassword).requestFocus();
//                break;
//            case R.id.s_register__form_repassword:
//                getActivity().findViewById(R.id.s_register__form_submit).performClick();
//        }
//        return false;
//    }
}
