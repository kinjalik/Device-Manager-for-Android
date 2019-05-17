package com.example.devicemanagement.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.devicemanagement.DeviceListRecyclerAdapter;
import com.example.devicemanagement.Entities.Device;
import com.example.devicemanagement.Fragments.DeviceDetailFragment;
import com.example.devicemanagement.Network.BackendApi;
import com.example.devicemanagement.Network.NetworkService;
import com.example.devicemanagement.R;
import com.google.gson.Gson;

import java.util.Objects;

public class DeviceDetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = "DEIVCE_DETAIL_A";
    private boolean mTwoPane = false;
    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_UID = "id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        assert savedInstanceState != null;
        String deviceJson = Objects.requireNonNull(getIntent().getExtras()).getString(DeviceListRecyclerAdapter.DEVICE_DATA);
        Gson gson = new Gson();
        Device device = gson.fromJson(deviceJson, Device.class);
        BackendApi api = NetworkService.getInstance().getApi();

        Log.i(LOG_TAG, "Using device info from Intent");
        final Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setTitle(device.getName());
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FragmentManager fm = getSupportFragmentManager();
        DeviceDetailFragment ddf = DeviceDetailFragment.newInstance(device);
        fm.beginTransaction()
                .replace(R.id.device_detail_container, ddf)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
