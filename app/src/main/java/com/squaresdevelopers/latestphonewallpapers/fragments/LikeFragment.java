package com.squaresdevelopers.latestphonewallpapers.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.squaresdevelopers.latestphonewallpapers.R;
import com.squaresdevelopers.latestphonewallpapers.controllers.LikeItemAdapter;
import com.squaresdevelopers.latestphonewallpapers.dataBase.LikedImagesCurd;
import com.squaresdevelopers.latestphonewallpapers.dataModels.LikedImagesmodel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.likeDataModel.LikeWallPaperModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.showLikeDataModel.ShowDetailModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.showLikeDataModel.ShowLikeResponseModel;
import com.squaresdevelopers.latestphonewallpapers.networking.ApiClient;
import com.squaresdevelopers.latestphonewallpapers.networking.ApiInterface;
import com.squaresdevelopers.latestphonewallpapers.utils.AlertUtils;
import com.squaresdevelopers.latestphonewallpapers.utils.GeneralUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;


public class LikeFragment extends Fragment {
    View view;
    @BindView(R.id.gv_like_items)
    GridView gvLikeItems;
    ArrayList<LikeWallPaperModel> likedImagesmodelArrayList;
    LikeItemAdapter likeItemAdapter;

    LikedImagesCurd likedImagesCurd;
    GridLayoutManager layoutManager;

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
        ButterKnife.bind(this, view);
        layoutManager = new GridLayoutManager(getActivity(), 3);
        likedImagesmodelArrayList = new ArrayList<>();
        showLikeImages();

    }

    public void showLikeImages() {
        Cursor cursor = likedImagesCurd.getProducts();
        while (cursor.moveToNext()) {
            String imageID = cursor.getString(1).trim();
            String imagetUrl = cursor.getString(2).trim();

            LikeWallPaperModel model = new LikeWallPaperModel();
            model.setImageID(imageID);
            model.setImageUrl(imagetUrl);

            likedImagesmodelArrayList.add(model);
            likeItemAdapter = new LikeItemAdapter(getActivity(), likedImagesmodelArrayList);
            gvLikeItems.setAdapter(likeItemAdapter);
            likeItemAdapter.notifyDataSetChanged();
        }
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
