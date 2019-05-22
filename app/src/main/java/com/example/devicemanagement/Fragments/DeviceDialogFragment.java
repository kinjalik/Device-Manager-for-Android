package com.example.devicemanagement.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.devicemanagement.Entities.Device;
import com.example.devicemanagement.R;
import com.google.gson.Gson;

import java.util.Objects;

public class DeviceDialogFragment extends DialogFragment {
    public static String EDIT_MODE = "edit_mode";
    public static String TRANSPORT_PREF = "device";

    private Device dp;

    private EditText formName;
    private EditText formDescription;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();

        @SuppressLint("InflateParams")
        View v = inflater.inflate(R.layout.dialog_create_device, null);


        TextView windowTitle = v.findViewById(R.id.d_create_device__title);
        formName = v.findViewById(R.id.d_create_device__name);
        formDescription = v.findViewById(R.id.d_create_device__description);

        boolean isEditMode = getArguments() != null && getArguments().getBoolean(EDIT_MODE);

        String title;
        if (isEditMode) {
            title = getActivity().getString(R.string.d_create_device__title__edit);
            dp = new Gson().fromJson(getArguments().getString(TRANSPORT_PREF, ""), Device.class);

            formName.setText(dp.name);
            formDescription.setText(dp.description);
        } else {
            title = getActivity().getString(R.string.d_create_device__title_add);
            dp = new Device();
        }
        windowTitle.setText(title);


        builder.setView(v);

        builder.setPositiveButton(getActivity().getString(R.string.d_create_device__button__ok), mPosOnClickListener);
        builder.setNegativeButton(getActivity().getString(R.string.d_create_device__button__cancel), mNegOnClickListener);

        if (isEditMode)
            builder.setNeutralButton(getActivity().getString(R.string.d_create_device__button__remove), deleteOnClickListener);

        return builder.create();
    }


    private DialogInterface.OnClickListener mPosOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dp.name = formName.getText().toString();
            dp.description = formDescription.getText().toString();

            if (!dp.name.equals("") && !dp.description.equals("")){
                Gson gson = new Gson();

                Bundle bndl = new Bundle();
                bndl.putString(TRANSPORT_PREF, gson.toJson(dp));
                cb.action(bndl);
            } else {
                dp = null;
                Bundle bndl = new Bundle();
                bndl.putString(TRANSPORT_PREF, "");
                cb.action(bndl);
            }
        }
    };

    private DialogInterface.OnClickListener deleteOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            cb.action(null);
        }
    };

    private DialogInterface.OnClickListener mNegOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    };

    private Callback cb;
    public void setCallback(Callback cb) {
        this.cb = cb;
    }

    public interface Callback {
        void action(Bundle args);
    }
}
