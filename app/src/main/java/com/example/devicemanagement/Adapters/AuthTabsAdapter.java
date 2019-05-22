package com.example.devicemanagement.Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.devicemanagement.Fragments.LoginFragment;
import com.example.devicemanagement.Fragments.RegisterFragment;

public class AuthTabsAdapter extends FragmentPagerAdapter {
    @SuppressWarnings("FieldCanBeLocal")
    final private int TAB_COUNT = 2;

    public AuthTabsAdapter(FragmentManager fm, Context ctx) {
        super(fm);
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return LoginFragment.getInstance();
            case 1:
                return RegisterFragment.getInstance();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Authorisation";
            case 1:
                return "Registration";
            default:
                return null;
        }
    }
}
