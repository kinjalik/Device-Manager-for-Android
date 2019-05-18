package com.example.devicemanagement.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devicemanagement.Activities.DeviceDetailActivity;
import com.example.devicemanagement.Entities.Device;
import com.example.devicemanagement.R;
import com.google.gson.Gson;

public class DeviceListRecyclerAdapter extends RecyclerView.Adapter<DeviceListRecyclerAdapter.ViewHolder> {
    private final static String LOG_TAG = "DEVICE_RECYCLER_ADAPTER";
    public static String DEVICE_DATA = "device";

    private boolean mTwoPane;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int devicePos = (int) v.getTag();
            Device device = devices[devicePos];
            Log.i(LOG_TAG, "Click on Device with ID: " + device.getId());
            if (mTwoPane) {
                Snackbar.make(v, "Two Panel Mode - To implement", 2000);
                // ToDo: Реализовать вывод на второй экран для планшета
            } else {
                Context cntx = v.getContext();

                Gson gson = new Gson();

                Intent intent = new Intent(cntx, DeviceDetailActivity.class);
                intent.putExtra(DEVICE_DATA, gson.toJson(device));
                cntx.startActivity(intent);
            }
        }
    };

    private Device[] devices;
    public DeviceListRecyclerAdapter(Device[] devs, boolean twoPane) {
        devices = devs;
        mTwoPane = twoPane;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View deviceView = inflater.inflate(R.layout.fragment_device_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(deviceView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Log.i(LOG_TAG, "Creating new item");
        Device device = devices[position];
        TextView name = viewHolder.deviceName;
        name.setText(device.getName());
        TextView description = viewHolder.deviceDescription;
        description.setText(device.getDescription());

        viewHolder.itemView.setTag(position);
        viewHolder.itemView.setOnClickListener(mOnClickListener);

    }

    @Override
    public int getItemCount() {
        if (devices == null)
            return 0;
        return devices.length;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView deviceName;
        private TextView deviceDescription;


        public ViewHolder(View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.device_list__name);
            deviceDescription = itemView.findViewById(R.id.device_list__description);

        }

    }
}
