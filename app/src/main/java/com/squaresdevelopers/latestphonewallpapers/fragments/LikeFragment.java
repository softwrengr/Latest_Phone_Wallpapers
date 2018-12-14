package com.squaresdevelopers.latestphonewallpapers.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.squaresdevelopers.latestphonewallpapers.R;
import com.squaresdevelopers.latestphonewallpapers.controllers.ItemAdapter;
import com.squaresdevelopers.latestphonewallpapers.controllers.LikeItemAdapter;
import com.squaresdevelopers.latestphonewallpapers.dataBase.LikedImagesCurd;
import com.squaresdevelopers.latestphonewallpapers.dataModels.LikedImagesmodel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.likeDataModel.LikeDetailModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.likeDataModel.LikeResponseModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.showLikeDataModel.ShowDetailModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.showLikeDataModel.ShowLikeResponseModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.wallpaperDataModel.ItemDetailModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.wallpaperDataModel.ItemReponseModel;
import com.squaresdevelopers.latestphonewallpapers.networking.ApiClient;
import com.squaresdevelopers.latestphonewallpapers.networking.ApiInterface;
import com.squaresdevelopers.latestphonewallpapers.utils.AlertUtils;
import com.squaresdevelopers.latestphonewallpapers.utils.GeneralUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;


public class LikeFragment extends Fragment {
    AlertDialog alertDialog;
    View view;
    String strDeviceID;
    @BindView(R.id.gv_like_items)
    GridView gvLikeItems;
    ArrayList<ShowDetailModel> itemReponseModelArrayList;
    LikeItemAdapter likeItemAdapter;

    LikedImagesCurd likedImagesCurd;
    List<LikedImagesmodel> likedImagesmodels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_like, container, false);
        customActionBar();

        likedImagesCurd = new LikedImagesCurd(getActivity());






        initUI();


        return view;


    }

    private void initUI() {
        ButterKnife.bind(this,view);
        strDeviceID = GeneralUtils.getDeviceID(getActivity());
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        itemReponseModelArrayList = new ArrayList<>();
        likeItemAdapter = new LikeItemAdapter(getActivity(), itemReponseModelArrayList);
        gvLikeItems.setAdapter(likeItemAdapter);
        apiCall();

    }

    private void apiCall(){
        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ShowLikeResponseModel> categoriesResponseModelCall = services.showLikeItems(strDeviceID);
        categoriesResponseModelCall.enqueue(new Callback<ShowLikeResponseModel>() {
            @Override
            public void onResponse(Call<ShowLikeResponseModel> call, retrofit2.Response<ShowLikeResponseModel> response) {

                alertDialog.dismiss();
                itemReponseModelArrayList.addAll(response.body().getData());
                likeItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ShowLikeResponseModel> call, Throwable t) {
                Log.d("zma",t.getMessage());
                alertDialog.dismiss();
            }
        });

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
