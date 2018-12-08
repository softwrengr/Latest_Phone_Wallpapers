package com.squaresdevelopers.latestphonewallpapers.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squaresdevelopers.latestphonewallpapers.R;
import com.squaresdevelopers.latestphonewallpapers.dataModels.wallpaperDataModel.ItemReponseModel;
import com.squaresdevelopers.latestphonewallpapers.networking.ApiClient;
import com.squaresdevelopers.latestphonewallpapers.networking.ApiInterface;
import com.squaresdevelopers.latestphonewallpapers.utils.GeneralUtils;

import retrofit2.Call;
import retrofit2.Callback;


public class LikeFragment extends Fragment {
    View view;
    String strDeviceID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_like, container, false);
       // customActionBar();
        strDeviceID = GeneralUtils.getDeviceID(getActivity());

        return view;


    }

    public void customActionBar() {
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView tvTitle = mCustomView.findViewById(R.id.title);
        tvTitle.setText("Liked WallPaper");
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.show();
    }


}
