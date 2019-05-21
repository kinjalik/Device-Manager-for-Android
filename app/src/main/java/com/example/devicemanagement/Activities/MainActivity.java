package com.example.devicemanagement.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.devicemanagement.Fragments.DeviceListFragment;
import com.example.devicemanagement.Fragments.ProfileInfoFragment;
import com.example.devicemanagement.Network.Authorisation;
import com.example.devicemanagement.R;

/*
    IT IS NOT AN ENTRY POINT OF AN APP!
    InitActivity IS AN ENTRY POINT!
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static String LOG_TAG = "MAIN_A";

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_layout);
        Snackbar.make(findViewById(R.id.drawer_layout), "Welcome to Device Manager", Snackbar.LENGTH_SHORT).show();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close);

//        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        NavigationView navigation = findViewById(R.id.s_main__navigation);
        navigation.setNavigationItemSelectedListener(this);

        fm = getSupportFragmentManager();

        /*
            USING DEVICE LIST AS MAIN SCREEN
         */
        Fragment f;
        try {
            f = DeviceListFragment.class.newInstance();
        } catch (IllegalAccessException e) {
            f = null;
            e.printStackTrace();
        } catch (InstantiationException e) {
            f = null;
            e.printStackTrace();
        }

        assert f != null;
        fm.beginTransaction().replace(R.id.s_main__container, f).commit();

        Button b = findViewById(R.id.s_main__logout);
        b.setOnClickListener(logoutBtn);

        Log.i(LOG_TAG, "Started.");
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        toggle.onConfigurationChanged(newConfig);
    }

    /*
        NAVIGATION SECTION
     */

    FragmentManager fm;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.i(LOG_TAG, "Changing current screen.");
        Fragment fragment = null;
        Class fragmentClass = null;

        int id = menuItem.getItemId();

        switch (id) {
            case R.id.s_main__nav_btn__device_list:
                fragmentClass = DeviceListFragment.class;
                Log.i(LOG_TAG, "Device List selected.");
                break;
            case R.id.s_main__nav_btn__profile:
                fragmentClass = ProfileInfoFragment.class;
                break;
            default:
               Log.i(LOG_TAG, "Selected screen doesn't exists.");
        }

        try {
            if (fragmentClass != null) {
                fragment = (Fragment) fragmentClass.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (fragment != null) {
            fm.beginTransaction().replace(R.id.s_main__container, fragment).commit();
        }
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    // Logout btn
    View.OnClickListener logoutBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Snackbar.make(v,"You have succesfully logged out.", Snackbar.LENGTH_SHORT);
            Authorisation.getInstance(getApplicationContext()).logout();
            Intent i = new Intent(MainActivity.this, InitActivity.class);
            startActivity(i);
        }
    };
    
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        moveTaskToBack(true);
    }
    
    
}
