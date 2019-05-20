package com.example.devicemanagement.Fragments;

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

import com.example.devicemanagement.Entities.DeviceProperty;
import com.example.devicemanagement.R;
import com.google.gson.Gson;

public class DevicePropertyDialogFragment extends DialogFragment {
    public static String EDIT_MODE = "edit_mode";
    public static String TRANSPORT_PREF = "device_property";

    private DeviceProperty dp;

    private EditText formName;
    private EditText formValue;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_create_device_prop, null);


        TextView windowTitle = v.findViewById(R.id.d_create_device_prop__title);
        formName = v.findViewById(R.id.d_create_device_prop__name);
        formValue = v.findViewById(R.id.d_create_device_prop__value);

        boolean isEditMode = getArguments() != null && getArguments().getBoolean(EDIT_MODE);

        // ToDo: Инкапсюлировать строки в String-ресурсы
        if (isEditMode) {
            windowTitle.setText("Edit Device Property");
            // ToDo: Логика редактирования свойств. ПОСЛЕ реализации на сервере.
            dp = new Gson().fromJson(getArguments().getString(TRANSPORT_PREF, ""), DeviceProperty.class);

            formName.setText(dp.name);
            formValue.setText(dp.value);
        } else {
            windowTitle.setText("Add Device Property");
            dp = new DeviceProperty();
        }


        builder.setView(v);

        // ToDo: Инкапсюлировать строки в String-ресурсы
        builder.setPositiveButton("OK", submitOnClickListener);
        builder.setNegativeButton("CANCEL", cancelOnCLickListener);

        if (isEditMode)
            builder.setNeutralButton("DELETE IT", deleteOnClickListener);


        return builder.create();
    }


    private DialogInterface.OnClickListener submitOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dp.name = formName.getText().toString();
            dp.value = formValue.getText().toString();

            if (!dp.name.equals("") && !dp.value.equals("")){
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

    private DialogInterface.OnClickListener cancelOnCLickListener = new DialogInterface.OnClickListener() {
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
        public void action(Bundle args);
    }
}
