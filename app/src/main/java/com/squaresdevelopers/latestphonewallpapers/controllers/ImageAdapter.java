package com.squaresdevelopers.latestphonewallpapers.controllers;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.squaresdevelopers.latestphonewallpapers.R;
import com.squaresdevelopers.latestphonewallpapers.dataModels.wallpaperDataModel.ItemDetailModel;

import java.util.List;

public class ImageAdapter extends PagerAdapter {
    Context context;
    List<ItemDetailModel> wallPaperDetailModelList;
    LayoutInflater mLayoutInflater;
    private ImageListener listener;

    public ImageAdapter(Context context, List<ItemDetailModel> wallPaperDetailModelList) {
        this.context = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.wallPaperDetailModelList = wallPaperDetailModelList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        int count = 0;
        count = wallPaperDetailModelList.size();
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        final ImageView imageView = (ImageView) itemView.findViewById(R.id.image);
        ItemDetailModel model = wallPaperDetailModelList.get(position);
        Glide.with(context).load(model.getImage()).into(imageView);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public interface ImageListener {
        void onImageListener(String imageID, String imageUrl, ImageView imageView);
    }

}
