package com.squaresdevelopers.latestphonewallpapers.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squaresdevelopers.latestphonewallpapers.R;
import com.squaresdevelopers.latestphonewallpapers.dataBase.LikedImagesCurd;
import com.squaresdevelopers.latestphonewallpapers.dataModels.WallpaperItems;
import com.squaresdevelopers.latestphonewallpapers.dataModels.wallpaperDataModel.ItemDetailModel;
import com.squaresdevelopers.latestphonewallpapers.dataModels.wallpaperDataModel.ItemReponseModel;
import com.squaresdevelopers.latestphonewallpapers.fragments.WallPaperFragment;
import com.squaresdevelopers.latestphonewallpapers.utils.GeneralUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by eapple on 01/12/2018.
 */

public class ItemAdapter extends BaseAdapter {
    ArrayList<ItemDetailModel> itemReponseModelArrayList;
    Context context;
    private LayoutInflater layoutInflater;
    MyViewHolder viewHolder = null;
    private LikedImagesCurd likedImagesCurd;

    public ItemAdapter(Context context, ArrayList<ItemDetailModel> itemReponseModelArrayList) {
        this.itemReponseModelArrayList = itemReponseModelArrayList;
        this.context = context;

        likedImagesCurd = new LikedImagesCurd(context);
        if (context != null) {
            this.layoutInflater = LayoutInflater.from(context);

        }
    }

    @Override
    public int getCount() {
        if (itemReponseModelArrayList != null) return itemReponseModelArrayList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (itemReponseModelArrayList != null && itemReponseModelArrayList.size() > position)
            return itemReponseModelArrayList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (itemReponseModelArrayList != null && itemReponseModelArrayList.size() > position)
            return itemReponseModelArrayList.size();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ItemDetailModel model = itemReponseModelArrayList.get(position);

        viewHolder = new MyViewHolder();
        convertView = layoutInflater.inflate(R.layout.wallpaper_layout, parent, false);
        viewHolder.imageView = convertView.findViewById(R.id.iv_wallpaper);
        viewHolder.layout = convertView.findViewById(R.id.layout);

        Glide.with(context).load(model.getImage()).into(viewHolder.imageView);

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = likedImagesCurd.checkImageUrl(model.getImage());
                GeneralUtils.putBooleanValueInEditor(context, "like_image", b);
                GeneralUtils.putStringValueInEditor(context, "image", model.getImage());
                GeneralUtils.putStringValueInEditor(context, "model_no", model.getModelNumber());
                GeneralUtils.connect(context, new WallPaperFragment());
            }
        });

        convertView.setTag(viewHolder);
        return convertView;
    }

    private class MyViewHolder {
        RelativeLayout layout;
        ImageView imageView;
    }
}
