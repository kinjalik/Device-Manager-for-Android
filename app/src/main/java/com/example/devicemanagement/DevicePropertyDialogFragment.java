package com.example.devicemanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.devicemanagement.Entities.DeviceProperty;
import com.google.gson.Gson;

public class DevicePropertyDialogFragment extends DialogFragment {
    public static String EDIT_MODE = "edit_mode";
    public static String DEVICE_PREF = "device";

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
            dp = new Gson().fromJson(getArguments().getString(DEVICE_PREF, ""), DeviceProperty.class);
        } else {
            windowTitle.setText("Add Device Property");
            dp = new DeviceProperty();
        }


        builder.setView(v);

        // ToDo: Инкапсюлировать строки в String-ресурсы
        builder.setPositiveButton("OK", mPosOnClickListener);
        builder.setNegativeButton("CANCEL", mNegOnClickListener);

        return builder.create();
    }


    private DialogInterface.OnClickListener mPosOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dp.name = formName.getText().toString();
            dp.value = formValue.getText().toString();

            Gson gson = new Gson();

            Bundle bndl = new Bundle();
            bndl.putString(DEVICE_PREF, gson.toJson(dp));
            cb.action(bndl);
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
        public void action(Bundle args);
    }
}
