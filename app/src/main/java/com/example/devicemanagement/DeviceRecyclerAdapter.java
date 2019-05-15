package com.example.devicemanagement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devicemanagement.Entities.Device;

import java.util.List;

public class DeviceRecyclerAdapter extends RecyclerView.Adapter<DeviceRecyclerAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView deviceName;
        private TextView deviceDescription;
        private final boolean mTwoPane;

        public ViewHolder(View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.device_list__name);
            deviceDescription = itemView.findViewById(R.id.device_list__description);

        }

    }

    private Device[] devices;
    public DeviceRecyclerAdapter(Device[] devs) {
        devices = devs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View deviceView = inflater.inflate(R.layout.device_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(deviceView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Device device = devices[position];
        TextView name = viewHolder.deviceName;
        name.setText(device.getName());
        TextView description = viewHolder.deviceDescription;
        description.setText(device.getDescription());

    }

    @Override
    public int getItemCount() {
        if (devices == null)
            return 0;
        return devices.length;
    }
}
