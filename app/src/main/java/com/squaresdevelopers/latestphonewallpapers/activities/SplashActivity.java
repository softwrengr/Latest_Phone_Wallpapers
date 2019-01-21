package com.squaresdevelopers.latestphonewallpapers.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.squaresdevelopers.latestphonewallpapers.R;
import com.squaresdevelopers.latestphonewallpapers.controllers.CategoriesAdapter;
import com.squaresdevelopers.latestphonewallpapers.dataModels.categoryListDataModel.CategorResponseModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.categoryListDataModel.CategoryDetailModel;
import com.squaresdevelopers.latestphonewallpapers.networking.ApiClient;
import com.squaresdevelopers.latestphonewallpapers.networking.ApiInterface;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.getSupportActionBar().hide();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, 2000);
    }
}
