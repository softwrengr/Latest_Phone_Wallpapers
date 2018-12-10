package com.squaresdevelopers.latestphonewallpapers.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.squaresdevelopers.latestphonewallpapers.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // GeneralUtils.connectFragment(MainActivity.this,new TabsFragment());

        startActivity(new Intent(MainActivity.this,NavigationActivity.class));

    }
}
