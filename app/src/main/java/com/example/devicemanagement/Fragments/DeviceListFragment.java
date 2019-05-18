package com.example.devicemanagement.Fragments;


import android.app.Dialog;
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
import android.widget.ListView;
import android.widget.Toast;

import com.example.devicemanagement.Adapters.DeviceListRecyclerAdapter;
import com.example.devicemanagement.DevicePropertyDialogFragment;
import com.example.devicemanagement.Entities.Device;
import com.example.devicemanagement.Network.NetworkService;
import com.example.devicemanagement.R;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceListFragment extends Fragment {
    private static final String LOG_TAG = "DEVICE_F";
    private static final String APP_PREFERENCES = "settings";
    private static final String APP_PREFERENCES_ID = "id";

    Device[] devices;
    ListView lv_devices;

    public DeviceListFragment() {
        // Required empty public constructor
    }

    private boolean mTwoPane = false;
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

                        Device[] devices = response.body();
                        RecyclerView devicesList = Objects.requireNonNull(getActivity()).findViewById(R.id.devices_recycler_view);
                        devicesList.setLayoutManager(new LinearLayoutManager(getActivity()));
                        DeviceListRecyclerAdapter deviceAdapter = new DeviceListRecyclerAdapter(devices, mTwoPane);
                        devicesList.setAdapter(deviceAdapter);
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

    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_list, container, false);
    }


}
