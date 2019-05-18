package com.example.devicemanagement.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devicemanagement.DevicePropertyDialogFragment;
import com.example.devicemanagement.Entities.Device;
import com.example.devicemanagement.Entities.DeviceProperty;
import com.example.devicemanagement.Network.Authorisation;
import com.example.devicemanagement.Network.NetworkService;
import com.example.devicemanagement.Adapters.PropertyListAdapter;
import com.example.devicemanagement.R;
import com.google.gson.Gson;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceDetailFragment extends Fragment {
    public static String LOG_TAG = "F_DEVICE_DETAIL";

    public static String ARGS_DEVICE = "device";

    public DeviceDetailFragment() {
        // Required empty public constructor
    }


    private static Gson gson = new Gson();
    public static DeviceDetailFragment newInstance(Device d) {
        DeviceDetailFragment instance = new DeviceDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_DEVICE, gson.toJson(d));
        instance.setArguments(args);
        return instance;
    }

    private Device device;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String deviceJson = getArguments().getString(ARGS_DEVICE, "");
        this.device = gson.fromJson(deviceJson, Device.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView description = getActivity().findViewById(R.id.f_device_details__description);
        description.setText(device.getDescription());

        FloatingActionButton fab = getActivity().findViewById(R.id.s_device_props__add_prop);
        fab.setOnClickListener(mFabOnClickListener);

        updateList();

    }

    private void updateList() {
        NetworkService.getInstance().getApi().getUserDeviceProps(device.ownerId, device.id)
                .enqueue(new Callback<DeviceProperty[]>() {
                    @Override
                    public void onResponse(Call<DeviceProperty[]> call, Response<DeviceProperty[]> response) {
                        RecyclerView propsList = Objects.requireNonNull(getActivity()).findViewById(R.id.device_props_recycler_view);
                        propsList.setLayoutManager(new LinearLayoutManager(getActivity()));
                        PropertyListAdapter propAdapter = new PropertyListAdapter(response.body());
                        propsList.setAdapter(propAdapter);
                    }

                    @Override
                    public void onFailure(Call<DeviceProperty[]> call, Throwable t) {

                    }
                });
    }

    private View.OnClickListener mFabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DevicePropertyDialogFragment dialog = new DevicePropertyDialogFragment();

            Bundle bundle = new Bundle();
            bundle.putBoolean(DevicePropertyDialogFragment.EDIT_MODE, false);
            dialog.setArguments(bundle);

            dialog.setCallback(new DevicePropertyDialogFragment.Callback() {
                @Override
                public void action(Bundle args) {
                    Log.i(LOG_TAG, "Got new device!");
                    DeviceProperty dp = new Gson().fromJson(args.getString(DevicePropertyDialogFragment.DEVICE_PREF, ""), DeviceProperty.class);
                    Log.i(LOG_TAG, String.format("Property name: %1$s, value: %2$s", dp.name, dp.value));
                    int userId = device.ownerId;
                    int deviceId = device.id;
                    NetworkService.getInstance().getApi().addUserDeviceProp(userId, deviceId, dp).enqueue(new Callback<DeviceProperty>() {
                        @Override
                        public void onResponse(Call<DeviceProperty> call, Response<DeviceProperty> response) {
                            if (response.code() != 200){
                                Log.i(LOG_TAG, "Device Property sent, but response is incorrect");
                                Toast.makeText(getContext(), "Failed to create new Property. Try again later.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Toast.makeText(getContext(), "Device Property successfully created", Toast.LENGTH_SHORT).show();
                            Log.i(LOG_TAG, "Device Property successfully sent to server");
                            updateList();
                        }

                        @Override
                        public void onFailure(Call<DeviceProperty> call, Throwable t) {
                            Log.i(LOG_TAG, t.getMessage(), t);
                        }
                    });

                }
            });
            dialog.show(getActivity().getSupportFragmentManager(), "DevicePropertyDialogFragment");
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_details, container, false);
    }

}
