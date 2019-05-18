package com.example.devicemanagement.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devicemanagement.Entities.DeviceProperty;
import com.example.devicemanagement.R;

public class PropertyListAdapter extends RecyclerView.Adapter<PropertyListAdapter.ViewHolder> {
    private final static String LOG_TAG = "PROPERTY_LIST_RECYCLER_ADAPTER";
    private DeviceProperty[] props;

    public PropertyListAdapter(DeviceProperty[] propsArr) {
        props = propsArr;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View propView = inflater.inflate(R.layout.fragment_props_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(propView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        DeviceProperty prop = props[position];
        TextView name = viewHolder.propName;
        name.setText(prop.name);
        TextView description = viewHolder.propValue;
        description.setText(prop.value);

        viewHolder.itemView.setTag(position);
//        viewHolder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        if (props == null)
            return 0;
        return props.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView propName;
        public TextView propValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            propName = itemView.findViewById(R.id.s_device_props__prop_name);
            propValue = itemView.findViewById(R.id.s_device_props__prop_value);
        }
    }
}
