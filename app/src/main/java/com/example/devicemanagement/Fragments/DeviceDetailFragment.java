package com.example.devicemanagement.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devicemanagement.Entities.Device;
import com.example.devicemanagement.Entities.DeviceProperty;
import com.example.devicemanagement.Network.NetworkService;
import com.example.devicemanagement.Adapters.PropertyListAdapter;
import com.example.devicemanagement.R;
import com.example.devicemanagement.RecyclerItemClickListener;
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

    private RecyclerView propsList;
    private PropertyListAdapter propAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView description = getActivity().findViewById(R.id.f_device_details__description);
        description.setText(device.getDescription());

        FloatingActionButton fab = getActivity().findViewById(R.id.s_device_props__add_prop);
        fab.setOnClickListener(mFabOnClickListener);
        NetworkService.getInstance().getApi().getUserDeviceProps(device.ownerId, device.id)
                .enqueue(new Callback<DeviceProperty[]>() {
                    @Override
                    public void onResponse(Call<DeviceProperty[]> call, Response<DeviceProperty[]> response) {
                        Activity a = getActivity();
                        if (a == null) {
                            return;
                        }
                        propsList = a.findViewById(R.id.device_props_recycler_view);
                        a = null;

                        propsList.setLayoutManager(new LinearLayoutManager(getActivity()));
                        propAdapter = new PropertyListAdapter(response.body());
                        propsList.setAdapter(propAdapter);

                        propsList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), propsList, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int pos) {
                                final DeviceProperty dp = propAdapter.getItemById(pos);

                                Bundle bndl = new Bundle();
                                bndl.putBoolean(DevicePropertyDialogFragment.EDIT_MODE, true);
                                bndl.putString(DevicePropertyDialogFragment.TRANSPORT_PREF, new Gson().toJson(dp));
                                DevicePropertyDialogFragment dialog = new DevicePropertyDialogFragment();
                                dialog.setArguments(bndl);

                                dialog.setCallback(new DevicePropertyDialogFragment.Callback() {
                                    @Override
                                    public void action(Bundle args) {
                                        if (args == null) {
                                            Log.i(LOG_TAG, "Sending property for deletion");
                                            deleteItem(dp);
                                            return;
                                        }
                                        Log.i(LOG_TAG, "Sending property for edition");
                                        editItem(dp);


                                    }
                                });
                                dialog.show(getActivity().getSupportFragmentManager(), "DevicePropertyDialogFragment");
                            }

                            @Override
                            public void onLongItemClick(View v, int pos) {
                                onItemClick(v, pos);
                            }
                        }));
                    }

                    @Override
                    public void onFailure(Call<DeviceProperty[]> call, Throwable t) {

                    }
                });

    }

    // ToDo: Сделать обновление списка свойств через штатные средства RecyclerView, а не путем полной перерисовки списка
    private void updateList() {
        NetworkService.getInstance().getApi().getUserDeviceProps(device.ownerId, device.id)
                .enqueue(new Callback<DeviceProperty[]>() {
                    @Override
                    public void onResponse(Call<DeviceProperty[]> call, Response<DeviceProperty[]> response) {
                        propAdapter.setProps(response.body());
                    }

                    @Override
                    public void onFailure(Call<DeviceProperty[]> call, Throwable t) {

                    }
                });
    }

    private void deleteItem(DeviceProperty dp) {
        NetworkService.getInstance().getApi().removeUserDeviceProp(device.ownerId, device.id, dp.id).enqueue(new Callback<DeviceProperty>() {
            @Override
            public void onResponse(Call<DeviceProperty> call, Response<DeviceProperty> response) {
                Toast.makeText(getContext(), "Property successfully deleted.", Toast.LENGTH_SHORT).show();
                updateList();
            }

            @Override
            public void onFailure(Call<DeviceProperty> call, Throwable t) {
                Toast.makeText(getContext(), "Error while deleting.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editItem(DeviceProperty dp) {
        Toast.makeText(getContext(), "Not implemented yet.", Toast.LENGTH_SHORT).show();
        // ToDo: Запрос на изменение
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
                    String dpJson = args.getString(DevicePropertyDialogFragment.TRANSPORT_PREF, "");
                    if (dpJson.equals("")) {
                        Toast.makeText(getContext(), "All fields must be filled!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DeviceProperty dp = new Gson().fromJson(dpJson, DeviceProperty.class);
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
