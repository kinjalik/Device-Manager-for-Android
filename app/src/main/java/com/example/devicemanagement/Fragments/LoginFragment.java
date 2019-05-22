package com.example.devicemanagement.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.devicemanagement.Activities.AuthorisationActivity;
import com.example.devicemanagement.Activities.MainActivity;
import com.example.devicemanagement.Callback;
import com.example.devicemanagement.Network.Authorisation;
import com.example.devicemanagement.Network.NetworkService;
import com.example.devicemanagement.R;
import com.example.devicemanagement.SharedPreferencesNames;

public class LoginFragment extends Fragment {
    public static String LOG_TAG = "F_LOGIN";
    private Authorisation authorisation;

    public static LoginFragment getInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Button loginBtn = view.findViewById(R.id.s_login__sign_in);
        if (loginBtn != null)
            loginBtn.setOnClickListener(mLoginBtnClickListener);

        return view;
    }


    View.OnClickListener mLoginBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText loginField = getActivity().findViewById(R.id.s_login__form__login);
            EditText passwdField = getActivity().findViewById(R.id.s_login__form__password);
            String login = loginField.getText().toString();
            final String passwd = passwdField.getText().toString();

            authorisation.authorise(login, passwd, new Callback<Boolean>() {
                @Override
                public void onResult(Boolean res) {
                    if (res) {
                        Snackbar.make(getActivity().findViewById(R.id.auth_root), "You successfully authorised", Snackbar.LENGTH_LONG).show();
                        Intent transit = new Intent(getContext(), MainActivity.class);
                        startActivity(transit);
                    } else {
                        Snackbar.make(getActivity().findViewById(R.id.auth_root), "Wrong credentials provided.", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public LoginFragment() {
        authorisation = Authorisation.getInstance(getContext());
    }

}
