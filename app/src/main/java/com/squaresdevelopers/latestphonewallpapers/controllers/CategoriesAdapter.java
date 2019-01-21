package com.squaresdevelopers.latestphonewallpapers.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squaresdevelopers.latestphonewallpapers.R;
import com.squaresdevelopers.latestphonewallpapers.dataModels.CategoryModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.categoryListDataModel.CategoryDetailModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.wallpaperDataModel.ItemDetailModel;
import com.squaresdevelopers.latestphonewallpapers.fragments.ItemsFragment;
import com.squaresdevelopers.latestphonewallpapers.utils.GeneralUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by eapple on 30/11/2018.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {
    private Context context;
    private List<CategoryDetailModel> categoryModelList;


    public CategoriesAdapter(Activity context, ArrayList<CategoryDetailModel> categoryModelList) {
        this.context = context;
        this.categoryModelList = categoryModelList;
     //   Collections.reverse(categoryModelList);

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_categories_layout, parent, false);
               // Collections.reverse(categoryModelList);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final CategoryDetailModel model = categoryModelList.get(position);

        holder.tvName.setText(model.getCategoryName());

        holder.ivLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralUtils.putStringValueInEditor(context, "id", String.valueOf(model.getId()));
                GeneralUtils.putStringValueInEditor(context, "name", model.getCategoryName());
                GeneralUtils.connectFragmentWithDrawer(context, new ItemsFragment());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        FrameLayout ivLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.name);
            ivLayout = itemView.findViewById(R.id.category_layout);

        }
    }
}
