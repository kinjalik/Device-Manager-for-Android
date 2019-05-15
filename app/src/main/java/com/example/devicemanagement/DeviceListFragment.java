package com.example.devicemanagement;


import android.bluetooth.BluetoothClass;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.devicemanagement.Entities.Device;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceListFragment extends ListFragment {
    private static final String LOG_TAG = "DEVICE_F";
    private static final String APP_PREFERENCES = "settings";
    private static final String APP_PREFERENCES_ID = "id";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int userId = Objects.requireNonNull(getActivity())
                .getSharedPreferences(APP_PREFERENCES, 0)
                .getInt(APP_PREFERENCES_ID, 0);

        NetworkService.getInstance()
                .getApi()
                .getUsersDevices(userId)
                .enqueue(new Callback<Device[]>() {
                    @Override
                    public void onResponse(Call<Device[]> call, Response<Device[]> response) {
                        Log.i(LOG_TAG, "Device list downloaded");
                        Device[] list = response.body();
                        ListAdapter adapter = new DeviceAdapter(getActivity(), list);
                        setListAdapter(adapter);
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
}
