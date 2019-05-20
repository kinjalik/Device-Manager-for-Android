package com.example.devicemanagement.Fragments;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.devicemanagement.Adapters.DeviceListRecyclerAdapter;
import com.example.devicemanagement.Entities.Device;
import com.example.devicemanagement.Network.NetworkService;
import com.example.devicemanagement.R;
import com.example.devicemanagement.SharedPreferencesNames;
import com.google.gson.Gson;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceListFragment extends Fragment {
    private static final String LOG_TAG = "DEVICE_F";

    Device[] devices;

    public DeviceListFragment() {
        // Required empty public constructor
    }

    private boolean mTwoPane = false;
    private int userId;

    private RecyclerView devicesList;
    private DeviceListRecyclerAdapter deviceAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.device_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        FloatingActionButton fab = getActivity().findViewById(R.id.s_devices_list__add_device);
        fab.setOnClickListener(mFabOnClickListener);


        userId = Objects.requireNonNull(getActivity())
                .getSharedPreferences(SharedPreferencesNames.APP_PREFERENCES, 0)
                .getInt(SharedPreferencesNames.APP_PREFERENCES_ID, 0);

        NetworkService.getInstance()
                .getApi()
                .getUsersDevices(userId)
                .enqueue(new Callback<Device[]>() {
                    @Override
                    public void onResponse(Call<Device[]> call, Response<Device[]> response) {
                        Log.i(LOG_TAG, "Device list downloaded");

                        devices = response.body();
                        devicesList = Objects.requireNonNull(getActivity()).findViewById(R.id.devices_recycler_view);
                        devicesList.setLayoutManager(new LinearLayoutManager(getActivity()));
                        deviceAdapter = new DeviceListRecyclerAdapter(response.body(), mTwoPane);
                        devicesList.setAdapter(deviceAdapter);
                        deviceAdapter.setmOnLongClickListener(mAdapterOnLongClickListener);

                    }

                    @Override
                    public void onFailure(Call<Device[]> call, Throwable t) {
                        Log.i(LOG_TAG, "Failed to download device list", t);
                        Toast.makeText(getActivity(),
                                "Something got wrong while downloading your devices.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private View.OnLongClickListener mAdapterOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            Vibrator vibro = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibro.vibrate(VibrationEffect.createOneShot(100L, 50));
            } else {
                vibro.vibrate(100);
            }

            int pos = (int) v.getTag();
            Toast.makeText(getContext(), "" + pos, Toast.LENGTH_SHORT).show();

            final Device device = deviceAdapter.getItemByPos(pos);
            Bundle bndl = new Bundle();
            bndl.putBoolean(DeviceDialogFragment.EDIT_MODE, true);
            bndl.putString(DeviceDialogFragment.TRANSPORT_PREF, new Gson().toJson(device));
            DeviceDialogFragment dialog = new DeviceDialogFragment();
            dialog.setArguments(bndl);

            dialog.setCallback(new DeviceDialogFragment.Callback() {
                @Override
                public void action(Bundle args) {
                    if (args == null) {
                        Log.i(LOG_TAG, "Sending property for deletion");
                        deleteItem(device);
                        return;
                    }
                    Log.i(LOG_TAG, "Sending property for edition");
                    editItem(device);


                }
            });
            dialog.show(getActivity().getSupportFragmentManager(), "DevicePropertyDialogFragment");
            return true;
        }
    };

    private void deleteItem(Device device) {
        NetworkService.getInstance().getApi().removeUserDevice(device.ownerId, device.id)
                .enqueue(new Callback<Device>() {
                    @Override
                    public void onResponse(Call<Device> call, Response<Device> response) {
                        Toast.makeText(getContext(), "Item deleted succesfully.", Toast.LENGTH_SHORT).show();
                        updateList();
                    }

                    @Override
                    public void onFailure(Call<Device> call, Throwable t) {

                    }
                });
    }

    private void editItem(Device device) {
        Toast.makeText(getContext(), "Not implemented yet.", Toast.LENGTH_SHORT).show();
        // ToDo: Реализовать изменение. После сервера
    }

    private void updateList() {
        NetworkService.getInstance()
                .getApi()
                .getUsersDevices(userId)
                .enqueue(new Callback<Device[]>() {
                    @Override
                    public void onResponse(Call<Device[]> call, Response<Device[]> response) {
                        Log.i(LOG_TAG, "Device list downloaded");
                        deviceAdapter.setItems(response.body());
                    }

                    @Override
                    public void onFailure(Call<Device[]> call, Throwable t) {
                        Log.i(LOG_TAG, "Failed to download device list", t);
                        Toast.makeText(getActivity(),
                                "Something got wrong while downloading your devices.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private View.OnClickListener mFabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            DeviceDialogFragment dialog = new DeviceDialogFragment();

            Bundle bundle = new Bundle();
            bundle.putBoolean(DeviceDialogFragment.EDIT_MODE, false);
            dialog.setArguments(bundle);

            dialog.setCallback(new DeviceDialogFragment.Callback() {
                @Override
                public void action(Bundle args) {
                    Log.i(LOG_TAG, "Got new device!");
                    String dpJson = args.getString(DeviceDialogFragment.TRANSPORT_PREF, "");
                    if (dpJson.equals("")) {
                        Toast.makeText(getContext(), "All fields must be filled!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Device dp = new Gson().fromJson(dpJson, Device.class);
                    int userId = getActivity().getSharedPreferences(SharedPreferencesNames.APP_PREFERENCES, 0).getInt(SharedPreferencesNames.APP_PREFERENCES_ID, 0);
                    dp.setOwnerId(userId);
                    Log.i(LOG_TAG, String.format("Device name: %1$s, description: %2$s", dp.name, dp.description));
                    NetworkService.getInstance().getApi().addUserDevice(userId, dp).enqueue(new Callback<Device>() {
                        @Override
                        public void onResponse(Call<Device> call, Response<Device> response) {
                            if (response.code() != 200){
                                Log.i(LOG_TAG, "Device  sent, but response is incorrect");
                                Toast.makeText(getContext(), "Failed to create new Device Try again later.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Toast.makeText(getContext(), "Device successfully created", Toast.LENGTH_SHORT).show();
                            Log.i(LOG_TAG, "Device successfully sent to server");
                            updateList();
                        }

                        @Override
                        public void onFailure(Call<Device> call, Throwable t) {
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
        return inflater.inflate(R.layout.fragment_device_list, container, false);
    }


}
