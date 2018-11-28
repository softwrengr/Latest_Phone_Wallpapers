package com.squaresdevelopers.latestphonewallpapers.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.squaresdevelopers.latestphonewallpapers.R;
import com.squaresdevelopers.latestphonewallpapers.fragments.HomeFragment;
import com.squaresdevelopers.latestphonewallpapers.utils.GeneralUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GeneralUtils.connectFragment(MainActivity.this,new HomeFragment());

    }
}
