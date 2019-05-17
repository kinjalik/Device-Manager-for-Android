package com.example.devicemanagement.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devicemanagement.DeviceListRecyclerAdapter;
import com.example.devicemanagement.Entities.Device;
import com.example.devicemanagement.Entities.DeviceProperty;
import com.example.devicemanagement.Network.NetworkService;
import com.example.devicemanagement.PropertyListAdapter;
import com.example.devicemanagement.R;
import com.google.gson.Gson;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceDetailFragment extends Fragment {
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_details, container, false);
    }

}
