package com.example.devicemanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.devicemanagement.Entities.Device;

import org.jetbrains.annotations.NotNull;

public class DeviceAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater layoutInflater;
    Device[] items;

    DeviceAdapter(Context context, Device[] items) {
        ctx = context;
        this.items = items;
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Device getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = layoutInflater.inflate(R.layout.device_list_item, parent, false);
        Device d = getItem(position);
        ((TextView) view.findViewById(R.id.device_list__name)).setText(d.getName());
        ((TextView) view.findViewById(R.id.device_list__description)).setText(d.getDescription());
        return view;
    }
}
